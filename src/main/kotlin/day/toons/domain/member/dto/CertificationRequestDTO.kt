package day.toons.domain.member.dto

import javax.validation.constraints.Pattern

class CertificationRequestDTO(
    @Pattern(regexp="^\\+[1-9]\\d{1,14}$", message = "휴대폰 형식이 일치하지 않습니다.")
    val phoneNumber: String
)

class CertificationCheckDTO(
    val method: CertificationMethod,
    val value: String
)

enum class CertificationMethod {
    SMS, EMAIL
}