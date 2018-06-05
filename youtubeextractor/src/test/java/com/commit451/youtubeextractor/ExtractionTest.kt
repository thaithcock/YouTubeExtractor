package com.commit451.youtubeextractor

import org.junit.Assert
import org.junit.Test

class ExtractionTest {

    companion object {
        private const val YOUTUBE_ID = "UEuOpxOrA_0"
    }

    @Test
    fun testExtraction() {
        val extractor = YouTubeExtractor.create()
        val result = extractor.extract(YOUTUBE_ID)
                .blockingGet()
        Assert.assertNotNull(result)
        Assert.assertTrue(result.videoStreams.isNotEmpty())
    }
}
