package com.zachary.lynch.bakingapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
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

    public interface DetailsListener{
        void onRotation(Bundle bundle, boolean orientation);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        test();
        mBundle = getArguments();
        if (savedInstanceState != null){
            onViewStateRestored(savedInstanceState);
        }
        assert mBundle != null;
        mIndex = mBundle.getInt("index");
        mSteps = mBundle.getParcelableArrayList(MainActivity.STEPS);

        final View view = inflater.inflate(R.layout.detail_fragment, container, false);
        mBackButton = view.findViewById(R.id.backButton);
        mNextButton = view.findViewById(R.id.nextButton);

       mOrientation = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;


        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIndex == 0){
                    mBackButton.setVisibility(View.INVISIBLE);
                    checkButtonStatus();
                } else{
                    mBackButton.setVisibility(View.VISIBLE);
                    mIndex = mIndex - 1;
                    checkButtonStatus();
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
                    checkButtonStatus();
                    updateUi(view);
                }
            }
        });
        updateUi(view);
        return view;
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
        RecyclerView recyclerView = view.findViewById(R.id.detailsRecyclerView);
        DetailsAdapter adapter = new DetailsAdapter(getContext(), mIndex, mSteps, mOrientation);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
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
        outState.putBundle("bundle", mBundle);
        outState.putBoolean("ori", mOrientation);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        test();
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle("bundle");
            mOrientation = savedInstanceState.getBoolean("ori");
            mDetailsListener.onRotation(mBundle, mOrientation);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDetailsListener = null;
    }


    private void test(){

    }
}
