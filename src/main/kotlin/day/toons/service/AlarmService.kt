package day.toons.service

import day.toons.domain.alarm.*
import day.toons.domain.member.MemberRepository
import day.toons.domain.member.exception.MemberNotFoundException
import day.toons.domain.webtoon.WebtoonRepository
import day.toons.domain.webtoon.dto.WebtoonDTO
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
    fun putAlarm(principal: MemberPrincipal, dto: AlarmCreateDTO): AlarmDTO{
        val member = memberRepository.findByEmail(principal.getEmail()).orElseThrow {
            MemberNotFoundException(principal.getEmail())
        }
        val webtoon = webtoonRepository.findById(dto.webtoonId).orElseThrow {
            WebtoonNotFoundException(dto.webtoonId)
        }

        val exist = alarmRepository.findByMemberAndWebtoonName(member, webtoon.name)
        if (exist.isPresent) {
            val alarm = exist.get()
            return AlarmDTO(
                id = alarm.id!!,
                webtoonDTO = AlarmWebtoonDTO(
                    name = alarm.webtoon.name,
                    thumbnail = alarm.webtoon.thumbnail,
                    dayOfWeek = alarm.webtoon.dayOfWeek,
                    platform = alarm.webtoon.platform,
                    link = alarm.webtoon.link,
                    deletedAt = alarm.webtoon.deletedAt
                ),
            )        }

        val alarm = Alarm(
            webtoon = webtoon,
            member = member
        )
        val savedAlarm = alarmRepository.save(alarm)
        return AlarmDTO(
            id = savedAlarm.id!!,
            webtoonDTO = AlarmWebtoonDTO(
                name = savedAlarm.webtoon.name,
                thumbnail = savedAlarm.webtoon.thumbnail,
                dayOfWeek = savedAlarm.webtoon.dayOfWeek,
                platform = savedAlarm.webtoon.platform,
                link = savedAlarm.webtoon.link,
                deletedAt = savedAlarm.webtoon.deletedAt
            )

        )
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