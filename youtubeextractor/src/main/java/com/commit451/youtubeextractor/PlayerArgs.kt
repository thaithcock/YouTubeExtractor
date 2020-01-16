package com.commit451.youtubeextractor

import com.squareup.moshi.Json

internal data class PlayerArgs(
    @Json(name = "title")
    var title: String? = null,
    @Json(name = "author")
    var author: String? = null,
    @Json(name = "view_count")
    var viewCount: String? = null,
    @Json(name = "length_seconds")
    var lengthSeconds: String? = null,
    @Json(name = "player_response")
    var playerResponse: String? = null
)
