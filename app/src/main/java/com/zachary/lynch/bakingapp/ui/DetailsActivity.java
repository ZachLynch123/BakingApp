package com.zachary.lynch.bakingapp.ui;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Ingredients;
import com.zachary.lynch.bakingapp.model.Steps;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements MasterDetailFragment.MasterListener, DetailsFragment.DetailsListener {
    ArrayList<Ingredients> mIngredients;
    ArrayList<Steps> mSteps;
    int count;

    android.support.v4.app.FragmentManager manager = getSupportFragmentManager();


    MasterDetailFragment masterDetailFragment = new MasterDetailFragment();
    DetailsFragment detailsFragment = new DetailsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        mIngredients = bundle.getParcelableArrayList("ingredients");
        mSteps = bundle.getParcelableArrayList("steps");
        masterDetailFragment.setArguments(bundle);
        if (savedInstanceState == null) {
            manager.beginTransaction()
                    .replace(R.id.detailsPlaceHolder, masterDetailFragment)
                    .commit();
        }else
            manager.beginTransaction()
            .replace(R.id.detailsPlaceHolder, detailsFragment)
            .commit();

    }

    @Override
    public void onStepClicked(ArrayList<Steps> step, int index) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("steps", step);
        bundle.putInt("index", index);
        detailsFragment.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.detailsPlaceHolder, detailsFragment)
                .addToBackStack("detailsFragment")
                .commit();
    }

    @Override
    public void buttonClicked(int index, ArrayList<Steps> steps) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("count", count);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRotation(Bundle bundle) {
    }
}
