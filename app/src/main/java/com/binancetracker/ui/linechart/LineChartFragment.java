package com.binancetracker.ui.linechart;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.binancetracker.R;
import com.binancetracker.databinding.LinechartFragmentBinding;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LineChartFragment extends Fragment {
    private LinechartFragmentBinding fragmentBinding;
    private LineChartViewModel mViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.linechart_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(LineChartViewModel.class);
        createLineChart();

        fragmentBinding.button1week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.setTimeToFetch(TimeToFetch.week);
            }
        });

        fragmentBinding.button1month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.setTimeToFetch(TimeToFetch.month);
            }
        });

        fragmentBinding.button1year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.setTimeToFetch(TimeToFetch.year);
            }
        });
        return fragmentBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.onResume();
    }

    private void createLineChart() {
        // no description text
        fragmentBinding.chart2.getDescription().setEnabled(false);

        // enable touch gestures
        fragmentBinding.chart2.setTouchEnabled(true);

        fragmentBinding.chart2.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        fragmentBinding.chart2.setDragEnabled(true);
        fragmentBinding.chart2.setScaleEnabled(true);
        fragmentBinding.chart2.setDrawGridBackground(false);
        fragmentBinding.chart2.setHighlightPerDragEnabled(true);

        // set an alternative background color
        fragmentBinding.chart2.setBackgroundColor(Color.BLACK);
        fragmentBinding.chart2.setViewPortOffsets(0f, 0f, 0f, 0f);
        // get the legend (only possible after setting data)
        Legend ll = fragmentBinding.chart2.getLegend();
        ll.setEnabled(true);
        ll.setTextSize(7f);
        ll.setFormSize(7f);
        ll.setFormToTextSpace(1f);
        ll.setForm(Legend.LegendForm.CIRCLE);
        ll.setWordWrapEnabled(true);
        ll.setDrawInside(true);
        ll.setYOffset(10f);
        ll.setXOffset(35f);
        ll.setMaxSizePercent(0);
        ll.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT );

        ll.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        ll.setTextColor(Color.WHITE);

        XAxis xAxis = fragmentBinding.chart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setTextSize(5f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = fragmentBinding.chart2.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(5f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(170f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));

        YAxis rightAxis = fragmentBinding.chart2.getAxisRight();
        rightAxis.setEnabled(false);

        mViewModel.lineChartModel.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                fragmentBinding.chart2.post(new Runnable() {
                    @Override
                    public void run() {
                        YAxis leftAxis = fragmentBinding.chart2.getAxisLeft();
                        leftAxis.setAxisMinimum(mViewModel.lineChartModel.getMin());
                        leftAxis.setAxisMaximum(mViewModel.lineChartModel.getMax());

                        fragmentBinding.chart2.setData(mViewModel.lineChartModel.getData());
                        //fragmentBinding.chart2.bringToFront();
                        fragmentBinding.chart2.invalidate();

                    }
                });
            }
        });
    }
}
