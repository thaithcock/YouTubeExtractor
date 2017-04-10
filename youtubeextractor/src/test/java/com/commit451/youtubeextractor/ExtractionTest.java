package com.commit451.youtubeextractor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class ExtractionTest {

    private static final String GRID_YOUTUBE_ID = "9d8wWcJLnFI";

    @Test
    public void testExtraction() throws Exception {
        YouTubeExtractor extractor = YouTubeExtractor.create();
        YouTubeExtractionResult result = extractor.extract(GRID_YOUTUBE_ID)
                .blockingGet();
        Assert.assertNotNull(result);
    }
}
