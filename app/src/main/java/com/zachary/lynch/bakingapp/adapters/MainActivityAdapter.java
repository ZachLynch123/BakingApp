package com.zachary.lynch.bakingapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Ingredients;
import com.zachary.lynch.bakingapp.model.Recipes;
import com.zachary.lynch.bakingapp.model.Steps;
import com.zachary.lynch.bakingapp.ui.MainActivityFragment.MainFragmentListener;
import com.zachary.lynch.bakingapp.ui.MasterDetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class MainActivityAdapter extends BaseAdapter {
    private Context mContext;
    private Recipes[] mRecipes;
    private TextView mTextView;
    private MainFragmentListener listener;


    public MainActivityAdapter(Context context, Recipes[] recipes) {
        mContext = context;
        mRecipes = recipes;
    }

    @Override
    public int getCount() {
        return mRecipes.length;
    }

    @Override
    public Object getItem(int position) {
        return mRecipes[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.main_list_layout, parent, false);
            holder = new ViewHolder();
            holder.recipeName = view.findViewById(R.id.recipeName);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Recipes singleRecipe = mRecipes[position];
        holder.recipeName.setText(singleRecipe.getRecipeName());
        return view;
    }

    private static class ViewHolder{
        TextView recipeName;
    }
}
