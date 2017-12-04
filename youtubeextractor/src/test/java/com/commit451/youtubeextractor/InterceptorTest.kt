package com.commit451.youtubeextractor

import org.junit.Test

import java.io.IOException
import java.util.Locale

import okhttp3.Connection
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response

import org.junit.Assert.assertEquals
import org.mockito.Mockito.mock

class InterceptorTest {

    @Test
    @Throws(Exception::class)
    fun testLanguage() {
        val interceptor = LanguageInterceptor()
        assertEquals(Locale.getDefault().language, interceptor.language)
        val language = "test language"
        interceptor.language = language
        assertEquals(language, interceptor.language)
    }

    @Test
    @Throws(IOException::class)
    fun testHeader() {
        val interceptor = LanguageInterceptor()
        val requestFacade = RealChain()
        interceptor.intercept(requestFacade)
        val headers = requestFacade.headers
        assertEquals(1, headers?.size()?.toLong())
        assertEquals(Locale.getDefault().language, headers?.get(LanguageInterceptor.ACCEPT_LANGUAGE_HEADER))
    }

    @Test
    @Throws(IOException::class)
    fun testQueryParam() {
        val interceptor = LanguageInterceptor()
        val requestFacade = RealChain()
        val url = interceptor.intercept(requestFacade).request().url()
        assertEquals(Locale.getDefault().language, url.queryParameter(LanguageInterceptor.LANGUAGE_QUERY_PARAM))
    }

    internal class RealChain : Interceptor.Chain {

        var headers: Headers? = null

        override fun request(): Request {
            return Request.Builder().url("http://test.url/").build()
        }

        @Throws(IOException::class)
        override fun proceed(request: Request): Response {
            headers = request.headers()
            return Response.Builder().request(request).protocol(Protocol.SPDY_3).code(200).build()
        }

        override fun connection(): Connection? {
            return mock(Connection::class.java)
        }
    }
}
