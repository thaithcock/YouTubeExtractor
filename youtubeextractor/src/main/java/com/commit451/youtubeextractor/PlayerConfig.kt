package com.commit451.youtubeextractor

import com.squareup.moshi.Json

internal class PlayerConfig {

    @Json(name = "args")
    var args: PlayerArgs? = null
    @Json(name = "assets")
    var assets: Assets? = null

    class Assets {

        @Json(name = "js")
        var js: String? = null

    }
}