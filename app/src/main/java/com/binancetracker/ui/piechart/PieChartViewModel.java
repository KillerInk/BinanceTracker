package com.binancetracker.ui.piechart;

import androidx.lifecycle.ViewModel;

import com.binancetracker.repo.AssetRepo;
import com.binancetracker.utils.Settings;

import javax.inject.Inject;

public class PieChartViewModel extends ViewModel {
    public final PieChartModel pieChartModel;

    @Inject
    public PieChartViewModel(AssetRepo assetRepo, Settings settings)
    {
        pieChartModel = new PieChartModel(assetRepo,settings);
    }
}
