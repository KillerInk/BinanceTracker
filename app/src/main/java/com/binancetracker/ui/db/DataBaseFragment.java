package com.binancetracker.ui.db;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.binancetracker.R;
import com.binancetracker.databinding.DatabaseFragmentBinding;

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
        databaseFragmentBinding.buttonStartSyncHistorOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseViewModel.startSyncTradeHistory();
            }
        });

        databaseFragmentBinding.buttonStartSyncDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseViewModel.startSyncDeposits();
            }
        });

        databaseFragmentBinding.buttonCalctrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseViewModel.calcTrades();
            }
        });

        databaseFragmentBinding.buttonStartSyncWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseViewModel.startSyncWithdraws();
            }
        });
        return databaseFragmentBinding.getRoot();
    }
}
