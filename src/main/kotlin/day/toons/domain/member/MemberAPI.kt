package day.toons.domain.member

import day.toons.domain.member.dto.*
import day.toons.global.config.security.MemberPrincipal
import day.toons.service.MemberCertificationService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/members")
class MemberAPI(
    private val memberService: MemberService,
    private val memberCertificationService: MemberCertificationService
) {
    @GetMapping
    fun getMember(
        @MemberAuth memberPrincipal: MemberPrincipal
    ): MemberDTO {
        return memberService.getMember(memberPrincipal.getEmail())
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createMember(
        @Validated @RequestBody dto: MemberCreateDTO.Req
    ): MemberCreateDTO.Res {
        return memberService.signup(dto)
    }

    @PatchMapping("/phone-number")
    fun updatePhoneNumber(
        @MemberAuth principal: MemberPrincipal,
        @Validated @RequestBody dto: MemberPhoneUpdateDTO.Req
    ) {
        memberService.updatePhoneNumber(principal.getEmail(), dto)
    }

    @PostMapping("/certification/send")
    fun send(
        @Validated @RequestBody dto: CertificationRequestDTO
    ) {
        memberCertificationService.send(dto)
    }

    @PostMapping("/certification/check")
    fun check(
        @RequestBody dto: CertificationCheckDTO
    ) {
        memberCertificationService.check(dto)
    }
}