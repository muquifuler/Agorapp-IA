package com.agora.agorapp;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class PercentValueFormatter extends ValueFormatter {

    private DecimalFormat decimalFormat;

    public PercentValueFormatter() {
        decimalFormat = new DecimalFormat("###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value) {
        return decimalFormat.format(value) + "%";
    }
}