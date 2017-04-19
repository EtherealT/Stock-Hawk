package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


/**
 * Created by tobi on 2017. 04. 19..
 */

class WidgetDataProvider implements RemoteViewsFactory {

    private Context context;
    private Cursor cursor;
    private final DecimalFormat dollarFormatWithPlus;
    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;

    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        onDestroy();
        final long identityToken = Binder.clearCallingIdentity();

        Uri uri = Contract.Quote.URI;
        cursor = context.getContentResolver().query(uri,
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                null, null, Contract.Quote.COLUMN_SYMBOL);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) cursor.close();
    }

    @Override
    public int getCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || cursor == null || !cursor.moveToPosition(position))
            return null;

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_list_item_quote);
        rv.setTextViewText(R.id.symbol, cursor.getString(Contract.Quote.POSITION_SYMBOL));
        rv.setTextViewText(R.id.price, dollarFormat.format(cursor.getFloat(Contract.Quote.POSITION_PRICE)));

        float rawAbsoluteChange = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
        float percentageChange = cursor.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);

        String change = dollarFormatWithPlus.format(rawAbsoluteChange);
        String percentage = percentageFormat.format(percentageChange / 100);

        if (PrefUtils.getDisplayMode(context).equals(context.getString(R.string.pref_display_mode_absolute_key)))
            rv.setTextViewText(R.id.change, change);
        else rv.setTextViewText(R.id.change, percentage);

        if (rawAbsoluteChange > 0) rv.setTextColor(R.id.change, Color.parseColor("#00C853"));
        else rv.setTextColor(R.id.change, Color.parseColor("#D50000"));

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("stockSymbol", cursor.getString(Contract.Quote.POSITION_SYMBOL));
        rv.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

        return rv;
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

}
