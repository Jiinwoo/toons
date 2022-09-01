package day.toons.domain.alarm

interface AlarmSms {
    suspend fun sendOut(phoneNumber: String, webtoonName: String): Result<Unit>
}