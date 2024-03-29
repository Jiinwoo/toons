package day.toons.domain.member

import day.toons.domain.member.dto.MemberCreateDTO
import day.toons.domain.member.dto.MemberDTO
import day.toons.domain.member.dto.MemberPhoneUpdateDTO
import day.toons.domain.member.dto.MemberUpdateDTO
import day.toons.domain.member.exception.EmailDuplicateException
import day.toons.domain.member.exception.MemberNotFoundException
import day.toons.domain.member.repository.MemberRepository
import day.toons.global.error.exception.BusinessException
import day.toons.global.error.exception.ErrorCode
import day.toons.service.MemberCertificationService
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberService(
        private val memberRepository: MemberRepository,
        private val encoder: PasswordEncoder,
        private val redisTemplate: RedisTemplate<String, String>
) {


    fun signup(dto: MemberCreateDTO.Req): MemberCreateDTO.Res {
        if (memberRepository.existsByEmail(dto.email)) {
            throw EmailDuplicateException(dto.email)
        }
        if (!verifyAndRemoveAuthKey(dto.phoneNumber)) {
            throw BusinessException(ErrorCode.CODE_NOT_EQUAL)
        }


        val member: Member = memberRepository.save(
                Member(
                        email = dto.email, username = dto.username,
                        phoneNumber = dto.phoneNumber,
                        encryptedPassword = encoder.encode(dto.password),
                        provider = AuthProvider.local,
                )
        )

        return MemberCreateDTO.Res(
                email = member.email,
                username = member.username
        )
    }

    fun updatePhoneNumber(email: String, dto: MemberPhoneUpdateDTO.Req) {
        if (!verifyAndRemoveAuthKey(dto.phoneNumber)) {
            throw BusinessException(ErrorCode.CODE_NOT_EQUAL)
        }
        val foundMember = memberRepository.findByEmail(email).orElseThrow {
            throw MemberNotFoundException(email)
        }
        foundMember.update(
                phoneNumber = dto.phoneNumber
        )
    }

    fun updateMember(email: String, dto: MemberUpdateDTO.Req): MemberUpdateDTO.Res {
        val member = memberRepository.findByEmail(email).orElseThrow {
            throw MemberNotFoundException(email)
        }

        if (member.phoneNumber != dto.phoneNumber) {
            if (!verifyAndRemoveAuthKey(dto.phoneNumber)) {
                throw BusinessException(ErrorCode.CODE_NOT_EQUAL)
            }
        }

        member.update(
                username = dto.username,
                phoneNumber = dto.phoneNumber
        )
        return MemberUpdateDTO.Res(
                username = member.username,
                phoneNumber = member.phoneNumber ?: ""
        )
    }

    private fun verifyAndRemoveAuthKey(phoneNumber: String): Boolean {
        val smsAuthKey = MemberCertificationService.SUCCESS_PREFIX + phoneNumber
        if (!redisTemplate.hasKey(smsAuthKey)) {
            return false
        }
        if (redisTemplate.opsForValue().get(smsAuthKey) != phoneNumber) {
            return false
        }
        redisTemplate.delete(smsAuthKey)
        return true
    }

    fun getMember(email: String): MemberDTO {
        val member = memberRepository.findByEmail(email).orElseThrow {
            throw MemberNotFoundException(email)
        }
        return MemberDTO(
                email = member.email,
                username = member.username,
                phoneNumber = member.phoneNumber
        )
    }

}
