package day.toons.global.config.security

class LoginDTO {

    data class Req(
        val email: String,
        val password: String,
    )

}