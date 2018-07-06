package com.zachary.lynch.bakingapp.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Recipes;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "Main Activity";

    private String json;
    private String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private Gson mGson = new Gson();
    private Recipes[] mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().
                url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(TAG, "Didn't WORK! " + e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                test();
                try{
                    String json = response.body().string();
                    if (response.isSuccessful()){
                        mRecipes = getRecipes(json);
                        test();
                    }else{
                        System.out.println("ERROR");
                    }
                }catch (IOException e){
                    Log.e(TAG, "IO EXCEPTION: " + e);
                }
            }
        });

    }

    private Recipes[] getRecipes(String json) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();
        Recipes[] recipes = gson.fromJson(json, Recipes[].class);
        Log.v(TAG, "runnable " + recipes[0].getRecipeName());
        return recipes;
    }

    private void test(){}
    }
    /*
     private class TrailersHttp extends AsyncTask<String, String, String >{
        @Override
        protected String doInBackground(String... strings) {
            while (jsonTrailerDataStuff == null) {
                final OkHttpClient client = new OkHttpClient();
                final Request request = new Request.Builder().
                        url(trailerUrl)
                        .build();
                try {
                    jsonTrailerDataStuff = client.newCall(request).execute().body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return jsonTrailerDataStuff;
     */