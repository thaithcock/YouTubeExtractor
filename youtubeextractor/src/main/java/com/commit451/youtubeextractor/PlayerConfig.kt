package com.commit451.youtubeextractor

import com.squareup.moshi.Json

internal data class PlayerConfig(
    @Json(name = "args")
    var args: PlayerArgs? = null,
    @Json(name = "assets")
    var assets: Assets? = null
) {
    data class Assets(
        @Json(name = "js")
        var js: String? = null
    )
}
