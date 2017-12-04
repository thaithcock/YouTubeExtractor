package com.commit451.youtubeextractor

import java.lang.reflect.Type

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

/**
 * Extracts the stuff
 */
internal class YouTubeExtractionConverterFactory : Converter.Factory() {

    companion object {

        fun create(): YouTubeExtractionConverterFactory {
            return YouTubeExtractionConverterFactory()
        }
    }

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        // This is good, we only register if the call includes this type, so that we could potentially
        // still be okay with having additional converter factories if we needed to
        return if (type == YouTubeExtraction::class.java) {
            YouTubeBodyConverter()
        } else null
        // Allow others to give it a go
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        throw IllegalStateException("Not supported")
    }
}
