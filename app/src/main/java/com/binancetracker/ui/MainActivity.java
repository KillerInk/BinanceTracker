package com.binancetracker.ui;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.binancetracker.R;
import com.binancetracker.databinding.MainActivityBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends FullScreenActivity {

    private MainActivityBinding mainActivityBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}