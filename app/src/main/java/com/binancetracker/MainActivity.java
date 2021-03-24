package com.binancetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.View;

import com.binancetracker.databinding.MainActivityBinding;
import com.binancetracker.api.BinanceApi;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.utils.Settings;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding mainActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        mainActivityBinding.imageButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                if (navController.getCurrentDestination().getId() != R.id.keyFragment) {
                    //navController.popBackStack();
                    navController.navigate(R.id.keyFragment);
                }
                else {
                    navController.popBackStack();
                    navController.navigate(R.id.mainFragment);
                }
            }
        });

        mainActivityBinding.imageButtonDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                if (navController.getCurrentDestination().getId() != R.id.dataBaseFragment) {
                    //navController.popBackStack();
                    navController.navigate(R.id.dataBaseFragment);
                }
                else {
                    navController.popBackStack();
                    navController.navigate(R.id.mainFragment);
                }
            }
        });
        new Settings(getApplicationContext());
        SingletonDataBase.init(getApplicationContext());
        BinanceApi.getInstance().setKeys(Settings.getInstance().getKEY(),Settings.getInstance().getSECRETKEY());
    }

    @Override
    protected void onDestroy() {
        SingletonDataBase.close();
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}