package com.zachary.lynch.bakingapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
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
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.adapters.DetailsAdapter;
import com.zachary.lynch.bakingapp.model.Steps;

import java.util.ArrayList;


public class DetailsFragment extends Fragment {

    private ArrayList<Steps> mSteps;
    private int mIndex;
    private Button mBackButton;
    private Button mNextButton;
    private Bundle saveBundle = null;
    private Bundle mBundle;
    private Boolean mOrientation = false;
    private DetailsListener mDetailsListener;
    private SimpleExoPlayerView mSimpleExoPlayer;
    private SimpleExoPlayer player;
    private String videoUrl;
    private BandwidthMeter bandwidthMeter;
    private Handler mainHandler;
    private int playerPosition = 0;

    public interface DetailsListener {
        void buttonClicked(int index, ArrayList<Steps> steps);
        void onRotation(Bundle bundle);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bandwidthMeter = new DefaultBandwidthMeter();
        mainHandler = new Handler();

        test();
        mBundle = getArguments();
        assert mBundle != null;
        mIndex = mBundle.getInt("index");
        mSteps = mBundle.getParcelableArrayList(MainActivity.STEPS);
        if (savedInstanceState != null){
            mIndex = savedInstanceState.getInt("index");
            mSteps = savedInstanceState.getParcelableArrayList("steps");
            onViewStateRestored(savedInstanceState);
        }


        final View view = inflater.inflate(R.layout.detail_fragment, container, false);
        mBackButton = view.findViewById(R.id.backButton);
        mNextButton = view.findViewById(R.id.nextButton);
        mSimpleExoPlayer = view.findViewById(R.id.use_this_playerView);
        mSimpleExoPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        mOrientation = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        //mDetailsListener.onRotation(mBundle, mOrientation);
        videoUrl = mSteps.get(mIndex).getVideoURL();
        initializePlayer(Uri.parse(videoUrl));





        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIndex == 0){
                    mBackButton.setVisibility(View.INVISIBLE);
                    checkButtonStatus();
                } else{
                    mBackButton.setVisibility(View.VISIBLE);
                    mIndex = mIndex - 1;
                    videoUrl = mSteps.get(mIndex).getVideoURL();
                    if (!videoUrl.isEmpty()){
                        initializePlayer(Uri.parse(videoUrl));
                    }
                    checkButtonStatus();
                    mDetailsListener.buttonClicked(mIndex, mSteps);
                    updateUi(view);
                }
            }
        });
        checkButtonStatus();

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIndex == mSteps.size()){
                    mNextButton.setVisibility(View.INVISIBLE);
                    checkButtonStatus();
                } else {
                    mNextButton.setVisibility(View.VISIBLE);
                    mIndex = mIndex + 1;
                    videoUrl = mSteps.get(mIndex).getVideoURL();
                    if (!videoUrl.isEmpty()){
                        initializePlayer(Uri.parse(videoUrl));
                    }
                    mDetailsListener.buttonClicked(mIndex, mSteps);
                    checkButtonStatus();
                    updateUi(view);
                }
            }
        });
        updateUi(view);
        return view;
    }

    private void initializePlayer(Uri videoUri) {
        if (player == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mSimpleExoPlayer.setPlayer(player);
            boolean resumePosition = playerPosition != C.INDEX_UNSET;

            if (resumePosition) {
                mSimpleExoPlayer.getPlayer().seekTo(playerPosition);
            }

            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource, !resumePosition, false);
            player.setPlayWhenReady(true);
        }
    }

    private void checkButtonStatus() {
        if (mIndex != 0 ) {
            mBackButton.setVisibility(View.VISIBLE);
        } else {
            mBackButton.setVisibility(View.INVISIBLE);
        }
        if (mIndex == mSteps.size() -1){
            mNextButton.setVisibility(View.INVISIBLE);
        } else {
            mNextButton.setVisibility(View.VISIBLE);
        }

    }

    private void updateUi(View view) {

        view.setVisibility(View.VISIBLE);
        videoUrl = mSteps.get(mIndex).getVideoURL();
        if (!videoUrl.isEmpty()){
            initializePlayer(Uri.parse(videoUrl));
        }

        RecyclerView recyclerView = view.findViewById(R.id.detailsRecyclerView);
        DetailsAdapter adapter = new DetailsAdapter(getContext(), mIndex, mSteps, mOrientation);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        if (mOrientation){
            ViewGroup.LayoutParams params = mSimpleExoPlayer.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            recyclerView.setVisibility(View.INVISIBLE);
        }else {
            recyclerView.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailsListener){
            mDetailsListener = (DetailsListener) context;
        } else {
            throw new RuntimeException(context.toString() +
            " Needs to impliment DetailsInterface");
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        mOrientation = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        outState.putBundle("bundle", mBundle);
        outState.putBoolean("ori", mOrientation);
        outState.putInt("index", mIndex);
        outState.putParcelableArrayList("steps", mSteps);
        outState.putInt("seek", playerPosition);
        mDetailsListener.onRotation(mBundle);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        test();
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle("bundle");
            mOrientation = savedInstanceState.getBoolean("ori");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDetailsListener = null;
        if (player != null){
            player.stop();
            player.release();
        }
    }
    public Boolean isInLandScape(Context context){
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null){
            player.stop();
            player.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null){
            player.stop();
            player.release();
        }
    }

    private void test(){

    }
}
