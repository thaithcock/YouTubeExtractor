package com.commit451.youtubeextractor

/**
 * The result of [YouTubeExtractor.extract]
 */
data class YouTubeExtraction(
    val videoId: String,
    val title: String?,
    val streams: List<Stream>,
    val thumbnails: List<Thumbnail>,
    val author: String?,
    val description: String?,
    val viewCount: Long?,
    val durationMilliseconds: Long?
)
