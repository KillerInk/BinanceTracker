package com.binancetracker;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.binancetracker.api.BinanceApi;
import com.binancetracker.databinding.MainActivityBinding;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.ui.FullScreenActivity;
import com.binancetracker.utils.Settings;

import java.util.Set;

import javax.inject.Inject;

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