package com.binancetracker.ui.key;

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
import com.binancetracker.databinding.KeyFragmentBinding;

public class KeyFragment extends Fragment {
    private KeyViewModel mViewModel;
    private KeyFragmentBinding keyFragmentBinding;

    public static KeyFragment newInstance() {
        return new KeyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        keyFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.key_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(KeyViewModel.class);
        mViewModel.loadKeys();
        keyFragmentBinding.setKeyviemodel(mViewModel);
        keyFragmentBinding.buttonSaveKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.save();
            }
        });
        keyFragmentBinding.buttonTestkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.testKey();
            }
        });
        return keyFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }
}
