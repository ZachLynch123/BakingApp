package com.zachary.lynch.bakingapp.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.RestrictTo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.adapters.MainActivityAdapter;
import com.zachary.lynch.bakingapp.model.Ingredients;
import com.zachary.lynch.bakingapp.model.Recipes;
import com.zachary.lynch.bakingapp.model.Steps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
@RestrictTo(RestrictTo.Scope.TESTS)
public class MainActivity extends AppCompatActivity implements MainActivityFragment.MainFragmentListener, MasterDetailFragment.MasterListener, DetailsFragment.DetailsListener {
    private static String TAG = "Main Activity";
    public static String STEP_DESCRIPTION = "description";
    public static String SHORT_DESC = "short description";
    public static String VIDEO_URL = "video url";
    public static String STEPS = "steps";

    private static String json;
    private static String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private Gson mGson = new Gson();
    private static Recipes[] mRecipes;
    private android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
    private MainActivityFragment mainActivityFragment = new MainActivityFragment();
    private MasterDetailFragment masterFragment = new MasterDetailFragment();
    private DetailsFragment detailsFragment = new DetailsFragment();
    private VideoFragment videoFragment = new VideoFragment();
    private Bundle mBundle;

    private boolean mTabletScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (isNetworkAvailable()) {

            while (json == null) {
                new FetchJson().execute(url);
            }
            test();
            mRecipes = setRecipes(json);

            manager.beginTransaction()
                    .add(R.id.masterList, mainActivityFragment)
                    .addToBackStack("mainActivityFragment")
                    .commit();
            if (findViewById(R.id.tabletLayout) != null) {
                mTabletScreen = true;
            } else {
                mTabletScreen = false;
            }
       /* if (savedInstanceState!= null){
            onRestoreInstanceState(savedInstanceState);
        }*/
        } else {
            Toast.makeText(this, "Please check network connection", Toast.LENGTH_LONG).show();
        }
    }

    private Recipes[] setRecipes(String json) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();
        return gson.fromJson(json, Recipes[].class);
    }


    @Override
    public void onRecipeClick(ArrayList<Ingredients> ingredients, ArrayList<Steps> steps) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ingredients", ingredients);
        bundle.putParcelableArrayList("steps", steps);
        masterFragment.setArguments(bundle);


        Intent intent = new Intent(this, BakingAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), BakingAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        intent.putParcelableArrayListExtra("test", ingredients);
        sendBroadcast(intent);

        manager.beginTransaction()
                .replace(R.id.masterList, masterFragment,"MasterFragment")
                .addToBackStack("masterFragment")
                .commit();
    }

    @Override
    public void onStepClicked(ArrayList<Steps> steps, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEPS, steps);
        bundle.putInt("index", position);
        detailsFragment.setArguments(bundle);
        videoFragment.setArguments(bundle);

        if (mTabletScreen){
            manager.beginTransaction()
                    .replace(R.id.detailsList, detailsFragment)
                    .replace(R.id.videoPlaceHolder, videoFragment)
                    .commit();
        }else {
            manager.beginTransaction()
                    .remove(masterFragment)
                    .replace(R.id.detailsList, detailsFragment, "DetailsFragment")
                    .replace(R.id.videoPlaceHolder, videoFragment, "VideoFragment")
                    .addToBackStack("detailsFragment")
                    .commit();
        }
    }

    @Override
    public void buttonClicked(int index, ArrayList<Steps> steps) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEPS, steps);
        bundle.putInt("index", index);
        videoFragment = new VideoFragment();
        videoFragment.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.videoPlaceHolder, videoFragment)
                .commit();
    }

    @Override
    public void onRotation(Bundle bundle, boolean orientation) {
        detailsFragment.setArguments(bundle);
        manager.beginTransaction()
                .remove(mainActivityFragment)
                .replace(R.id.detailsList, detailsFragment)
                .replace(R.id.videoPlaceHolder, videoFragment)
                .commit();

    }

    /*@Override
    public void onRotation(Bundle bundle, boolean orientation) {
        detailsFragment.setArguments(bundle);
        if (orientation){
            if (getSupportActionBar()!= null){
                getSupportActionBar().hide();
            }
        } else {
            if (getSupportActionBar() != null){
                getSupportActionBar().show();
            }
        }
        manager.beginTransaction()
                .replace(R.id.detailsList, detailsFragment)
                .addToBackStack("detailsFragment")
                .commit();
    }
    */


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.recipeCardFrag) {
            manager.beginTransaction()
                    .add(R.id.masterList, mainActivityFragment)
                    .addToBackStack("mainActivityFragment")
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle("bundle", mBundle);
        super.onSaveInstanceState(outState);
    }
/*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            savedInstanceState.getBundle("bundle");
            detailsFragment.setArguments(savedInstanceState);
            manager.beginTransaction()
                    .replace(R.id.placeholder, detailsFragment)
                    .commit();
        }
    }
    */

    public static Recipes[] getRecipes() {
        return mRecipes;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    public void test(){}


}

    // look up on udacity how to set up main activity using a grid layout or some kind of list layout with pictures
    //