package day.toons

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

private val logger = KotlinLogging.logger {}

@EnableAsync
@SpringBootApplication
class ToonsApplication

fun main(args: Array<String>) {
    runApplication<ToonsApplication>(*args)
}
