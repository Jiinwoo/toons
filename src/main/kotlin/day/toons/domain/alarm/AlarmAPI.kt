package day.toons.domain.alarm

import day.toons.domain.member.MemberAuth
import day.toons.global.config.security.MemberPrincipal
import day.toons.service.AlarmService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/alarms")
class AlarmAPI(
    private val alarmService: AlarmService
) {
    @GetMapping
    fun getAlarms(
        @MemberAuth principal: MemberPrincipal
    ): List<AlarmDTO>{
        return alarmService.getAlarms(principal)
    }

    @PutMapping
    fun putAlarm(
        @MemberAuth principal: MemberPrincipal,
        @RequestBody dto: AlarmCreateDTO
    ): AlarmDTO {
        return alarmService.putAlarm(principal, dto)
    }


    @DeleteMapping("/{alarmId}")
    fun removeAlarm(
        @MemberAuth principal: MemberPrincipal,
        @PathVariable alarmId: Long
    ) {
        alarmService.delete(principal, alarmId)
    }


}