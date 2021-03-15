package com.binancetracker.ui.main;

import android.widget.TextView;
import androidx.databinding.BindingAdapter;
import com.binancetracker.utils.ConvertingUtil;

public class CustomBinding {

    @BindingAdapter("setDoubleToTextview")
    public static void setDoubleToTextview(TextView view, double val)
    {
        view.setText(ConvertingUtil.trimDecimalFormatToString(val,20));
    }
}
