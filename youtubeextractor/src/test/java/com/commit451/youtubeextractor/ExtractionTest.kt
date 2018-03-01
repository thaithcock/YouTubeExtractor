package com.commit451.youtubeextractor

import org.junit.Assert
import org.junit.Test

class ExtractionTest {

    companion object {
        private const val GRID_YOUTUBE_ID = "UAz1Apj2kqs"
    }

    @Test
    fun testExtraction() {
        val extractor = YouTubeExtractor.create()
        val result = extractor.extract(GRID_YOUTUBE_ID)
                .blockingGet()
        Assert.assertNotNull(result)
        Assert.assertTrue(result.videoStreams.isNotEmpty())
    }
}
