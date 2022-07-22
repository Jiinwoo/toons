package day.toons.global.config

import day.toons.global.config.security.MemberPrincipalMethodArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(MemberPrincipalMethodArgumentResolver())
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        super.addCorsMappings(registry)
    }
}