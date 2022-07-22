package day.toons.domain.alarm

import day.toons.global.error.exception.EntityNotFoundException

class AlarmNotFoundException(alarmId: Long) : EntityNotFoundException("알람을 찾을 수 없습니다. : $alarmId") {
}