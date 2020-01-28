package com.commit451.youtubeextractor

import org.junit.Assert
import org.junit.Test

class SortingTest {

    @Test
    fun `sort video streams`() {
        val videoStreams = mutableListOf(
            createFakeStream(Stream.VideoStream.RESOLUTION_1080p),
            createFakeStream(Stream.VideoStream.RESOLUTION_1080p60),
            createFakeStream(Stream.VideoStream.RESOLUTION_144p)
        )

        videoStreams.sort()
        Assert.assertEquals(Stream.VideoStream.RESOLUTION_144p, videoStreams.first().resolution)
        Assert.assertEquals(Stream.VideoStream.RESOLUTION_1080p60, videoStreams.last().resolution)
    }

    private fun createFakeStream(resolution: String): Stream.VideoStream {
        return Stream.VideoStream("", "", resolution, true)
    }
}
