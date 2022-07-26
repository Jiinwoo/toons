package day.toons.global.config

import day.toons.infra.job.CrawlingJob
import org.quartz.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import javax.annotation.PostConstruct

@Configuration
class QuartzConfig(
    private val scheduler: Scheduler,
    @Value("\${spring.quartz.cron-expression.crawling}")
    private val CRAWLING_CRON_EXPRESSION: String,
    private val env: Environment
) {
    @PostConstruct
    fun register() {
        val jobKey = JobKey(CrawlingJob::class.simpleName, "main")
        val job = JobBuilder.newJob()
            .ofType(CrawlingJob::class.java)
            .withIdentity(jobKey)
            .build()
        val trigger = getTrigger()
        if(!scheduler.checkExists(jobKey)) {
            scheduler.scheduleJob(job, trigger)
        }
    }

    private fun getTrigger(): Trigger {
        return if (env.activeProfiles.contains("local")) {
            TriggerBuilder.newTrigger()
                .build()
        } else {
            TriggerBuilder.newTrigger()
                .withIdentity(TriggerKey("크롤링 주기", "main"))
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(CRAWLING_CRON_EXPRESSION))
                .build()

        }
    }

}