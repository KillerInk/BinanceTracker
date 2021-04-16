package com.binancetracker.ui.main;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.binancetracker.BR;
import com.binancetracker.MainActivity;
import com.binancetracker.R;
import com.binancetracker.databinding.MainFragmentBinding;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private CustomAdapter customAdapter;
    private MainFragmentBinding mainFragmentBinding;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);


        mainFragmentBinding.imageButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                if (navController.getCurrentDestination().getId() != R.id.keyFragment) {
                    navController.navigate(R.id.action_mainFragment_to_keyFragment);
                }
            }
        });

        mainFragmentBinding.imageButtonDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                if (navController.getCurrentDestination().getId() != R.id.dataBaseFragment) {
                    navController.navigate(R.id.action_mainFragment_to_dataBaseFragment);
                }
            }
        });

        customAdapter = new CustomAdapter();
        mainFragmentBinding.recyclerViewBalance.setAdapter(customAdapter);
        mainFragmentBinding.recyclerViewBalance.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.balances.observe(getViewLifecycleOwner(), new Observer<Collection<AssetModel>>() {
            @Override
            public void onChanged(Collection<AssetModel> strings) {
                customAdapter.setLocalDataSet(strings);
            }
        });

        createPieChart();
        createLineChart();

        return mainFragmentBinding.getRoot();
    }

    private void createPieChart() {
        mainFragmentBinding.chart1.setUsePercentValues(true);

        mainFragmentBinding.chart1.setDrawHoleEnabled(true);
        mainFragmentBinding.chart1.setHoleColor(Color.BLACK);
        mainFragmentBinding.chart1.setHoleRadius(58f);

        mainFragmentBinding.chart1.setTransparentCircleColor(Color.GRAY);
        mainFragmentBinding.chart1.setTransparentCircleAlpha(110);
        mainFragmentBinding.chart1.setTransparentCircleRadius(61f);

        mainFragmentBinding.chart1.setCenterTextColor(Color.WHITE);
        mainFragmentBinding.chart1.setCenterTextSize(11f);
        mainFragmentBinding.chart1.setCenterText("Asset");
        mainFragmentBinding.chart1.setDrawCenterText(true);

        mainFragmentBinding.chart1.setRotationAngle(0);
        // enable rotation of the chart by touch
        mainFragmentBinding.chart1.setRotationEnabled(true);
        mainFragmentBinding.chart1.setHighlightPerTapEnabled(false);
        // undo all highlights
        mainFragmentBinding.chart1.highlightValues(null);
        mainFragmentBinding.chart1.setEntryLabelTextSize(10f);
        mainFragmentBinding.chart1.setEntryLabelColor(Color.WHITE);
        mainFragmentBinding.chart1.setExtraOffsets(10, 15, 10, 15);


        Legend l = mainFragmentBinding.chart1.getLegend();
        l.setEnabled(false);

        mViewModel.pieChartModel.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.pieData) {
                    mainFragmentBinding.chart1.post(() -> {
                        mainFragmentBinding.chart1.setData(mViewModel.pieChartModel.getPieData());
                        mainFragmentBinding.chart1.setCenterText(mViewModel.pieChartModel.getPiechartMidString());
                        mainFragmentBinding.chart1.invalidate();
                    });
                }
            }
        });
    }

    private void createLineChart() {
        // no description text
        mainFragmentBinding.chart2.getDescription().setEnabled(false);

        // enable touch gestures
        mainFragmentBinding.chart2.setTouchEnabled(true);

        mainFragmentBinding.chart2.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mainFragmentBinding.chart2.setDragEnabled(true);
        mainFragmentBinding.chart2.setScaleEnabled(true);
        mainFragmentBinding.chart2.setDrawGridBackground(false);
        mainFragmentBinding.chart2.setHighlightPerDragEnabled(true);

        // set an alternative background color
        mainFragmentBinding.chart2.setBackgroundColor(Color.BLACK);
        mainFragmentBinding.chart2.setViewPortOffsets(0f, 0f, 0f, 0f);
        // get the legend (only possible after setting data)
        Legend ll = mainFragmentBinding.chart2.getLegend();
        ll.setEnabled(true);
        ll.setTextSize(7f);
        ll.setFormSize(7f);
        ll.setFormToTextSpace(1f);
        ll.setForm(Legend.LegendForm.CIRCLE);
        ll.setWordWrapEnabled(true);
        ll.setDrawInside(false);
        ll.setYOffset(10f);
        ll.setXOffset(35f);
        ll.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        ll.setTextColor(Color.WHITE);

        XAxis xAxis = mainFragmentBinding.chart2.getXAxis();
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

        YAxis leftAxis = mainFragmentBinding.chart2.getAxisLeft();
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

        YAxis rightAxis = mainFragmentBinding.chart2.getAxisRight();
        rightAxis.setEnabled(false);

        mViewModel.lineChartModel.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mainFragmentBinding.chart2.post(new Runnable() {
                    @Override
                    public void run() {
                        YAxis leftAxis = mainFragmentBinding.chart2.getAxisLeft();
                        leftAxis.setAxisMinimum(mViewModel.lineChartModel.getMin());
                        leftAxis.setAxisMaximum(mViewModel.lineChartModel.getMax());

                        mainFragmentBinding.chart2.setData(mViewModel.lineChartModel.getData());
                        //mainFragmentBinding.chart2.bringToFront();
                        mainFragmentBinding.chart2.invalidate();

                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }



}