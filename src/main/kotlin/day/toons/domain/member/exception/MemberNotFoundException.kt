package day.toons.domain.member.exception

import day.toons.global.error.exception.EntityNotFoundException

class MemberNotFoundException(email: String) : EntityNotFoundException("해당하는 유저가 없습니다. : $email") {
}