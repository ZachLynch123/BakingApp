package com.zachary.lynch.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.adapters.WidgetService;
import com.zachary.lynch.bakingapp.model.Ingredients;

import java.util.ArrayList;

public class BakingAppWidget extends AppWidgetProvider {
    private ArrayList<Ingredients> mIngredients;
    private Intent mIntent;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Recieved", Toast.LENGTH_SHORT).show();
        mContext = context;
        mIngredients = intent.getParcelableArrayListExtra("test");
        super.onReceive(context, intent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {
            // won't call widgetService
            Intent serviceIntent = new Intent(context, WidgetService.class);
            serviceIntent.putExtra(appWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.putParcelableArrayListExtra("data", mIngredients);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
            remoteViews.setRemoteAdapter(R.id.ingredientsList, serviceIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

