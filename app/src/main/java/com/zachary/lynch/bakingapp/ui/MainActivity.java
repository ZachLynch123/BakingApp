package com.zachary.lynch.bakingapp.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Recipes;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "Main Activity";

    private static String json;
    private static String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private Gson mGson = new Gson();
    private Recipes[] mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        while (json == null){
            new FetchJson().execute(url);
        }
        mRecipes = getRecipes(json);
        Log.v(TAG, "" + mRecipes[0].getRecipeName());
    }

    private Recipes[] getRecipes(String json) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();
        return gson.fromJson(json, Recipes[].class);
    }

    private static class FetchJson extends AsyncTask<String, String, String >{
        @Override
        protected String doInBackground(String... strings) {
            while (json == null){
                final OkHttpClient client = new OkHttpClient();
                final Request request = new Request.Builder()
                        .url(url)
                        .build();
                try{
                    json = client.newCall(request).execute().body().string();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            return json;
        }
    }
    private void test(){}
    }

    // look up on udacity how to set up main activity using a grid layout or some kind of list layout with pictures
    //