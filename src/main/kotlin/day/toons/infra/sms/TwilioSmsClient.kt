package day.toons.infra.sms

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber
import day.toons.domain.alarm.AlarmSms
import day.toons.domain.member.MemberSms
import kotlinx.coroutines.delay
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@Component
class TwilioSmsClient(
    @Value("\${app.twilio.account-sid}")
    private val accountSID: String,
    @Value("\${app.twilio.auth-token}")
    private val authToken: String,
    @Value("\${app.twilio.from-number}")
    private val fromNumber: String
) : MemberSms, AlarmSms {
    override fun send(phoneNumber: String, certificationNumber: String): Boolean {
        val message =
            Message.creator(PhoneNumber(phoneNumber), PhoneNumber(fromNumber), "인증번호를 입력해주세요 [$certificationNumber]")
                .create()
        logger.info(message.toString())
        return true
    }

    override suspend fun sendOut(phoneNumber: String, webtoonName: String): Result<Unit> = runCatching {
        val message =
            Message.creator(PhoneNumber(phoneNumber), PhoneNumber(fromNumber), "완결 웹툰 [$webtoonName]")
                .create()
        logger.info { message }
    }

    @PostConstruct
    fun init() {
        Twilio.init(accountSID, authToken)
    }

}