package com.binancetracker.ui.main;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.binancetracker.image.ImageCache;

public class CustomBinding {

    @BindingAdapter("setDoubleToTextview")
    public static void setDoubleToTextview(TextView view, double val)
    {
        String sval ="";
        if (val < 10.0d)
            sval = String.format("%.8f",val);
        else
            sval = String.format("%.2f",val);
        view.setText(sval);
    }

    @BindingAdapter("setPercentStringToTextView")
    public static void setPercentStringToTextView(TextView view, String val)
    {
        if (val.startsWith("-"))
            view.setTextColor(Color.RED);
        else
            view.setTextColor(Color.GREEN);
        view.setText(val+"%");
    }

    @BindingAdapter("loadImage")
    public static void loadImage(ImageView view, String assetname) {
        view.setImageBitmap(null);
        new ImageCache(view,assetname).loadBitmap();
    }
}
