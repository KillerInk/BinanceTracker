package com.binancetracker.ui.main;

import android.widget.ImageView;
import android.widget.TextView;
import androidx.databinding.BindingAdapter;

import com.binancetracker.image.ImageCache;
import com.binancetracker.utils.ConvertingUtil;

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

    @BindingAdapter("loadImage")
    public static void loadImage(ImageView view, String assetname) {
        new ImageCache(view,assetname).loadBitmap();
    }
}
