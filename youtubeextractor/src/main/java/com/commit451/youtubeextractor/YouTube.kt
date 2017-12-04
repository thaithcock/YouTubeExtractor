package com.commit451.youtubeextractor

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

internal interface YouTube {

    @GET("get_video_info?el=info&ps=default&gl=US")
    fun extract(@Query("video_id") videoId: String): Single<YouTubeExtraction>

}
