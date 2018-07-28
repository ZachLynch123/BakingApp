package com.zachary.lynch.bakingapp.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Steps;
import java.util.ArrayList;


public class VideoFragment extends Fragment {

    private ArrayList<Steps> mSteps;
    private int mIndex;
    private boolean mOrientation = false;
    private Bundle mBundle;
    private int startWindow;
    private long startPosition;
    private SimpleExoPlayerView mPlayerView;
    private String mThumbUrl;
    private ImageView mThumbnail;
    private String mVideoUrl;


    public interface videoFullScreen {
        void onRotation(Bundle bundle, boolean orientation);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mBundle = getArguments();
        if (mBundle != null) {
            mIndex = mBundle.getInt("index");
            mSteps = mBundle.getParcelableArrayList(MainActivity.STEPS);
        }
        mBundle = null;

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            startWindow = savedInstanceState.getInt("resume");
            startPosition = savedInstanceState.getLong("seek");
            mOrientation = savedInstanceState.getBoolean("ori");
            mSteps = savedInstanceState.getParcelableArrayList("steps");
            mIndex =savedInstanceState.getInt("index");
            mBundle = null;
        }



        View view = inflater.inflate(R.layout.video_fragment, container, false);

        mPlayerView = view.findViewById(R.id.videoPlayer);
        mThumbnail = view.findViewById(R.id.thumbnail);
        mOrientation = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        mVideoUrl = mSteps.get(mIndex).getVideoURL();
        mThumbUrl = mSteps.get(mIndex).getThumbnail();


        if (!"".equals(mVideoUrl)) {
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(mPlayerView);
            mThumbnail.setVisibility(View.INVISIBLE);
        } else if (!"".equals(mThumbUrl)) {
            mPlayerView.setVisibility(View.INVISIBLE);
            mThumbnail.setVisibility(View.VISIBLE);
            Picasso
                    .get()
                    .load(mThumbUrl)
                    .into(mThumbnail);
        } else {
            mPlayerView.setVisibility(View.INVISIBLE);
            mThumbnail.setVisibility(View.VISIBLE);
            mThumbnail.setImageResource(R.drawable.exo_edit_mode_logo);
        }
        mBundle = null;
        return view;
    }


    private void initializePlayer(PlayerView mPlayerView) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, new DefaultLoadControl());

        mPlayerView.setUseController(true);
        mPlayerView.requestFocus();

        mPlayerView.setPlayer(player);

        boolean resumePosition = startPosition != C.INDEX_UNSET;

        if (resumePosition) {
            mPlayerView.getPlayer().seekTo(startPosition);
        }

        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                getContext(),
                Util.getUserAgent(getContext(), "BakingApp"),
                defaultBandwidthMeter);

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource videoSource = new ExtractorMediaSource(
                Uri.parse(mSteps.get(mIndex).getVideoURL()),
                dataSourceFactory,
                extractorsFactory,
                null,
                null);

        player.prepare(videoSource, !resumePosition, false);

        player.setPlayWhenReady(true);


        if (mOrientation) {
            ViewGroup.LayoutParams params = mPlayerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        mBundle = null;
        outState.putLong("seek", startPosition);
        outState.putBoolean("ori", mOrientation);
        outState.putInt("index", mIndex);
        outState.putParcelableArrayList("steps", mSteps);
        super.onSaveInstanceState(outState);
    }

    private void test() {

    }

    @Override
    public void onPause() {
        super.onPause();

        if (mPlayerView != null && mPlayerView.getPlayer() != null) {
            startPosition = mPlayerView.getPlayer().getCurrentPosition();
            mOrientation = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            mPlayerView.getPlayer().stop();
            mPlayerView.getPlayer().release();
            mPlayerView = null;
            mBundle = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPlayerView == null){
            initializePlayer(mPlayerView);
        }
    }
}

/*
 @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }


    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }


    private void openFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void closeFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_expand));
    }


    private void initFullscreenButton() {

        PlaybackControlView controlView = mExoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }


    private void initExoPlayer() {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this), trackSelector, loadControl);
        mExoPlayerView.setPlayer(player);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            mExoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }

        mExoPlayerView.getPlayer().prepare(mVideoSource);
        mExoPlayerView.getPlayer().setPlayWhenReady(true);
    }


    @Override
    protected void onResume() {

        super.onResume();

        if (mExoPlayerView == null) {

            mExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoplayer);
            initFullscreenDialog();
            initFullscreenButton();

            String streamUrl = "https://mnmedias.api.telequebec.tv/m3u8/29880.m3u8";
            String userAgent = Util.getUserAgent(MainActivity.this, getApplicationContext().getApplicationInfo().packageName);
            DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(MainActivity.this, null, httpDataSourceFactory);
            Uri daUri = Uri.parse(streamUrl);

            mVideoSource = new HlsMediaSource(daUri, dataSourceFactory, 1, null, null);
        }

        initExoPlayer();

        if (mExoPlayerFullscreen) {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
            mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
            mFullScreenDialog.show();
        }
    }


    @Override
    protected void onPause() {

        super.onPause();

        if (mExoPlayerView != null && mExoPlayerView.getPlayer() != null) {
            mResumeWindow = mExoPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, mExoPlayerView.getPlayer().getContentPosition());

            mExoPlayerView.getPlayer().release();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }

}

 */

