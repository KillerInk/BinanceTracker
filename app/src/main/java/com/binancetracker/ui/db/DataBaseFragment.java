package com.binancetracker.ui.db;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.binancetracker.R;
import com.binancetracker.databinding.DatabaseFragmentBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DataBaseFragment extends Fragment {
    private DatabaseFragmentBinding databaseFragmentBinding;
    private DataBaseViewModel dataBaseViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        databaseFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.database_fragment, container, false);
        dataBaseViewModel = new ViewModelProvider(this).get(DataBaseViewModel.class);
        databaseFragmentBinding.setViewmodel(dataBaseViewModel);

        dataBaseViewModel.errorMSG.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                DataBaseFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), dataBaseViewModel.errorMSG.get(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return databaseFragmentBinding.getRoot();
    }
}
