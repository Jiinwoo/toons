package day.toons.global.config.security.oauth2.user

class GoogleOAuth2UserInfo(attributes: MutableMap<String, Any>) : OAuth2UserInfo(attributes) {
    override val id: String
        get() = attributes["sub"] as String
    override val name: String
        get() = attributes["name"] as String
    override val email: String
        get() = attributes["email"] as String
    override val imageUrl: String?
        get() = attributes["picture"] as String?
}