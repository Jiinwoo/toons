package day.toons.service

import day.toons.domain.member.MemberSms
import day.toons.domain.member.dto.CertificationCheckDTO
import day.toons.domain.member.dto.CertificationRequestDTO
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class MemberCertificationService(
    private val smsClient: MemberSms,
    private val redisTemplate: RedisTemplate<*, *>
) {
    fun send(dto: CertificationRequestDTO) {



        smsClient.send(dto.phoneNumber)
    }

    fun check(dto: CertificationCheckDTO) {
        TODO("Not yet implemented")
    }

}
