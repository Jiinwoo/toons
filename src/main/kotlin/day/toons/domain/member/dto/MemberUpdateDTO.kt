package day.toons.domain.member.dto

import javax.validation.constraints.Pattern

class MemberUpdateDTO {
    data class Req(
        val username: String,
        @Pattern(regexp="^\\+[1-9]\\d{1,14}$", message = "휴대폰 형식이 일치하지 않습니다.")
        val phoneNumber: String
    )
    data class Res(
        val username: String,
        val phoneNumber: String
    )
}