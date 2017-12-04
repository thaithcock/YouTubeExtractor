package com.commit451.youtubeextractor

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
class ExtractionTest {

    companion object {
        private val GRID_YOUTUBE_ID = "9d8wWcJLnFI"
    }

    @Test
    fun testExtraction() {
        val extractor = YouTubeExtractor.create()
        val result = extractor.extract(GRID_YOUTUBE_ID)
                .blockingGet()
        Assert.assertNotNull(result)
        Assert.assertNotNull(result.bestAvailableQualityVideoUrl())
    }
}
