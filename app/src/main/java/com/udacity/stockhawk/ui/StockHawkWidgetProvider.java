package com.udacity.stockhawk.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;

/**
 * Created by tobi on 2017. 04. 19..
 */

public class StockHawkWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews rvs = new RemoteViews(context.getPackageName(), R.layout.stock_hawk_widget);
            appWidgetManager.updateAppWidget(appWidgetId, rvs);
        }

    }
}
