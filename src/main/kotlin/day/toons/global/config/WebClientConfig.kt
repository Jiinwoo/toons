package day.toons.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://gateway-kw.kakao.com")
            .codecs { defaultCodecs ->
                defaultCodecs.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)
            }
            .build()
    }
}