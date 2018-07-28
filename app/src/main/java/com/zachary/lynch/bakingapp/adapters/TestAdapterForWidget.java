package com.zachary.lynch.bakingapp.adapters;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Ingredients;

import java.util.ArrayList;

public class TestAdapterForWidget extends BroadcastReceiver implements RemoteViewsService.RemoteViewsFactory {
    Context mContext;
    ArrayList<Ingredients> mIngredients;
    String[] list = {"Treehouse", "Android", "Java", "Kotlin", "Anko"};

    public TestAdapterForWidget(Context context, Intent intent){
        mContext = context;
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
        return (mIngredients != null) ? mIngredients.size(): 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.widgetIngredient, mIngredients.get(position).getIngredient());
        return views;
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
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mIngredients = intent.getParcelableArrayListExtra("test");
    }
}
