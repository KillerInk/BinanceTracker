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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.binancetracker.BR;
import com.binancetracker.R;
import com.binancetracker.databinding.MainFragmentBinding;
import com.github.mikephil.charting.components.Legend;

import java.util.Collection;

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
        customAdapter = new CustomAdapter();
        mainFragmentBinding.recyclerViewBalance.setAdapter(customAdapter);
        mainFragmentBinding.recyclerViewBalance.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.balances.observe(getViewLifecycleOwner(), new Observer<Collection<AssetModel>>() {
            @Override
            public void onChanged(Collection<AssetModel> strings) {
                customAdapter.setLocalDataSet(strings);
            }
        });

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

        return mainFragmentBinding.getRoot();
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