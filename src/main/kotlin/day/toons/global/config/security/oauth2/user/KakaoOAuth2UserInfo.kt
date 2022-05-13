package day.toons.global.config.security.oauth2.user

import day.toons.global.config.security.oauth2.user.OAuth2UserInfo

class KakaoOAuth2UserInfo(attributes: MutableMap<String, Any>): OAuth2UserInfo(attributes) {
    override val id: String
        get() = (attributes["id"] as Int).toString()
    override val name: String
        get() = (attributes["properties"] as Map<*, *>)["nickname"] as String
    override val email: String
        get() = (attributes["kakao_account"] as Map<*, *>)["email"] as String
    override val imageUrl: String?
        get() = (attributes["properties"] as Map<*, *>)["thumbnail_image"] as String?
}