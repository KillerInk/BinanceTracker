package com.binancetracker.ui.linechart;

import androidx.lifecycle.ViewModel;

import com.binancetracker.room.SingletonDataBase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LineChartViewModel extends ViewModel {
    public final LineChartModel lineChartModel;

    @Inject
    public LineChartViewModel(SingletonDataBase lineChartModel)
    {
        this.lineChartModel = new LineChartModel(lineChartModel);
    }

    public void onResume()
    {
        lineChartModel.setData();
    }
}
