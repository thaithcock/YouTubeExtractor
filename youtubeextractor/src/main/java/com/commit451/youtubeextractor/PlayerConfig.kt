package com.commit451.youtubeextractor

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class PlayerConfig(
    @Json(name = "args")
    var args: PlayerArgs? = null,
    @Json(name = "assets")
    var assets: Assets? = null
) {
    @JsonClass(generateAdapter = true)
    data class Assets(
        @Json(name = "js")
        var js: String? = null
    )
}
