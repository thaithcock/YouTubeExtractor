package com.commit451.youtubeextractor

import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * Class that allows you to extract desired data from a YouTube video, such as streamable URLs
 * given its video id, which is typically contained within the YouTube video url, ie. https://www.youtube.com/watch?v=dQw4w9WgXcQ
 * has a video id of dQw4w9WgXcQ
 */
class YouTubeExtractor private constructor(okBuilder: OkHttpClient.Builder?) {

    companion object {

        private val BASE_URL = "https://www.youtube.com/"

        /**
         * Create a new YouTubeExtractor with a custom OkHttp client builder
         * @return a new [YouTubeExtractor]
         */
        @JvmOverloads
        fun create(okHttpBuilder: OkHttpClient.Builder? = null): YouTubeExtractor {
            return YouTubeExtractor(okHttpBuilder)
        }
    }

    private val youTube: YouTube
    private val interceptor: LanguageInterceptor

    init {
        val clientBuilder = okBuilder ?: OkHttpClient.Builder()

        interceptor = LanguageInterceptor()
        clientBuilder.addInterceptor(interceptor)

        val retrofitBuilder = Retrofit.Builder()

        retrofitBuilder
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(YouTubeExtractionConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        youTube = retrofitBuilder.build().create(YouTube::class.java)
    }

    /**
     * Extract the video information
     * @param videoId the video ID
     * @return the extracted result
     */
    fun extract(videoId: String): Single<YouTubeExtraction> {
        return youTube.extract(videoId)
    }

    /**
     * Set the language. Defaults to [java.util.Locale.getDefault]
     * @param language the language
     */
    fun setLanguage(language: String) {
        interceptor.language = language
    }
}
