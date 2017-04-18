package com.udacity.stockhawk.ui;

import android.graphics.Color;
import android.util.Log;
import android.os.Bundle;
import android.os.AsyncTask;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;
import yahoofinance.histquotes.HistoricalQuote;

public class StockDetailsActivity extends AppCompatActivity{

    private String stockSymbol;
    private List<HistoricalQuote> history;
    private LineChart chart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);
        chart = (LineChart) findViewById(R.id.chart);

        Intent intent = getIntent();
        stockSymbol = intent.getStringExtra("stockSymbol");

        new StockDetailsQuery().execute(stockSymbol);
    }

    private class StockDetailsQuery extends AsyncTask<String, Void, Stock>{

        @Override
        protected Stock doInBackground(String... params) {
            String stockSymbol = params[0];
            Stock s = null;

            try {
                s = YahooFinance.get(stockSymbol, true);
                history = s.getHistory(Interval.DAILY);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return s;
        }

        @Override
        protected void onPostExecute(Stock s) {
            setupGraph();
        }
    }

    private void setupGraph(){
        List<Entry> entries = new ArrayList<>();
        List<HistoricalQuote> graphData = history.subList(0, 30);

        for (int i = 0; i < 30; i++)
            entries.add(new Entry(i, graphData.get(i).getClose().floatValue()));

        LineDataSet dataSet = new LineDataSet(entries, stockSymbol);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);

        chart.setData(lineData);
        chart.invalidate(); // refresh
    }

}
