package com.zachary.lynch.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Ingredients;

import java.util.ArrayList;



public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private ArrayList<Ingredients> mIngredients;

    public ListProvider(Context context, Intent intent){
        mContext = context;
        mIngredients = intent.getParcelableArrayListExtra("test");
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteViews = new RemoteViews(
                mContext.getPackageName(), R.layout.ingredients_layout);
        Ingredients ingredient = mIngredients.get(position);
        remoteViews.setTextViewText(R.id.quantity, ingredient.getQuality() + "");
        remoteViews.setTextViewText(R.id.measurement, ingredient.getMeasure());
        remoteViews.setTextViewText(R.id.ingredient, ingredient.getIngredient());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
