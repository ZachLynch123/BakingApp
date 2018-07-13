package com.zachary.lynch.bakingapp.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.adapters.MainActivityAdapter;
import com.zachary.lynch.bakingapp.model.Recipes;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivityFragment extends Fragment {
    private static String json;
    private static String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private Gson mGson = new Gson();
    private Recipes[] mRecipes = MainActivity.getRecipes();
    private RecyclerView mRecyclerView;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, container);

        View view = inflater.inflate(R.layout.main_fragment, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.mainFragmentRecyclerView);

        MainActivityAdapter adapter = new MainActivityAdapter(getContext(), mRecipes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        Log.v("shit", "ah");
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

        return view;

    }
    private void test(){

    }


}

