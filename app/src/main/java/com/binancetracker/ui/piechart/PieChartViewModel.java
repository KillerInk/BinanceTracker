package com.binancetracker.ui.piechart;

import androidx.lifecycle.ViewModel;

import com.binancetracker.repo.AssetRepo;
import com.binancetracker.utils.Settings;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PieChartViewModel extends ViewModel {
    public final PieChartModel pieChartModel;

    @Inject
    public PieChartViewModel(AssetRepo assetRepo, Settings settings)
    {
        pieChartModel = new PieChartModel(assetRepo,settings);
    }

    public void onResume()
    {
        pieChartModel.onResume();
    }

    public void onPause()
    {
        pieChartModel.onPause();
    }
}
