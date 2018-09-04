package com.zachary.lynch.bakingapp.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
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
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Steps;
import java.util.ArrayList;

import butterknife.ButterKnife;

// guess I'll have to work on this for a bit tomorrow. again.
public class VideoFragment extends Fragment {

    private ArrayList<Steps> mSteps;
    private int mIndex;
    private boolean mOrientation = false;
    private Bundle mBundle;
    private long playerPosition;
    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer exoPlayer;
    private String mThumbUrl;
    private ImageView mThumbnail;
    private String mVideoUrl;
    private boolean playstate = false;
    private boolean isFullScreen = false;


    public interface videoFullScreen {
        void onRotation(Bundle bundle, boolean orientation);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBundle = getArguments();
        if (mBundle == null){
            assert savedInstanceState != null;
            savedInstanceState.getBundle("bundle");
        }
        if (mBundle != null){
        mIndex = mBundle.getInt("index");
        mSteps = mBundle.getParcelableArrayList(MainActivity.STEPS);}
        if (savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong("seek");
            mOrientation = savedInstanceState.getBoolean("ori");
        }


        View view = inflater.inflate(R.layout.video_fragment, container, false);
        checkFullScreen();

        mPlayerView = view.findViewById(R.id.videoPlayer);
        mThumbnail = view.findViewById(R.id.thumbnail);
        mOrientation = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        mThumbUrl = mSteps.get(mIndex).getThumbnail();
        mVideoUrl = mSteps.get(mIndex).getVideoURL();


        if (!mVideoUrl.equals("")) {
           // initializePlayer(Uri.parse(mVideoUrl));
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        } else {
            assert mPlayerView != null;
            if (!"".equals(mThumbUrl)) {
                mPlayerView.setVisibility(View.GONE);
                mThumbnail.setVisibility(View.VISIBLE);
                Picasso
                        .get()
                        .load(mThumbUrl)
                        .into(mThumbnail);
            } else {
                mPlayerView.setVisibility(View.GONE);
                mThumbnail.setVisibility(View.VISIBLE);
                mThumbnail.setImageResource(R.drawable.exo_edit_mode_logo);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }

        /*if (!"".equals(mVideoUrl)) {
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
        }*/
        }
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


        boolean resumePosition = playerPosition != C.INDEX_UNSET;

        if (resumePosition) {
            mPlayerView.getPlayer().seekTo(playerPosition);
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
        if (mOrientation) {
            isFullScreen = true;
            mPlayerView.setVisibility(View.VISIBLE);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            playstate = true;
            ViewGroup.LayoutParams params = mPlayerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }else {
            isFullScreen = false;
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        player.prepare(videoSource, !resumePosition, false);

        player.setPlayWhenReady(playstate);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        mOrientation = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        outState.putLong("seek", playerPosition);
        outState.putBoolean("ori", mOrientation);
        outState.putBoolean("fullScreen", isFullScreen);
        outState.putInt("index", mIndex);
        outState.putBundle("bundle", mBundle);
        outState.putParcelableArrayList("steps", mSteps);
        super.onSaveInstanceState(outState);
    }

    private void test() {

    }

    @Override
    public void onPause() {
        super.onPause();

        if (mPlayerView != null && mPlayerView.getPlayer() != null) {
            playerPosition = mPlayerView.getPlayer().getCurrentPosition();
            mPlayerView.getPlayer().stop();
            mPlayerView.getPlayer().release();
            mPlayerView = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPlayerView != null){
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(mPlayerView);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if(exoPlayer != null) {
            playerPosition = exoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    /**
     * Method to initialize media player
     *
     * @param mediaUri Uri of media
     */
    private void initializePlayer(Uri mediaUri){
        if(exoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),trackSelector,loadControl);
            mPlayerView.setPlayer(exoPlayer);
            String userAgent = Util.getUserAgent(getContext(),"BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(),userAgent), new DefaultExtractorsFactory(),null,null);
            exoPlayer.prepare(mediaSource);
            if (playerPosition != 0 && !playstate){
                exoPlayer.seekTo(playerPosition);
            } else {
                exoPlayer.seekTo(playerPosition);
            }
        }
    }

    /**
     * Method to release exoPlayer
     */
    private void releasePlayer(){
        if(exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
    public void checkFullScreen(){
        Configuration newConfig = new Configuration();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isFullScreen = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            isFullScreen = false;
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