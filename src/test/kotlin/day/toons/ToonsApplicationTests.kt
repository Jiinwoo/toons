package day.toons

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [TestConfiguration::class])
@ActiveProfiles(value = ["test"])
class ToonsApplicationTests {

    @Test
    fun contextLoads() {
    }

}
