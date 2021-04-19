package com.binancetracker.ui.linechart;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.binancetracker.R;

import com.binancetracker.utils.ConvertingUtil;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class ChartMarkerView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */

    private TextView textView;
    public ChartMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        textView = findViewById(R.id.textview_value);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if(e.getX() < 1)
            textView.setText((String)e.getData() + ":" + ConvertingUtil.getDoubleString8F(e.getY()));
        else
            textView.setText((String)e.getData() + ":" + ConvertingUtil.getDoubleString2F(e.getY()));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
            return new MPPointF(0, -getHeight() * 5);
    }
}
