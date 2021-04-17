package com.binancetracker.ui.piechart;

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

import com.binancetracker.BR;
import com.binancetracker.R;
import com.binancetracker.databinding.PiechartFragmentBinding;
import com.binancetracker.ui.main.MainViewModel;
import com.github.mikephil.charting.components.Legend;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PieChartFragment extends Fragment {

    private PiechartFragmentBinding fragmentBinding;
    private PieChartViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.piechart_fragment, container, false);
        viewModel = new ViewModelProvider(this).get(PieChartViewModel.class);
        createPieChart();
        return fragmentBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.onPause();
    }

    private void createPieChart() {
        fragmentBinding.chart1.setUsePercentValues(true);

        fragmentBinding.chart1.setDrawHoleEnabled(true);
        fragmentBinding.chart1.setHoleColor(Color.BLACK);
        fragmentBinding.chart1.setHoleRadius(58f);

        fragmentBinding.chart1.setTransparentCircleColor(Color.GRAY);
        fragmentBinding.chart1.setTransparentCircleAlpha(110);
        fragmentBinding.chart1.setTransparentCircleRadius(61f);

        fragmentBinding.chart1.setCenterTextColor(Color.WHITE);
        fragmentBinding.chart1.setCenterTextSize(11f);
        fragmentBinding.chart1.setCenterText("Asset");
        fragmentBinding.chart1.setDrawCenterText(true);

        fragmentBinding.chart1.setRotationAngle(0);
        // enable rotation of the chart by touch
        fragmentBinding.chart1.setRotationEnabled(true);
        fragmentBinding.chart1.setHighlightPerTapEnabled(false);
        // undo all highlights
        fragmentBinding.chart1.highlightValues(null);
        fragmentBinding.chart1.setEntryLabelTextSize(10f);
        fragmentBinding.chart1.setEntryLabelColor(Color.WHITE);
        fragmentBinding.chart1.setExtraOffsets(10, 15, 10, 15);


        Legend l = fragmentBinding.chart1.getLegend();
        l.setEnabled(false);

        viewModel.pieChartModel.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.pieData) {
                    fragmentBinding.chart1.post(() -> {
                        fragmentBinding.chart1.setData(viewModel.pieChartModel.getPieData());
                        fragmentBinding.chart1.setCenterText(viewModel.pieChartModel.getPiechartMidString());
                        fragmentBinding.chart1.invalidate();
                    });
                }
            }
        });
    }
}
