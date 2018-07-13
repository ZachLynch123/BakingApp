package com.zachary.lynch.bakingapp.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.adapters.MainActivityAdapter;
import com.zachary.lynch.bakingapp.model.Recipes;

import java.io.IOException;

import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "Main Activity";

    private static String json;
    private static String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private Gson mGson = new Gson();
    private static Recipes[] mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        while (json == null){
            new FetchJson().execute(url);
        }
        Log.v(TAG, "MAIN ACTIVITY " + json);

        // this while loop may be redundant. delete after tests
            test();
            mRecipes = setRecipes(json);
            MainActivityFragment fragment = new MainActivityFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .add(R.id.placeholder, fragment)
                    .commit();

    }

    private Recipes[] setRecipes(String json) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();
        return gson.fromJson(json, Recipes[].class);
    }

    private static class FetchJson extends AsyncTask<String, String, String >{
        @Override
        protected String doInBackground(String... strings) {

            final OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            while (json == null){
                    try{
                        json = client.newCall(request).execute().body().string();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
            }
            return json;
        }
    }

    public static Recipes[] getRecipes() {
        return mRecipes;
    }

    public void test(){}


}

    // look up on udacity how to set up main activity using a grid layout or some kind of list layout with pictures
    //