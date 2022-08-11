package day.toons.domain.member.dto

import com.twilio.type.PhoneNumber
import javax.validation.constraints.Pattern

class MemberPhoneUpdateDTO {
    data class Req(
        @Pattern(regexp="^\\+[1-9]\\d{1,14}$", message = "휴대폰 형식이 일치하지 않습니다.")
        val phoneNumber: String
    )
}