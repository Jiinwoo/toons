package day.toons.domain.webtoon

interface KAKAOCrawler {
    fun getWebtoonList(): Result<List<Webtoon>>
    fun getWebtoonTitles(): Result<List<String>>
}