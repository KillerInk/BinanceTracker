package com.binancetracker.ui.main;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.binancetracker.repo.AssetRepo;
import com.binancetracker.utils.Settings;

import java.util.Collection;


public class MainViewModel extends ViewModel implements AssetRepo.AssetEvent {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private AssetRepo assetRepo;
    public MutableLiveData<Collection<AssetModel>> balances;
    private Handler handler;
    public final PieChartModel pieChartModel;
    private final Handler piechartUpdateHandler;

    public MainViewModel()
    {
        piechartUpdateHandler = new Handler(Looper.getMainLooper());
        assetRepo = new AssetRepo();
        handler = new Handler(Looper.getMainLooper());
        balances = new MutableLiveData<>();
        pieChartModel = new PieChartModel();
    }

    public void onResume()
    {
        if (Settings.getInstance().getSECRETKEY().equals("") || Settings.getInstance().getKEY().equals(""))
            return;
        assetRepo.setAssetEventListner(this);
        assetRepo.onResume();
        piechartUpdateHandler.postDelayed(updatePieChart,1000);

    }

    public void onPause()
    {
        piechartUpdateHandler.removeCallbacks(updatePieChart);
        assetRepo.setAssetEventListner(null);
        assetRepo.onPause();
    }

    private void applyHasmapToLiveData() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                balances.setValue(assetRepo.getAssetModelHashMap().values());
            }
        });
    }

    private Runnable updatePieChart = new Runnable() {
        @Override
        public void run() {
            pieChartModel.setPieChartData(assetRepo.getAssetModelHashMap());
            piechartUpdateHandler.postDelayed(updatePieChart,1000);
        }
    };

    @Override
    public void onAssetChanged() {
        applyHasmapToLiveData();
    }
}