package day.toons.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate




@Configuration
class RedisConfig(
    @Value("\${spring.redis.host}")
    private val redisHost: String,
    @Value("\${spring.redis.port}")
    private val redisPort: Int
) {

    @Bean
    fun redisConnectionFactory (): RedisConnectionFactory{
        return LettuceConnectionFactory(redisHost, redisPort)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<*, *> {
        val redisTemplate: RedisTemplate<*, *> = RedisTemplate<Any, Any>()
        redisTemplate.setConnectionFactory(redisConnectionFactory())
        return redisTemplate
    }

}