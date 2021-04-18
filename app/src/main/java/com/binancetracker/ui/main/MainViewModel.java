package com.binancetracker.ui.main;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.binancetracker.repo.AssetRepo;

import java.util.Collection;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private AssetRepo assetRepo;
    public MutableLiveData<Collection<AssetModel>> balances;
    private Handler handler;

    @Inject
    public MainViewModel(AssetRepo assetRepo)
    {
        this.assetRepo = assetRepo;
        handler = new Handler(Looper.getMainLooper());
        balances = new MutableLiveData<>();
    }

    public void onResume()
    {
        if (assetRepo.getSettings().getSECRETKEY().equals("") || assetRepo.getSettings().getKEY().equals(""))
            return;
        assetRepo.setAssetEventListner(assetEvent);
        assetRepo.onResume();
    }

    public void onPause()
    {
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

    private AssetRepo.AssetEvent assetEvent = new AssetRepo.AssetEvent() {
        @Override
        public void onAssetChanged() {
            applyHasmapToLiveData();
        }
    };


}