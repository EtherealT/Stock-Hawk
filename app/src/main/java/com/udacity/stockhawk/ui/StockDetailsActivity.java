package com.udacity.stockhawk.ui;

import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.udacity.stockhawk.R;
import com.github.mikephil.charting.charts.LineChart;

public class StockDetailsActivity extends AppCompatActivity{

    private String stockSymbol;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        Intent intent = getIntent();
        stockSymbol = intent.getStringExtra("stockSymbol");
        Log.i(getClass() + "stockSymbol", stockSymbol);

        LineChart chart = (LineChart) findViewById(R.id.chart);

    }

}
