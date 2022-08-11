package day.toons.service

import day.toons.domain.member.MemberSms
import day.toons.domain.member.dto.CertificationCheckDTO
import day.toons.domain.member.dto.CertificationRequestDTO
import day.toons.global.error.exception.BusinessException
import day.toons.global.error.exception.ErrorCode
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Service
@Transactional
class MemberCertificationService(
    private val smsClient: MemberSms,
    private val redisTemplate: RedisTemplate<String, String>
) {
    companion object {
        const val SUCCESS_PREFIX = "success:"

        private const val PREFIX = "sms:"
        private const val CHECK_LIFE_TIME = 3L * 60L;
        private const val SIGNUP_LIFE_TIME = 60L * 60L;
    }

    fun send(dto: CertificationRequestDTO) {
        val certificationNumber = RandomStringUtils.randomAlphanumeric(6)
        redisTemplate.opsForValue()
            .set(PREFIX + dto.phoneNumber, certificationNumber, Duration.ofSeconds(CHECK_LIFE_TIME))
        if (!smsClient.send(dto.phoneNumber, certificationNumber)) {
            throw BusinessException(ErrorCode.SMS_FAIL)
        }
    }

    fun check(dto: CertificationCheckDTO) {
        if (!redisTemplate.hasKey(PREFIX + dto.phoneNumber)) {
            throw BusinessException(ErrorCode.CODE_NOT_FOUND)
        }
        if (redisTemplate.opsForValue().get(PREFIX + dto.phoneNumber) != dto.code) {
            throw BusinessException(ErrorCode.CODE_NOT_EQUAL)
        }
        redisTemplate.opsForValue().set(SUCCESS_PREFIX + dto.phoneNumber, dto.phoneNumber, Duration.ofSeconds(SIGNUP_LIFE_TIME))
    }

}
