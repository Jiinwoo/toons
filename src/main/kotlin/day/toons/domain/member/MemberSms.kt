package day.toons.domain.member

interface MemberSms {
    fun send(phoneNumber: String, certificationNumber: String): Boolean
}