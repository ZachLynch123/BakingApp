package com.zachary.lynch.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.adapters.MainActivityAdapter;
import com.zachary.lynch.bakingapp.model.Ingredients;
import com.zachary.lynch.bakingapp.model.Recipes;
import com.zachary.lynch.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivityFragment extends Fragment {
    private static String json;
    private static String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private Gson mGson = new Gson();
    private Recipes[] mRecipes;
    private ListView mListView;
    private MainFragmentListener mCommunicator;

    public interface MainFragmentListener {
        void onRecipeClick(ArrayList<Ingredients> ingredients, ArrayList<Steps> steps);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, container);
        test();
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        mListView = view.findViewById(R.id.mainFragmentListView);
        if (mRecipes == null){
            mRecipes = MainActivity.getRecipes();
        }

        MainActivityAdapter adapter = new MainActivityAdapter(getContext(), mRecipes);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCommunicator.onRecipeClick(mRecipes[position].getIngredientsList(), mRecipes[position].getStepsList());
            }
        });
        return view;
    }
    /*
    MainFragmentListener mCommunication = new MainFragmentListener() {
        @Override
        public void onRecipeClick(List<Ingredients> ingredients, List<Steps> steps) {
            MasterDetailFragment fragment = new MasterDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("ingredients", (ArrayList<? extends Parcelable>) ingredients);
            bundle.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) steps);
            fragment.setArguments(bundle);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.placeholder, fragment).commit();

        }
    };
    */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFragmentListener){
            mCommunicator = (MainFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
            + " Must impleoment interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCommunicator = null;
    }

    /*
            MainFragmentListener communication=new MainFragmentListener() {
            @Override
            public void respond(int position,String name,String job) {
                FragmentB fragmentB=new FragmentB();
                Bundle bundle=new Bundle();
                bundle.putString("NAME",name);
                bundle.putString("JOB",job);
                fragmentB.setArguments(bundle);
                FragmentManager manager=getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.dumper,fragmentB).commit();

             */
    private void test(){

    }


}

