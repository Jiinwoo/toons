package day.toons.infra.job

import day.toons.service.WebtoonService
import mu.KotlinLogging
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}
@Component
class CrawlingJob(
    private val webtoonService: WebtoonService
): QuartzJobBean(){
    override fun executeInternal(context: JobExecutionContext) {
        logger.info("===== 크롤링 작업 시작 =====")
        webtoonService.doCrawling()
        logger.info("===== 크롤링 작업 끝 =====")
    }
}