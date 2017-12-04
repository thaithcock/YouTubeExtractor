package com.commit451.youtubeextractor

import java.io.IOException
import java.util.Locale

import okhttp3.Interceptor
import okhttp3.Response

internal class LanguageInterceptor : Interceptor {

    companion object {

        val ACCEPT_LANGUAGE_HEADER = "Accept-Language"
        val LANGUAGE_QUERY_PARAM = "language"
    }

    var language: String = Locale.getDefault().language

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val url = request.url()
                .newBuilder()
                .addQueryParameter(LANGUAGE_QUERY_PARAM, language)
                .build()

        val requestWithHeaders = request.newBuilder()
                .addHeader(ACCEPT_LANGUAGE_HEADER, language)
                .url(url)
                .build()
        return chain.proceed(requestWithHeaders)
    }
}
