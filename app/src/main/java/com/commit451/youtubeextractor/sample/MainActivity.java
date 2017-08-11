package com.commit451.youtubeextractor.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.commit451.youtubeextractor.YouTubeExtractionResult;
import com.commit451.youtubeextractor.YouTubeExtractor;
import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String GRID_YOUTUBE_ID = "9d8wWcJLnFI";

    private static final String STATE_SAVED_POSITION = "saved_position";

    private ImageView imageView;
    private TextView description;
    private VideoView videoView;

    private int savedPosition;

    private final YouTubeExtractor extractor = YouTubeExtractor.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.thumb);
        videoView = findViewById(R.id.video_view);
        description = findViewById(R.id.description);

        extractor.extract(GRID_YOUTUBE_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<YouTubeExtractionResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(YouTubeExtractionResult value) {
                        bindVideoResult(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        MainActivity.this.onError(e);
                    }
                });
        if (savedInstanceState != null) {
            savedPosition = savedInstanceState.getInt(STATE_SAVED_POSITION, 0);
        }
        videoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                if (videoView == null) {
                    return;
                }

                videoView.setVolume(0);
                videoView.seekTo(savedPosition);
                savedPosition = 0;
                videoView.start();
            }
        });
        videoView.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.seekTo(savedPosition);
        videoView.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SAVED_POSITION, savedPosition);
    }

    private void onError(Throwable t) {
        t.printStackTrace();
        Toast.makeText(MainActivity.this, "It failed to extract. So sad", Toast.LENGTH_SHORT).show();
    }

    private void bindVideoResult(YouTubeExtractionResult result) {
        Log.d("OnSuccess", "Got a result with the best url: " + result.getBestAvailableQualityVideoUri());
        Glide.with(this)
                .load(result.getBestAvailableQualityThumbUri())
                .into(imageView);
        videoView.setVideoURI(result.getBestAvailableQualityVideoUri());
    }
}
