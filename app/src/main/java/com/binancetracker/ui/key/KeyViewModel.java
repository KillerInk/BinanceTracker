package com.binancetracker.ui.key;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.binancetracker.api.BinanceApi;
import com.binancetracker.utils.Settings;

public class KeyViewModel extends ViewModel {
    private static final String TAG = KeyViewModel.class.getSimpleName();
    public MutableLiveData<String> key = new MutableLiveData<>();
    public MutableLiveData<String> secret = new MutableLiveData<>();
    public MutableLiveData<String> testkeyresult = new MutableLiveData<>();

    public void loadKeys()
    {
        key.setValue(Settings.getInstance().getKEY());
        secret.setValue(Settings.getInstance().getSECRETKEY());
    }

    public void save()
    {
        Settings.getInstance().setKey(key.getValue());
        Settings.getInstance().setSecretKey(secret.getValue());
    }

    public void testKey()
    {
        BinanceApi.getInstance().setKeys(key.getValue(),secret.getValue());
        String res = BinanceApi.getInstance().testKey();
        Log.d(TAG,res);
        testkeyresult.postValue(res);
    }
}
