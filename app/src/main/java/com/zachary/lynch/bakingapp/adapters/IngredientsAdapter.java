package com.zachary.lynch.bakingapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Ingredients;

import java.util.List;

public class IngredientsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Ingredients> mIngredients;

    public IngredientsAdapter(Context context, List<Ingredients> ingredients){
        mContext = context;
        mIngredients = ingredients;

    }


    @Override
    public int getCount() {
        return mIngredients.size();
    }

    @Override
    public Object getItem(int position) {
        return mIngredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.ingredients_layout, parent, false);
            holder = new ViewHolder();
            holder.quantity = view.findViewById(R.id.quantity);
            holder.measurement = view.findViewById(R.id.measurement);
            holder.ingredient = view.findViewById(R.id.ingredient);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Ingredients singleInstruction = mIngredients.get(position);
        holder.quantity.setText(singleInstruction.getQuality() + "");
        holder.measurement.setText(singleInstruction.getMeasure());
        holder.ingredient.setText(singleInstruction.getIngredient());
        return view;
    }

    private static class ViewHolder{
        TextView quantity;
        TextView measurement;
        TextView ingredient;


    }
}
