package com.commit451.youtubeextractor


/**
 * A stream
 */
class VideoStream(val url: String, val format: String, val resolution: String) {

    companion object {
        const val STREAM_TYPE_v3GPP = "v3GPP"
        const val STREAM_TYPE_MPEG_4 = "MPEG4"
        const val STREAM_TYPE_WEBM = "WEBM"

        const val STREAM_RESOLUTION_144p = "144p"
        const val STREAM_RESOLUTION_240p = "240p"
        const val STREAM_RESOLUTION_360p = "360p"
        const val STREAM_RESOLUTION_480p = "480p"
        const val STREAM_RESOLUTION_720p = "720p"
        const val STREAM_RESOLUTION_720p60 = "720p60"
        const val STREAM_RESOLUTION_1080p = "1080p"
        const val STREAM_RESOLUTION_1080p60 = "1080p60"
        const val STREAM_RESOLUTION_1440p = "1440p"
        const val STREAM_RESOLUTION_1440p60 = "1440p60"
        const val STREAM_RESOLUTION_2160p = "2160p"
        const val STREAM_RESOLUTION_2160p60 = "2160p60"
    }
}