package com.zachary.lynch.bakingapp.ui;

import android.app.PendingIntent;
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

        /*
            final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, ExampleActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider_layout);
            views.setOnClickPendingIntent(R.id.button, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
         */
        final int widgetId = appWidgetIds.length - 1;

        for (int i = 0; i < widgetId; i++){
            int oneId = appWidgetIds[i];
            Intent intent = new Intent(context, WidgetService.class);
            intent.putParcelableArrayListExtra("data", mIngredients);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
            views.setRemoteAdapter(R.id.widgetList, intent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(widgetId, views);

        }

        /*for (int appWidgetId : appWidgetIds) {
            // won't call widgetService
            Intent serviceIntent = new Intent(context, WidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.putParcelableArrayListExtra("data", mIngredients);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
            remoteViews.setRemoteAdapter(R.id.widgetList, serviceIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        */
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

