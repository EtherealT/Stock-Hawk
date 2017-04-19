package com.udacity.stockhawk.ui;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Calendar;

/**
 * Created by tobi on 2017. 04. 19..
 */

public class DayAxisValueFormatter implements IAxisValueFormatter {

    private BarLineChartBase<?> chart;

    protected String[] months = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    public DayAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long)value);

        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return months[month] + " " + String.valueOf(day);
    }

}
