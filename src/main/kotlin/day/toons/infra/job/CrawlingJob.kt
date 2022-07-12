package day.toons.infra.job

import day.toons.global.config.security.CustomAuthenticationEntryPoint.Companion.logger
import day.toons.service.WebtoonService
import mu.toKLogger
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

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