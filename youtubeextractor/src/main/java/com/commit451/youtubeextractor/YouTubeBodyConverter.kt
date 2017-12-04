package com.commit451.youtubeextractor

import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.HashMap

import okhttp3.ResponseBody
import retrofit2.Converter

/**
 * Converts the bodies for the YouTubes
 */
internal class YouTubeBodyConverter : Converter<ResponseBody, YouTubeExtraction> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): YouTubeExtraction {
        val html = value.string()

        val queryMap = getQueryMap(html, "UTF-8")

        if (queryMap.containsKey("url_encoded_fmt_stream_map")) {
            val streamQueries = queryMap["url_encoded_fmt_stream_map"]
                    ?.split(",".toRegex())
                    ?.dropLastWhile { it.isEmpty() }
                    ?.toMutableList()

            val adaptiveFmts: List<String>? = queryMap["adaptive_fmts"]
                    ?.split(",".toRegex())
                    ?.dropLastWhile { it.isEmpty() }

            adaptiveFmts?.let {
                streamQueries?.addAll(it)
            }

            val streamLinks = mutableMapOf<Int, String>()
            if (streamQueries != null) {
                for (streamQuery in streamQueries) {
                    val stream = getQueryMap(streamQuery, "UTF-8")
                    var urlString: String? = stream["url"]

                    if (urlString != null) {
                        val signature = stream["sig"]

                        if (signature != null) {
                            urlString = urlString + "&signature=" + signature
                        }

                        if (getQueryMap(urlString, "UTF-8").containsKey("signature")) {
                            streamLinks.put(Integer.parseInt(stream["itag"]), urlString)
                        }
                    }
                }
            }

            val sd240VideoUri = extractVideoUrl(YOUTUBE_VIDEO_QUALITY_SMALL_240, streamLinks)
            val sd360VideoUri = extractVideoUrl(YOUTUBE_VIDEO_QUALITY_MEDIUM_360, streamLinks)
            val hd720VideoUri = extractVideoUrl(YOUTUBE_VIDEO_QUALITY_HD_720, streamLinks)
            val hd1080VideoUri = extractVideoUrl(YOUTUBE_VIDEO_QUALITY_HD_1080, streamLinks)

            val mediumThumbUri = if (queryMap.containsKey("iurlmq")) queryMap["iurlmq"] else null
            val highThumbUri = if (queryMap.containsKey("iurlhq")) queryMap["iurlhq"] else null
            val defaultThumbUri = if (queryMap.containsKey("iurl")) queryMap["iurl"] else null
            val standardThumbUri = if (queryMap.containsKey("iurlsd")) queryMap["iurlsd"] else null
            //final String description = doc.select("p[id=\"eow-description\"]").first().html();

            val result = YouTubeExtraction()
            result.sd240VideoUrl = sd240VideoUri
            result.sd360VideoUrl = sd360VideoUri
            result.hd720VideoUrl = hd720VideoUri
            result.hd1080VideoUrl = hd1080VideoUri
            result.mediumThumbUrl = mediumThumbUri
            result.highThumbUrl = highThumbUri
            result.defaultThumbUrl = defaultThumbUri
            result.standardThumbUrl = standardThumbUri
            return result
        } else {
            throw YouTubeExtractionException("Status: " + queryMap["status"] + "\nReason: " + queryMap["reason"] + "\nError code: " + queryMap["errorcode"])
        }
    }

    @Throws(UnsupportedEncodingException::class)
    private fun getQueryMap(queryString: String, charsetName: String): HashMap<String, String> {
        val map = HashMap<String, String>()

        val fields = queryString.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for (field in fields) {
            val pair = field.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (pair.size == 2) {
                val key = pair[0]
                val value = URLDecoder.decode(pair[1], charsetName).replace('+', ' ')
                map.put(key, value)
            }
        }

        return map
    }

    private fun extractVideoUrl(quality: Int, streamLinks: MutableMap<Int, String>): String? {
        var videoUri: String? = null
        if (streamLinks[quality] != null) {
            val streamLink = streamLinks[quality]
            videoUri = streamLink
        }
        return videoUri
    }
}
