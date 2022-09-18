package day.toons.service

import day.toons.domain.alarm.*
import day.toons.domain.member.MemberRepository
import day.toons.domain.member.exception.MemberNotFoundException
import day.toons.domain.webtoon.WebtoonRepository
import day.toons.domain.webtoon.exception.WebtoonNotFoundException
import day.toons.global.config.security.MemberPrincipal
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class AlarmService(
    private val alarmRepository: AlarmRepository,
    private val webtoonRepository: WebtoonRepository,
    private val memberRepository: MemberRepository
) {
    fun putAlarm(principal: MemberPrincipal, dto: AlarmCreateDTO) {
        val member = memberRepository.findByEmail(principal.getEmail()).orElseThrow {
            MemberNotFoundException(principal.getEmail())
        }
        val webtoon = webtoonRepository.findById(dto.webtoonId).orElseThrow {
            WebtoonNotFoundException(dto.webtoonId)
        }

        if (alarmRepository.findByMemberAndWebtoonName(member, webtoon.name).isPresent) return

        val alarm = Alarm(
            webtoon = webtoon,
            member = member
        )
        alarmRepository.save(alarm)
    }

    fun getAlarms(principal: MemberPrincipal): List<AlarmDTO> {
        val member = memberRepository.findByEmail(principal.getEmail()).orElseThrow {
            MemberNotFoundException(principal.getEmail())
        }
        return alarmRepository.findAllByMember(member).map {
            AlarmDTO(
                id = it.id!!,
                webtoonDTO = AlarmWebtoonDTO(
                    name = it.webtoon.name,
                    thumbnail = it.webtoon.thumbnail,
                    dayOfWeek = it.webtoon.dayOfWeek,
                    platform = it.webtoon.platform,
                    link = it.webtoon.link,
                    deletedAt = it.webtoon.deletedAt
                )
            )
        }
    }

    fun delete(principal: MemberPrincipal, alarmId: Long) {
        val member = memberRepository.findByEmail(principal.getEmail()).orElseThrow {
            MemberNotFoundException(principal.getEmail())
        }
        val alarm = alarmRepository.findByMemberAndId(member, alarmId).orElseThrow {
            AlarmNotFoundException(alarmId)
        }
        alarmRepository.delete(alarm)
    }
}