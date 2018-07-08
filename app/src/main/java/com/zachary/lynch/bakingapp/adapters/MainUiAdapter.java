package com.zachary.lynch.bakingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Recipes;


public class MainUiAdapter extends BaseAdapter {
    private Context mContext;
    private Recipes[] mRecipes;

    public MainUiAdapter(Context context, Recipes[] recipes) {
        mContext= context;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.main_list_layout, null);
            holder = new ViewHolder();

            holder.mImageView = view.findViewById(R.id.recipeCard);
            holder.mTextView = view.findViewById(R.id.recipeName);

        }


        return null;
    }
    private static class ViewHolder{
        ImageView mImageView;
        TextView mTextView;

    }
}
