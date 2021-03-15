package com.binancetracker.ui.main;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.text.DecimalFormat;

public class CustomBinding {


    private static DecimalFormat decimalFormat = new DecimalFormat("0");
    @BindingAdapter("setDoubleToTextview")
    public static void setDoubleToTextview(TextView view, double val)
    {
        decimalFormat.setMaximumFractionDigits(20);
        view.setText(decimalFormat.format(val));
    }
}
