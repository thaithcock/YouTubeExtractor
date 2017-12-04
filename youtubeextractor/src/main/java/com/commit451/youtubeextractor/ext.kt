package com.commit451.youtubeextractor

/**
 * Get the best available quality video, starting with 1080p all the way down to 240p.
 * @return the best quality video uri, or null if no uri is available
 */
fun YouTubeExtraction.bestAvailableQualityVideoUrl(): String? {
    var url = hd1080VideoUrl
    if (url != null) {
        return url
    }
    url = hd720VideoUrl
    if (url != null) {
        return url
    }
    url = sd360VideoUrl
    if (url != null) {
        return url
    }
    url = sd240VideoUrl
    return if (url != null) {
        url
    } else null
}

/**
 * Convenience method which will go through all thumbnail urls and return you the best one
 * @return the best image uri, or null if none exist
 */
fun YouTubeExtraction.bestAvailableQualityThumbUri(): String? {
    var uri = highThumbUrl
    if (uri != null) {
        return uri
    }
    uri = mediumThumbUrl
    if (uri != null) {
        return uri
    }
    uri = defaultThumbUrl
    if (uri != null) {
        return uri
    }
    uri = standardThumbUrl
    return if (uri != null) {
        uri
    } else null
}