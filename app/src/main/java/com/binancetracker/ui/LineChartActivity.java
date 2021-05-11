package com.binancetracker.ui;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.binancetracker.R;
import com.binancetracker.databinding.LinechartActivityBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LineChartActivity extends FullScreenActivity {

    private LinechartActivityBinding linechartActivityBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linechartActivityBinding = DataBindingUtil.setContentView(this, R.layout.linechart_activity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
