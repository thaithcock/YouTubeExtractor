package com.commit451.youtubeextractor

import com.squareup.moshi.Json

class PlayerArgs {

    @Json(name = "url_encoded_fmt_stream_map")
    var urlEncodedFmtStreamMap: String? = null
    @Json(name = "title")
    var title: String? = null
}