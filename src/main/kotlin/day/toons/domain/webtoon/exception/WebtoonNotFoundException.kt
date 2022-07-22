package day.toons.domain.webtoon.exception

import day.toons.global.error.exception.EntityNotFoundException

class WebtoonNotFoundException(webtoonId: Long) : EntityNotFoundException("웹툰을 찾을 수 없습니다. : $webtoonId") {
}