package com.binancetracker.ui.linechart;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.binancetracker.repo.room.SingletonDataBase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LineChartViewModel extends ViewModel {
    private final String TAG = LineChartViewModel.class.getSimpleName();
    public final LineChartModel lineChartModel;

    private TimeToFetch timeToFetch = TimeToFetch.week;

    @Inject
    public LineChartViewModel(SingletonDataBase lineChartModel)
    {
        this.lineChartModel = new LineChartModel(lineChartModel);
    }

    public void onResume()
    {
        Log.v(TAG, "onResume lineChartModel.setData");
        //lineChartModel.setData(timeToFetch);
    }

    public void setTimeToFetch(TimeToFetch timeToFetch) {
        this.timeToFetch = timeToFetch;
        Log.v(TAG, "setTimeToFetch lineChartModel.setData");
        lineChartModel.setData(timeToFetch);
    }

    public void setAsset(String asset)
    {
        lineChartModel.setAsset(asset);
    }
}
