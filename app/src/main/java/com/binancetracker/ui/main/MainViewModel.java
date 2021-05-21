package com.binancetracker.ui.main;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.binancetracker.repo.AssetRepo;
import com.binancetracker.repo.room.entity.AssetModel;

import java.util.Collection;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final String TAG = MainViewModel.class.getSimpleName();
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
        Log.v(TAG, "onResume");
        assetRepo.setAssetEventListner(assetEvent);
        assetRepo.onResume();
    }

    public void onPause()
    {
        Log.v(TAG, "onPause");
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