package com.commit451.youtubeextractor


/**
 * A stream, either video or audio
 */
sealed class Stream {

    /**
     * A video stream, which may or may not contain audio
     */
    data class VideoStream(
        val url: String,
        val format: String,
        val resolution: String,
        val isVideoOnly: Boolean
    ): Stream(), Comparable<VideoStream> {

        override fun compareTo(other: VideoStream): Int {
            if (other.resolution == this.resolution) {
                return 0
            }
            val ourResolutionSplit = resolution.split("p")
            val ourResolution = ourResolutionSplit.first()
            val ourFramerate = ourResolutionSplit[1].toIntOrNull() ?: 0
            val theirResolutionSplit = other.resolution.split("p")
            val theirResolution = theirResolutionSplit.first()
            val theirFramerate = theirResolutionSplit[1].toIntOrNull() ?: 0
            return if (ourResolution == theirResolution) {
                ourFramerate.compareTo(theirFramerate)
            } else {
                theirResolution.compareTo(ourResolution)
            }
        }

        companion object{
            const val RESOLUTION_144p = "144p"
            const val RESOLUTION_240p = "240p"
            const val RESOLUTION_360p = "360p"
            const val RESOLUTION_480p = "480p"
            const val RESOLUTION_720p = "720p"
            const val RESOLUTION_720p60 = "720p60"
            const val RESOLUTION_1080p = "1080p"
            const val RESOLUTION_1080p60 = "1080p60"
            const val RESOLUTION_1440p = "1440p"
            const val RESOLUTION_1440p60 = "1440p60"
            const val RESOLUTION_2160p = "2160p"
            const val RESOLUTION_2160p60 = "2160p60"
        }
    }

    /**
     * An audio only stream
     */
    data class AudioStream(
        val url: String,
        val format: String
    ): Stream()

    companion object {
        const val FORMAT_v3GPP = "v3GPP"
        const val FORMAT_MPEG_4 = "MPEG4"
        const val FORMAT_WEBM = "WEBM"

        const val FORMAT_WEBMA = "WEBMA"
        const val FORMAT_M4A = "M4A"
        const val FORMAT_WEBMA_OPUS = "WEBMA_OPUS"
    }
}
