package com.commit451.youtubeextractor

import com.squareup.moshi.Json

class PlayerConfig {

    @Json(name = "args")
    var args: PlayerArgs? = null
    @Json(name = "assets")
    var assets: Assets? = null

    class Assets {

        @Json(name = "js")
        var js: String? = null

    }
}