package day.toons.global.util

import java.io.UnsupportedEncodingException
import java.net.URL
import java.net.URLDecoder
import java.util.*


object URLUtils {

    @Throws(UnsupportedEncodingException::class)
    fun splitQuery(url: URL): Map<String, MutableList<String?>> {
        val queryPairs: MutableMap<String, MutableList<String?>> = LinkedHashMap()
        val pairs: List<String> = url.query.split("&")
        for (pair in pairs) {
            val idx = pair.indexOf("=")
            val key = if (idx > 0) URLDecoder.decode(pair.substring(0, idx), "UTF-8") else pair
            if (!queryPairs.containsKey(key)) {
                queryPairs[key] = LinkedList()
            }
            val value =
                if (idx > 0 && pair.length > idx + 1) URLDecoder.decode(pair.substring(idx + 1), "UTF-8") else null
            queryPairs[key]!!.add(value)
        }
        return queryPairs
    }

}