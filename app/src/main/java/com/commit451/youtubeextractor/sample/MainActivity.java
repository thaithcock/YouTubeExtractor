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
import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final String GRID_YOUTUBE_ID = "9d8wWcJLnFI";

    private static final String STATE_SAVED_POSITION = "saved_position";

    private ImageView imageView;
    private TextView description;
    private EMVideoView videoView;

    private int savedPosition;

    private Callback<YouTubeExtractionResult> mExtractionCallback = new Callback<YouTubeExtractionResult>() {
        @Override
        public void onResponse(Call<YouTubeExtractionResult> call, Response<YouTubeExtractionResult> response) {
            bindVideoResult(response.body());
        }

        @Override
        public void onFailure(Call<YouTubeExtractionResult> call, Throwable t) {
            onError(t);
        }
    };

    private OnPreparedListener mOnPreparedListener = new OnPreparedListener() {
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
    };
    private OnCompletionListener mOnCompletionListener = new OnCompletionListener() {
        @Override
        public void onCompletion() {
            //I dunno, maybe play it again
        }
    };
    private OnErrorListener mOnErrorListener = new OnErrorListener() {
        @Override
        public boolean onError() {
            //cry I guess
            Log.d("ERROR", "There was an error. Oh no!");
            return true;
        }
    };

    private final YouTubeExtractor extractor = YouTubeExtractor.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.thumb);
        videoView = (EMVideoView) findViewById(R.id.video_view);
        description = (TextView) findViewById(R.id.description);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.setScaleType(ScaleType.CENTER_CROP);
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
        videoView.setScaleType(ScaleType.CENTER_CROP);
        videoView.setOnPreparedListener(mOnPreparedListener);
        videoView.setOnCompletionListener(mOnCompletionListener);
        videoView.setOnErrorListener(mOnErrorListener);
        videoView.setVideoURI(result.getBestAvailableQualityVideoUri());
    }
}
