package com.commit451.youtubeextractor

import com.squareup.moshi.Moshi
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Class that allows you to extract stream data from a YouTube video
 * given its video id, which is typically contained within the YouTube video url, ie. https://www.youtube.com/watch?v=dQw4w9WgXcQ
 * has a video id of dQw4w9WgXcQ
 */
class YouTubeExtractor private constructor(builder: Builder) {

    companion object {

        private const val BASE_URL = "https://www.youtube.com"

        /**
         * Extract the thumbnails for the video. This will be done if you call
         * [extract] but since it is a lightweight operation, you can do it
         * synchronously if you choose
         */
        fun extractThumbnails(videoId: String): List<Thumbnail> {
            return YouTubeImageHelper.extractAll(videoId)
        }

        /**
         * Extract a thumbnail of a specific quality. See qualities within [Thumbnail]
         */
        fun extractThumbnail(videoId: String, quality: String): Thumbnail {
            return YouTubeImageHelper.extract(videoId, quality)
        }

        /**
         * Create a new YouTubeExtractor with a custom OkHttp client builder
         * @return a new [YouTubeExtractor]
         */
        @Deprecated("Use the Builder class instead", ReplaceWith("YouTubeExtractor.Builder().okHttpClientBuilder(okHttpBuilder).build()"))
        @JvmOverloads
        fun create(okHttpBuilder: OkHttpClient.Builder? = null): YouTubeExtractor {
            return Builder()
                .okHttpClientBuilder(okHttpBuilder)
                .build()
        }
    }

    private var client: OkHttpClient
    private var moshi: Moshi
    private var debug = false

    init {
        this.debug = builder.debug
        val clientBuilder = builder.okHttpClientBuilder ?: OkHttpClient.Builder()
        client = clientBuilder.build()
        moshi = Moshi.Builder()
            .build()
    }

    /**
     * Extract the video information
     * @param videoId the video ID
     * @return the extracted result single
     */
    fun extract(videoId: String): Single<YouTubeExtraction> {
        return Single.defer {
            val url = "$BASE_URL/watch?v=$videoId"
            log("Extracting from URL $url")
            val pageContent = urlToString(url)
            val doc = Jsoup.parse(pageContent)

            val ytPlayerConfigJson = Util.matchGroup1("ytplayer.config\\s*=\\s*(\\{.*?\\});", pageContent)
            val ytPlayerConfig = moshi.adapter<PlayerConfig>(PlayerConfig::class.java).fromJson(ytPlayerConfigJson)!!
            val playerArgs = ytPlayerConfig.args!!
            val playerResponse = moshi.adapter<PlayerResponse>(PlayerResponse::class.java).fromJson(playerArgs.playerResponse!!)!!
            val playerUrl = formatPlayerUrl(ytPlayerConfig)
            val streams = parseStreams(playerResponse, playerUrl)
            val extraction = YouTubeExtraction(
                videoId = videoId,
                title = title(playerArgs, doc),
                streams = streams,
                thumbnails = extractThumbnails(videoId),
                author = author(playerArgs, doc),
                description = descriptionFromHtml(doc),
                viewCount = viewCountFromHtml(doc),
                durationMilliseconds = playerResponse.streamingData?.formats?.find { it.approxDurationMs != null }?.approxDurationMs?.toLong()
            )
            Single.just(extraction)
        }
    }

    private fun title(playerArgs: PlayerArgs, doc: Document): String? {
        return playerArgs.title ?: titleFromHtml(doc)
    }

    private fun titleFromHtml(doc: Document): String? {
        return tryIgnoringException {
            doc.select("meta[name=title]").attr("content")
        }
    }

    private fun author(playerArgs: PlayerArgs, doc: Document): String? {
        return playerArgs.author ?: authorFromHtml(doc)
    }

    private fun authorFromHtml(doc: Document): String? {
        return tryIgnoringException {
            doc.select("div.yt-user-info").first().text();
        }
    }

    private fun descriptionFromHtml(doc: Document): String? {
        return tryIgnoringException {
            doc.select("p[id=\"eow-description\"]").first().html()
        }
    }

    private fun viewCountFromHtml(doc: Document): Long? {
        return tryIgnoringException {
            doc.select("meta[itemprop=interactionCount]").attr("content").toLong()
        }
    }

    private fun <T>tryIgnoringException(block: () -> T): T? {
        try {
            return block.invoke()
        } catch (e: Exception) {
            //we tried our best
            log(e.message)
        }
        return null
    }

    private fun urlToString(url: String): String {
        val request = Request.Builder()
            .url(url)
            .build()

        return client.newCall(request)
            .execute()
            .body()
            ?.string() ?: throw Exception("Unable to connect")
    }

    private fun formatPlayerUrl(playerConfig: PlayerConfig): String {
        var playerUrl = playerConfig.assets?.js!!

        if (playerUrl.startsWith("//")) {
            playerUrl = "https:$playerUrl"
        }
        if (!playerUrl.startsWith(BASE_URL)) {
            playerUrl = BASE_URL + playerUrl
        }
        return playerUrl
    }

    private fun parseStreams(playerResponse: PlayerResponse, playerUrl: String): List<Stream> {
        val itags = parseItags(playerResponse, playerUrl)
        return itags.map {
            when (it.value.type) {
                StreamType.VIDEO -> Stream.VideoStream(it.key, it.value.format, it.value.resolution!!, false)
                StreamType.AUDIO -> Stream.AudioStream(it.key, it.value.format)
                StreamType.VIDEO_ONLY -> Stream.VideoStream(it.key, it.value.format, it.value.resolution!!, true)
            }
        }
    }

    private fun parseItags(playerResponse: PlayerResponse, playerUrl: String): Map<String, ItagItem> {
        val urlAndItags = LinkedHashMap<String, ItagItem>()

        val playerCode = urlToString(playerUrl)
            .replace("\n", "")
        val decryptionCode = try {
            JavaScriptUtil.loadDecryptionCode(playerCode)
        } catch(e: Exception) {
            null
        }

        playerResponse.streamingData?.formats?.forEach {
            parseItag(it, decryptionCode, urlAndItags)
        }
        playerResponse.streamingData?.adaptiveFormats?.forEach {
            parseItag(it, decryptionCode, urlAndItags)
        }
        return urlAndItags
    }

    private fun parseItag(format: PlayerResponse.Format, decryptionCode: String?, map: LinkedHashMap<String, ItagItem>) {
        if (!ItagItem.isSupported(format.itag)) {
            return
        }
        val itag = ItagItem.getItag(format.itag)
        val url = format.url
        val streamUrl = if (url != null) {
            url
        } else if (decryptionCode != null) {
            // encrypted signature
            val cipher: Map<String, String> = Util.compatParseMap(format.cipher!!)
            cipher["url"].toString() + "&" + cipher["sp"] + "=" + JavaScriptUtil.decryptSignature(cipher["s"]!!, decryptionCode)
        } else {
            throw Exception("No url or decryption code!")
        }
        map[streamUrl] = itag
    }

    private fun log(string: String?) {
        if (debug) {
            println(string)
        }
    }

    /**
     * Builds a [YouTubeExtractor] instance
     */
    class Builder {
        internal var debug = false
        internal var okHttpClientBuilder: OkHttpClient.Builder? = null

        /**
         * Forces logging to show for the [YouTubeExtractor]
         */
        fun debug(debug: Boolean): Builder {
            this.debug = debug
            return this
        }

        /**
         * Set a custom [OkHttpClient.Builder] on the [YouTubeExtractor]
         */
        fun okHttpClientBuilder(okHttpClientBuilder: OkHttpClient.Builder?): Builder {
            this.okHttpClientBuilder = okHttpClientBuilder
            return this
        }

        /**
         * Create the configured [YouTubeExtractor]
         */
        fun build(): YouTubeExtractor {
            return YouTubeExtractor(this)
        }
    }
}
