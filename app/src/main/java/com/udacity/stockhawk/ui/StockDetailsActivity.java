package com.udacity.stockhawk.ui;

import android.util.Log;
import android.os.Bundle;
import android.os.AsyncTask;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.udacity.stockhawk.R;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;
import java.io.IOException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;
import yahoofinance.histquotes.HistoricalQuote;

public class StockDetailsActivity extends AppCompatActivity{

    private String stockSymbol;
    private Stock stock;
    private List<HistoricalQuote> history;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        Intent intent = getIntent();
        stockSymbol = intent.getStringExtra("stockSymbol");
        Log.i(getClass() + ":stockSymbol", stockSymbol);

        new StockDetailsQuery().execute(stockSymbol);

        LineChart chart = (LineChart) findViewById(R.id.chart);
    }

    private class StockDetailsQuery extends AsyncTask<String, Void, Stock>{

        @Override
        protected Stock doInBackground(String... params) {
            String stockSymbol = params[0];
            Stock s = null;

            try {
                s = YahooFinance.get(stockSymbol, true);
                history = s.getHistory(Interval.DAILY);
                Log.i(getClass() + ":stockHistoryDepth", String.valueOf(history.size()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return s;
        }

        @Override
        protected void onPostExecute(Stock s) {
            stock = s;
            Log.i(getClass() + ":stock", stock.toString());
       }
    }

}
