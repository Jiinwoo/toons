package day.toons.domain.webtoon

interface NAVERCrawler {
    fun getWebtoonList(): Result<List<Webtoon>>
    fun getCompletedWebtoonTitles(): Result<List<String>>
}