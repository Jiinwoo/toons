package day.toons.global.config.security.oauth2

import day.toons.domain.member.AuthProvider
import day.toons.domain.member.Member
import day.toons.domain.member.repository.MemberRepository
import day.toons.global.config.security.MemberPrincipal
import day.toons.global.config.security.oauth2.user.GoogleOAuth2UserInfo
import day.toons.global.config.security.oauth2.user.KakaoOAuth2UserInfo
import day.toons.global.config.security.oauth2.user.OAuth2UserInfo
import org.apache.commons.lang3.StringUtils
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import javax.naming.AuthenticationException

@Service
class CustomOAuth2UserService(
        private val memberRepository: MemberRepository,
) : DefaultOAuth2UserService() {
    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(oAuth2UserRequest)
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun processOAuth2User(oAuth2UserRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {

        val oAuth2UserInfo =
            getOAuth2UserInfo(oAuth2UserRequest.clientRegistration.registrationId, oAuth2User.attributes)
        if (StringUtils.isBlank(oAuth2UserInfo.email)) {
            throw OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider")
        }

        val memberOptional = memberRepository.findByEmail(oAuth2UserInfo.email)

        val member = memberOptional.map { member ->
            if (member.provider != AuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId)) {
                throw OAuth2AuthenticationProcessingException(
                    """이미 ${member.provider} 계정이 있습니다.
                    | ${member.provider} 계정으로 로그인해주세요
                """.trimMargin()
                )
            }
            updateExistingMember(member, oAuth2UserInfo)
        }.orElseGet {
            registerNewUser(oAuth2UserRequest, oAuth2UserInfo)
        }
//        if(oAuth2UserRequest.clientRegistration.registrationId.equals(AuthProvider.naver.toString(),false))
//            return MemberPrincipal.create(member, oAuth2User.attributes["response"] as MutableMap<String, Any>)

        return MemberPrincipal.create(member, oAuth2User.attributes)
    }

    private fun getOAuth2UserInfo(registrationId: String, attributes: MutableMap<String, Any>) =
        when (registrationId.lowercase()) {
            AuthProvider.kakao.toString() -> KakaoOAuth2UserInfo(attributes)
            AuthProvider.google.toString() -> GoogleOAuth2UserInfo(attributes)
            else -> throw OAuth2AuthenticationProcessingException("죄송합니다. 현재 ${registrationId}는 지원하지 않고있습니다.")
        }

    private fun registerNewUser(oAuth2UserRequest: OAuth2UserRequest, oAuth2UserInfo: OAuth2UserInfo): Member{
        return memberRepository.save(
            Member(
                provider = AuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId),
                providerId = oAuth2UserInfo.id,
                username = oAuth2UserInfo.name,
                email = oAuth2UserInfo.email,
            )
        )
    }

    private fun updateExistingMember(existingMember: Member, oAuth2UserInfo: OAuth2UserInfo): Member {
        return memberRepository.save(existingMember.update(
            username = oAuth2UserInfo.name
        ))
    }
}
