package com.commit451.youtubeextractor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class InterceptorTest {

    @Test
    public void testLanguage() throws Exception {
        LanguageInterceptor interceptor = new LanguageInterceptor();
        assertEquals(Locale.getDefault().getLanguage(), interceptor.language);
        String language = "test language";
        interceptor.setLanguage(language);
        assertEquals(language, interceptor.language);
    }

    @Test
    public void testHeader() throws IOException {
        LanguageInterceptor interceptor = new LanguageInterceptor();
        RealChain requestFacade = new RealChain();
        interceptor.intercept(requestFacade);
        Headers headers = requestFacade.mHeaders;
        assertEquals(1, headers.size());
        assertEquals(Locale.getDefault().getLanguage(), headers.get(LanguageInterceptor.ACCEPT_LANGUAGE_HEADER));
    }

    @Test
    public void testQueryParam() throws IOException {
        LanguageInterceptor interceptor = new LanguageInterceptor();
        RealChain requestFacade = new RealChain();
        HttpUrl url = interceptor.intercept(requestFacade).request().url();
        assertEquals(Locale.getDefault().getLanguage(), url.queryParameter(LanguageInterceptor.LANGUAGE_QUERY_PARAM));
    }

    static class RealChain implements Interceptor.Chain {

        Headers mHeaders;

        @Override
        public Request request() {
            return new Request.Builder().url("http://test.url/").build();
        }

        @Override
        public Response proceed(Request request) throws IOException {
            mHeaders = request.headers();
            return new Response.Builder().request(request).protocol(Protocol.SPDY_3).code(200).build();
        }

        @Override
        public Connection connection() {
            return mock(Connection.class);
        }
    }
}
