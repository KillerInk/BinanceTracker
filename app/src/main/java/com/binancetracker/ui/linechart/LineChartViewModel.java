package com.binancetracker.ui.linechart;

import androidx.lifecycle.ViewModel;

import com.binancetracker.room.SingletonDataBase;

import java.sql.Time;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LineChartViewModel extends ViewModel {
    public final LineChartModel lineChartModel;

    private TimeToFetch timeToFetch = TimeToFetch.week;

    @Inject
    public LineChartViewModel(SingletonDataBase lineChartModel)
    {
        this.lineChartModel = new LineChartModel(lineChartModel);
    }

    public void onResume()
    {
        lineChartModel.setData(timeToFetch);
    }

    public void setTimeToFetch(TimeToFetch timeToFetch) {
        this.timeToFetch = timeToFetch;
        lineChartModel.setData(timeToFetch);
    }
}
