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
        logger.info("===== 전체 웹툰 목록 크롤링 작업 시작 =====")
        webtoonService.crawlingSerialize()
        logger.info("===== 전체 웹툰 목록 크롤링 작업 끝   =====")
        logger.info("===== 완결 웹툰 목록 크롤링 작업 시작 =====")
        webtoonService.crawlingComplete()
        logger.info("===== 완결 웹툰 목록 크롤링 작업 끝   =====")
    }
}