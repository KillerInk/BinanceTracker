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
    public final LineChartModel lineChartModel;


    public MainViewModel()
    {
        assetRepo = new AssetRepo();
        handler = new Handler(Looper.getMainLooper());
        balances = new MutableLiveData<>();
        pieChartModel = new PieChartModel(assetRepo);
        lineChartModel = new LineChartModel();
    }

    public void onResume()
    {
        if (Settings.getInstance().getSECRETKEY().equals("") || Settings.getInstance().getKEY().equals(""))
            return;
        assetRepo.setAssetEventListner(this);
        assetRepo.onResume();
        pieChartModel.onResume();
        lineChartModel.setData();
    }

    public void onPause()
    {
        pieChartModel.onPause();
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



    @Override
    public void onAssetChanged() {
        applyHasmapToLiveData();
    }
}