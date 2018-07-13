package com.zachary.lynch.bakingapp.adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Ingredients;
import com.zachary.lynch.bakingapp.model.Recipes;
import com.zachary.lynch.bakingapp.model.Steps;
import com.zachary.lynch.bakingapp.ui.MasterDetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {
    private Context mContext;
    private Recipes[] mRecipes;
    private TextView mTextView;

    public MainActivityAdapter(Context context, Recipes[] recipes) {
        mContext= context;
        mRecipes = recipes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        test();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_layout, parent, false);
        mTextView = view.findViewById(R.id.recipeName);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.BindView(mRecipes[position]);

    }

    @Override
    public int getItemCount() {
        return mRecipes.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipeName) TextView mRecipeName;
        private String mNames;
        private List<Ingredients> mIngredients;
        private List<Steps> mSteps;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
        public void BindView(Recipes recipes){
            test();
            mNames = recipes.getRecipeName();
            mIngredients = recipes.getIngredientsList();
            mSteps = recipes.getStepsList();

            if (mNames != null){
                mTextView.setText(mNames);
               // mRecipeName.setText(mNames);
            }else{
                Toast.makeText(mContext, "this is weird", Toast.LENGTH_LONG).show();
            }
        }


        @Override
        public void onClick(View v){
            Toast.makeText(mContext, mNames, Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Ingredients", (ArrayList<? extends Parcelable>) mIngredients);
            bundle.putParcelableArrayList("Steps", (ArrayList<? extends Parcelable>) mSteps);
            MasterDetailFragment fragment = new MasterDetailFragment();

        }
    }
    private void test(){

    }
}
