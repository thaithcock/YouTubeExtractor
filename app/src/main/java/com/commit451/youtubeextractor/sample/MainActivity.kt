package com.commit451.youtubeextractor.sample

import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.commit451.youtubeextractor.YouTubeExtraction
import com.commit451.youtubeextractor.YouTubeExtractor
import com.devbrackets.android.exomedia.ui.widget.VideoView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {

        private const val GRID_YOUTUBE_ID = "9d8wWcJLnFI"

        private const val KEY_SAVED_POSITION = "saved_position"
    }

    private lateinit var videoView: VideoView
    private lateinit var imageView: ImageView
    private lateinit var description: TextView
    private lateinit var title: TextView
    private lateinit var duration: TextView
    private lateinit var views: TextView

    private var savedPosition: Int = 0

    private val extractor = YouTubeExtractor.Builder()
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.thumb)
        videoView = findViewById(R.id.video_view)
        title = findViewById(R.id.title)
        description = findViewById(R.id.description)
        duration = findViewById(R.id.duration)
        views = findViewById(R.id.views)

        extractor.extract(GRID_YOUTUBE_ID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ extraction ->
                bindVideoResult(extraction)
            }, { t ->
                onError(t)
            })
        if (savedInstanceState != null) {
            savedPosition = savedInstanceState.getInt(KEY_SAVED_POSITION, 0)
        }
        videoView.setOnPreparedListener {
            videoView.volume = 0f
            videoView.seekTo(savedPosition.toLong())
            savedPosition = 0
            videoView.start()
        }
        videoView.setOnErrorListener { e ->
            e.printStackTrace()
            false
        }
    }

    override fun onResume() {
        super.onResume()
        videoView.seekTo(savedPosition.toLong())
        videoView.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SAVED_POSITION, savedPosition)
    }

    private fun onError(t: Throwable) {
        t.printStackTrace()
        Toast.makeText(this, "It failed to extract. So sad", Toast.LENGTH_SHORT)
            .show()
    }

    private fun bindVideoResult(result: YouTubeExtraction) {
        val videoUrl = result.videoStreams.first().url
        Log.d("OnSuccess", "Got a result with the best url: $videoUrl")
        title.text = result.title
        val descriptionText = Html.fromHtml(result.description).toString()
        description.text = descriptionText.substring(0, descriptionText.length.coerceAtMost(50)) + "..."
        duration.text = "Duration: ${TimeUnit.MILLISECONDS.toMinutes(result.durationMilliseconds!!)} minutes"
        views.text = "Views: ${result.viewCount}"
        Glide.with(this)
            .load(result.thumbnails.first().url)
            .into(imageView)
        videoView.setVideoURI(Uri.parse(videoUrl))
    }
}
