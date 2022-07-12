package day.toons

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

private val logger = KotlinLogging.logger {}
@SpringBootApplication
class ToonsApplication

fun main(args: Array<String>) {
    runApplication<ToonsApplication>(*args)
}
