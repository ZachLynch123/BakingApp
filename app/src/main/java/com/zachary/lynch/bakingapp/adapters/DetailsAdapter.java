package com.zachary.lynch.bakingapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
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
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Steps;

import java.util.ArrayList;


public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {
    private Context mContext;
    private int mStepIndex;
    private ArrayList<Steps> mSteps;
    private boolean mOrientation;
    private int startWindow;
    private long startPosition;


    public DetailsAdapter(Context context, int stepIndex, ArrayList<Steps> arrayList, boolean orientation) {
        mContext = context;
        mStepIndex = stepIndex;
        mSteps = arrayList;
        mOrientation = orientation;
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.detail_fragment_layout, parent, false);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.DetailsViewHolder holder, int position) {
        test();
        holder.bindDetails(mSteps.get(mStepIndex));
        test();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class DetailsViewHolder extends RecyclerView.ViewHolder {
        PlayerView playerView;
        TextView description;
        Button backDesc;
        Button forwardDesc;
        TextView noVideo;


        public DetailsViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.detailDescription);
            playerView = itemView.findViewById(R.id.videoPlayerView);
            backDesc = itemView.findViewById(R.id.backButton);
            forwardDesc = itemView.findViewById(R.id.nextButton);
            noVideo = itemView.findViewById(R.id.noVideo);
        }

        public void bindDetails(Steps steps) {
            description.setText(steps.getDescription());
            test();
            if (mSteps.get(mStepIndex).getVideoURL() == null){
                noVideo.setVisibility(View.VISIBLE);
            } else {
                noVideo.setVisibility(View.INVISIBLE);
            }
           /* if (mStepIndex != 0) {
                backDesc.setVisibility(View.VISIBLE);
                backDesc.setText(mSteps.get(mStepIndex - 1).getShortDescription());
            } else {
                backDesc.setVisibility(View.INVISIBLE);
            }
            if (mStepIndex == mSteps.size()) {
                forwardDesc.setVisibility(View.INVISIBLE);
            } else {
                forwardDesc.setVisibility(View.VISIBLE);
                forwardDesc.setText(mSteps.get(mStepIndex + 1).getShortDescription());
            }*/

            //initializePlayer(playerView);

        }
    }



    private void initializePlayer(PlayerView mPlayerView) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, new DefaultLoadControl());

        mPlayerView.setUseController(true);
        mPlayerView.requestFocus();

        mPlayerView.setPlayer(player);

        boolean resumePosition = startWindow != C.INDEX_UNSET;

        if (resumePosition){
            mPlayerView.getPlayer().seekTo(startWindow, startPosition);
        }

        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                mContext,
                Util.getUserAgent(mContext, "BakingApp"),
                defaultBandwidthMeter);

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource videoSource = new ExtractorMediaSource(
                Uri.parse(mSteps.get(mStepIndex).getVideoURL()),
                dataSourceFactory,
                extractorsFactory,
                null,
                null);

        player.prepare(videoSource);



        player.setPlayWhenReady(false);


        if (mOrientation){
            ViewGroup.LayoutParams params = mPlayerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

    private void test(){

    }
}
