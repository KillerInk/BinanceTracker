package com.binancetracker.ui.main;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binancetracker.R;
import com.binancetracker.databinding.MainFragmentBinding;

import java.util.Collection;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private CustomAdapter customAdapter;
    MainFragmentBinding mainFragmentBinding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        customAdapter = new CustomAdapter();
        mainFragmentBinding.recyclerViewBalance.setAdapter(customAdapter);
        mainFragmentBinding.recyclerViewBalance.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.balances.observe(getViewLifecycleOwner(), new Observer<Collection<AssetModel>>() {
            @Override
            public void onChanged(Collection<AssetModel> strings) {
                customAdapter.setLocalDataSet(strings);
            }
        });
        return mainFragmentBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

}