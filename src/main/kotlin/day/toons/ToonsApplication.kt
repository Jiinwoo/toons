package day.toons

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ToonsApplication

fun main(args: Array<String>) {
    runApplication<ToonsApplication>(*args)
}
