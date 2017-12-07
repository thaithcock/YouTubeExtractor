package com.commit451.youtubeextractor

import org.junit.Assert
import org.junit.Test

class ExtractionTest {

    companion object {
        private val GRID_YOUTUBE_ID = "orxXlAltFQg"
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
