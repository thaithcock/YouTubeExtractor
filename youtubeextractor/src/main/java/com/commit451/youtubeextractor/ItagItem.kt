package com.commit451.youtubeextractor

internal data class ItagItem(
    val id: Int,
    val type: StreamType,
    val format: String,
    val resolution: String? = null
) {

    companion object {

        /**
         * https://github.com/ytdl-org/youtube-dl/blob/master/youtube_dl/extractor/youtube.py#L431
         */
        private val ITAG_MAP = mapOf(

                17 to ItagItem(17, StreamType.VIDEO, Stream.FORMAT_v3GPP, Stream.VideoStream.RESOLUTION_144p),

                18 to ItagItem(18, StreamType.VIDEO, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_360p),
                34 to ItagItem(34, StreamType.VIDEO, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_360p),
                35 to ItagItem(35, StreamType.VIDEO, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_480p),
                36 to ItagItem(36, StreamType.VIDEO, Stream.FORMAT_v3GPP, Stream.VideoStream.RESOLUTION_240p),
                59 to ItagItem(59, StreamType.VIDEO, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_480p),
                78 to ItagItem(78, StreamType.VIDEO, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_480p),
                22 to ItagItem(22, StreamType.VIDEO,  Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_720p),
                37 to ItagItem(37, StreamType.VIDEO, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_1080p),
                38 to ItagItem(38, StreamType.VIDEO, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_1080p),

                43 to ItagItem(43, StreamType.VIDEO, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_360p),
                44 to ItagItem(44, StreamType.VIDEO, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_480p),
                45 to ItagItem(45, StreamType.VIDEO, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_720p),
                46 to ItagItem(46, StreamType.VIDEO, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_1080p),

                171 to ItagItem(171, StreamType.AUDIO, Stream.FORMAT_WEBMA),
                172 to ItagItem(172, StreamType.AUDIO, Stream.FORMAT_WEBMA),
                139 to ItagItem(139, StreamType.AUDIO, Stream.FORMAT_M4A),
                140 to ItagItem(140, StreamType.AUDIO, Stream.FORMAT_M4A),
                141 to ItagItem(141, StreamType.AUDIO, Stream.FORMAT_M4A),
                249 to ItagItem(249, StreamType.AUDIO, Stream.FORMAT_WEBMA_OPUS),
                250 to ItagItem(250, StreamType.AUDIO, Stream.FORMAT_WEBMA_OPUS),
                251 to ItagItem(251, StreamType.AUDIO, Stream.FORMAT_WEBMA_OPUS),

                160 to ItagItem(160, StreamType.VIDEO_ONLY, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_144p),
                133 to ItagItem(133, StreamType.VIDEO_ONLY, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_240p),
                135 to ItagItem(135, StreamType.VIDEO_ONLY, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_480p),
                212 to ItagItem(212, StreamType.VIDEO_ONLY, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_480p),
                298 to ItagItem(298, StreamType.VIDEO_ONLY, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_720p60),
                137 to ItagItem(137, StreamType.VIDEO_ONLY, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_1080p),
                299 to ItagItem(299, StreamType.VIDEO_ONLY, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_1080p60),
                266 to ItagItem(266, StreamType.VIDEO_ONLY, Stream.FORMAT_MPEG_4, Stream.VideoStream.RESOLUTION_2160p),

                278 to ItagItem(278, StreamType.VIDEO_ONLY, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_144p),
                242 to ItagItem(242, StreamType.VIDEO_ONLY, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_240p),
                244 to ItagItem(244, StreamType.VIDEO_ONLY, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_480p),
                245 to ItagItem(245, StreamType.VIDEO_ONLY, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_480p),
                246 to ItagItem(246, StreamType.VIDEO_ONLY, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_480p),
                247 to ItagItem(247, StreamType.VIDEO_ONLY, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_720p),
                248 to ItagItem(248, StreamType.VIDEO_ONLY, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_1080p),
                271 to ItagItem(271, StreamType.VIDEO_ONLY, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_1440p),
                272 to ItagItem(272, StreamType.VIDEO_ONLY, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_2160p),
                302 to ItagItem(302, StreamType.VIDEO_ONLY, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_720p60),
                303 to ItagItem(303, StreamType.VIDEO_ONLY, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_1080p60),
                308 to ItagItem(308, StreamType.VIDEO_ONLY, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_1440p60),
                313 to ItagItem(313, StreamType.VIDEO_ONLY, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_2160p),
                315 to ItagItem(315, StreamType.VIDEO_ONLY, Stream.FORMAT_WEBM, Stream.VideoStream.RESOLUTION_2160p60)
        )


        fun isSupported(itag: Int?): Boolean {
            return ITAG_MAP.containsKey(itag)
        }

        fun getItag(itagId: Int?): ItagItem {
            return ITAG_MAP[itagId] ?: throw YouTubeExtractionException("itag=$itagId not supported")
        }
    }
}
