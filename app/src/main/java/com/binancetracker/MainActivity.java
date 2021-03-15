package com.binancetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.View;

import com.binancetracker.databinding.MainActivityBinding;
import com.binancetracker.api.BinanceApi;
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
        new Settings(getApplicationContext());
        BinanceApi.getInstance().setKeys(Settings.getInstance().getKEY(),Settings.getInstance().getSECRETKEY());
    }

}