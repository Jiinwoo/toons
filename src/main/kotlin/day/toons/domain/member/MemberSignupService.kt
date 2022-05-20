package day.toons.domain.member

import day.toons.domain.member.dto.MemberCreateDTO
import day.toons.domain.member.exception.EmailDuplicateException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberSignupService(
    private val memberRepository: MemberRepository,
    private val encoder: PasswordEncoder
) {
    fun signup(dto: MemberCreateDTO.Req): MemberCreateDTO.Res {
        if (memberRepository.existsByEmail(dto.email)) {
            throw EmailDuplicateException(dto.email)
        }
        val member: Member = memberRepository.save(
            Member(
                email = dto.email, username = dto.username,
                encryptedPassword = encoder.encode(dto.password),
                provider = AuthProvider.local,
            )
        )
        return MemberCreateDTO.Res(
            email = member.email,
            username = member.username
        )
    }
}
