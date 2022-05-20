package day.toons.domain.member

import day.toons.domain.member.dto.MemberCreateDTO
import day.toons.global.config.security.MemberPrincipal
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberSignupService: MemberSignupService
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createMember(
        @Validated @RequestBody dto: MemberCreateDTO.Req
    ): MemberCreateDTO.Res {
        return memberSignupService.signup(dto);
    }
    @GetMapping
    fun getMember (
        @MemberAuth principal: MemberPrincipal
    ): String {
        println("principal = ${principal.username}")
        println("principal = ${principal.password}")
        println("principal = ${principal.getEmail()}")
        return "asd"
    }
}