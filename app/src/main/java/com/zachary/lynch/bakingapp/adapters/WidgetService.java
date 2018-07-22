package com.zachary.lynch.bakingapp.adapters;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Ingredients;

import java.util.ArrayList;


public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
       return new ListProvider(getApplicationContext(), intent);
    }
    class ListProvider implements RemoteViewsFactory {
        private Context mContext;
        private ArrayList<Ingredients> mIngredients;
        private Intent mIntent;


        public ListProvider(Context context, Intent intent){
            test();
            mContext = context;
            mIngredients = intent.getParcelableArrayListExtra("data");
        }
        @Override
        public void onCreate() {
            test();
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
            test();
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
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private void test(){}
    }


}
