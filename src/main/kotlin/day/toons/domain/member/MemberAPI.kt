package day.toons.domain.member

import day.toons.domain.member.dto.CertificationCheckDTO
import day.toons.domain.member.dto.CertificationRequestDTO
import day.toons.domain.member.dto.MemberCreateDTO
import day.toons.service.MemberCertificationService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/members")
class MemberAPI(
    private val memberSignupService: MemberSignupService,
    private val memberCertificationService: MemberCertificationService
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createMember(
        @Validated @RequestBody dto: MemberCreateDTO.Req
    ): MemberCreateDTO.Res {
        return memberSignupService.signup(dto);
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