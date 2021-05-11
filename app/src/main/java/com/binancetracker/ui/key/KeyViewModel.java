package com.binancetracker.ui.key;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.binancetracker.MyApplication;
import com.binancetracker.R;
import com.binancetracker.utils.Settings;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class KeyViewModel extends ViewModel {
    private static final String TAG = KeyViewModel.class.getSimpleName();
    public MutableLiveData<String> key = new MutableLiveData<>();
    public MutableLiveData<String> secret = new MutableLiveData<>();
    public MutableLiveData<String> testkeyresult = new MutableLiveData<>();
    public MutableLiveData<Integer> fiatspinner = new MutableLiveData<>();
    private Settings settings;

    @Inject
    public KeyViewModel(Settings settings)
    {
        this.settings = settings;
    }

    public void loadKeys()
    {
        key.setValue(settings.getKEY());
        secret.setValue(settings.getSECRETKEY());
        String[] fiats = MyApplication.getStringArrayFromRes(R.array.fiats);
        String fiat = settings.getDefaultAsset();
        for (int i = 0; i < fiats.length; i++)
        {
            if (fiat.equals(fiats[i]))
                fiatspinner.setValue(i);
        }
    }

    public void save()
    {
        settings.setKey(key.getValue());
        settings.setSecretKey(secret.getValue());
        String[] fiats = MyApplication.getStringArrayFromRes(R.array.fiats);
        settings.setDefaultAsset(fiats[fiatspinner.getValue()]);
    }

    public void testKey()
    {
       /* String res = binanceApi.testKey();
        Log.d(TAG,res);
        testkeyresult.postValue(res);*/
    }
}
