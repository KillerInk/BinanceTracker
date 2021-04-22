package com.binancetracker.ui.linechart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.binancetracker.R;
import com.binancetracker.databinding.LinechartFragmentBinding;
import com.binancetracker.utils.ConvertingUtil;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LineChartFragment extends Fragment {

    public static final String ASSET_MSG = "asset_msg";

    private static final String TAG = LineChartFragment.class.getSimpleName();
    private LinechartFragmentBinding fragmentBinding;
    private LineChartViewModel mViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.linechart_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(LineChartViewModel.class);
        Intent intent = getActivity().getIntent();
        if (intent != null)
        {
            String as = intent.getStringExtra(ASSET_MSG);
            if (as != null)
                mViewModel.setAsset(as);
        }

        createLineChart();

        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(getContext(),
                R.array.timerange_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        fragmentBinding.timerangeSpinner.setAdapter(adapter);

        fragmentBinding.timerangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TimeToFetch timeToFetch = TimeToFetch.values()[position];
                mViewModel.setTimeToFetch(timeToFetch);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        //fragmentBinding.chart2.setViewPortOffsets(0f, 0f, 0f, 0f);
        // get the legend (only possible after setting data)
        Legend ll = fragmentBinding.chart2.getLegend();
        ll.setEnabled(true);
        ll.setTextSize(7f);
        ll.setFormSize(7f);
        ll.setFormToTextSpace(1f);
        ll.setForm(Legend.LegendForm.CIRCLE);
        ll.setWordWrapEnabled(true);
        ll.setDrawInside(false);
        ll.setYOffset(3f);
        //ll.setXOffset(35f);
        //ll.setMaxSizePercent(0);
        ll.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT );

        ll.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        ll.setTextColor(Color.WHITE);

        XAxis xAxis = fragmentBinding.chart2.getXAxis();
        //xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setTextSize(5f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                //long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date((long) value));
            }
        });

        YAxis leftAxis = fragmentBinding.chart2.getAxisLeft();
        //leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawAxisLine(true);
        //leftAxis.setDrawGridLines(false);
        //leftAxis.setGranularityEnabled(false);
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

        ChartMarkerView chartMarkerView = new ChartMarkerView(getContext(),R.layout.chartmarkerview);
        // create marker to display box when values are selected

        // Set the marker to the chart
        chartMarkerView.setChartView(fragmentBinding.chart2);
        fragmentBinding.chart2.setMarker(chartMarkerView);
    }
}
