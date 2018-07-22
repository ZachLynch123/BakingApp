package com.zachary.lynch.bakingapp.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private Bundle saveBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        while (json == null){
            new FetchJson().execute(url);
        }
            test();
            mRecipes = setRecipes(json);

            manager.beginTransaction()
                    .add(R.id.placeholder, mainActivityFragment)
                    .addToBackStack("mainActivityFragment")
                    .commit();
       /* if (savedInstanceState!= null){
            onRestoreInstanceState(savedInstanceState);
        }*/
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

        manager.beginTransaction()
                .replace(R.id.placeholder, masterFragment)
                .addToBackStack("masterFragment")
                .commit();
    }

    @Override
    public void onStepClicked(ArrayList<Steps> steps, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEPS, steps);
        bundle.putInt("index", position);
        saveBundle = bundle;
        detailsFragment.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.placeholder, detailsFragment)
                .addToBackStack("detailsFragment")
                .commit();
    }

    @Override
    public void onRotation(Bundle bundle) {
        detailsFragment.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.placeholder, detailsFragment)
                .addToBackStack("detailsFragment")
                .commit();
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
                    .add(R.id.placeholder, mainActivityFragment)
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
/*
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle("bundle", saveBundle);
        super.onSaveInstanceState(outState);
    }

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

    public void test(){}


}

    // look up on udacity how to set up main activity using a grid layout or some kind of list layout with pictures
    //