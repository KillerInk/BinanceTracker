package com.binancetracker.ui.linechart;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import com.binancetracker.R;

import com.binancetracker.repo.room.entity.PortofolioHistory;
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
        textView.setTextColor(Color.RED);
        textView.setBackgroundColor(Color.BLACK);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e.getData() instanceof PortofolioHistory) {
            PortofolioHistory portofolioHistory = (PortofolioHistory) e.getData();
            textView.setText(getMarkerString(portofolioHistory,e));
        }
        else
        {
            textView.setText("Total:" + e.getY());
        }
        super.refreshContent(e, highlight);
    }

    private String getMarkerString(PortofolioHistory history,Entry e)
    {
        StringBuilder b = new StringBuilder();
        b.append(history.asset).append(":");
        if (e.getX() < 1)
            b.append(ConvertingUtil.getDoubleString8F(e.getY()));
        else
            b.append(ConvertingUtil.getDoubleString2F(e.getY()));
        b.append("\n");
        b.append("Amount:").append(ConvertingUtil.getDoubleString8F(history.amount)).append("\n");
        b.append("Price:").append(history.price);
        return b.toString();
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth()/2),-(getHeight()*5));
    }
}
