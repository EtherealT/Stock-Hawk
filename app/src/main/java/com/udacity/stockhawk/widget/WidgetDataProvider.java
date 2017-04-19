package com.udacity.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

/**
 * Created by tobi on 2017. 04. 19..
 */

class WidgetDataProvider implements RemoteViewsFactory {

    private Context context;
    private Cursor cursor;

    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
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

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.list_item_quote);
        rv.setTextViewText(R.id.symbol, cursor.getString(Contract.Quote.POSITION_SYMBOL));

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
