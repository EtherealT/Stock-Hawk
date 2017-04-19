package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.os.AsyncTask;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.udacity.stockhawk.R;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;

import java.util.*;
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

    private void setupGraph(){
        List<Entry> entries = new ArrayList<>();
        history = history.subList(0, 51);
        Collections.reverse(history);

        for (int i = 0; i <= 50; i++) {
            HistoricalQuote hq = history.get(i);
            entries.add(new Entry(hq.getDate().getTimeInMillis(), hq.getClose().floatValue()));
        }

        LineDataSet dataSet = new LineDataSet(entries, stockSymbol);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getXAxis().setValueFormatter(new DayAxisValueFormatter(chart));

        chart.getAxisLeft().setTextColor(Color.WHITE);
        chart.getAxisRight().setTextColor(Color.WHITE);

        chart.setData(lineData);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.invalidate(); // refresh
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

}
