package day.toons.global.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun publicAPI(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("Webtoon API")
        .pathsToMatch("/api/**")
        .build()

    @Bean
    fun webtoonsAPI(): OpenAPI = OpenAPI()
        .info(
            Info().title("Webtoons API")
                .description("Webtoon API 명세")
                .version("v0.0.1")
        )

}