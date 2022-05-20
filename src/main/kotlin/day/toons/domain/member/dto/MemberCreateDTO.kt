package day.toons.domain.member.dto

class MemberCreateDTO {

    data class Req(
        var email: String,
        var username: String,
        var password: String,
    )

    data class Res(
        var email: String,
        var username: String,
    )
}