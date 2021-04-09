package com.binancetracker.ui.main;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.binancetracker.image.ImageCache;

public class CustomBinding {

    @BindingAdapter("setDoubleToTextview")
    public static void setDoubleToTextview(TextView view, double val)
    {
        String sval;
        if (val < 10.0d)
            sval = String.format("%.8f",val);
        else
            sval = String.format("%.2f",val);
        view.setText(sval);
    }

    @BindingAdapter("setPercentStringToTextView")
    public static void setPercentStringToTextView(TextView view, String val)
    {
        if (!TextUtils.isEmpty(val)) {
            view.setText(val + "%");
            if (val.startsWith("-"))
                view.setTextColor(Color.RED);
            else
                view.setTextColor(Color.GREEN);
        }
        else
            view.setText("");


    }

    @BindingAdapter("loadImage")
    public static void loadImage(ImageView view, String assetname) {
        if(view.getTag() == null || !view.getTag().equals(assetname)) {
            view.setImageBitmap(null);
            view.setTag(assetname);
            new ImageCache(view, assetname).loadBitmap();
        }
    }
}
