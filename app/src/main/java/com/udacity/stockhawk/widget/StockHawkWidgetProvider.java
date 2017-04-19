package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.MainActivity;

/**
 * Created by tobi on 2017. 04. 19..
 */

public class StockHawkWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            RemoteViews rvs = new RemoteViews(context.getPackageName(), R.layout.stock_hawk_widget);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);
            rvs.setOnClickPendingIntent(R.id.widget, pending);

            Intent i = new Intent(context, WidgetRemoteViewsService.class);
            rvs.setRemoteAdapter(R.id.widget_stock_list, i);

            appWidgetManager.updateAppWidget(appWidgetId, rvs);
        }

    }
}
