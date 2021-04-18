 package com.binancetracker.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.binancetracker.BR;
import com.binancetracker.MainActivity;
import com.binancetracker.R;
import com.binancetracker.databinding.MainFragmentBinding;
import com.binancetracker.ui.linechart.LineChartFragment;
import com.binancetracker.ui.piechart.PieChartFragment;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private CustomAdapter customAdapter;
    private MainFragmentBinding mainFragmentBinding;
    private boolean showLineChart = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);


        mainFragmentBinding.imageButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                if (navController.getCurrentDestination().getId() != R.id.keyFragment) {
                    navController.navigate(R.id.action_mainFragment_to_keyFragment);
                }
            }
        });

        mainFragmentBinding.imageButtonDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                if (navController.getCurrentDestination().getId() != R.id.dataBaseFragment) {
                    navController.navigate(R.id.action_mainFragment_to_dataBaseFragment);
                }
            }
        });

        mainFragmentBinding.imageButtonPieLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showLineChart)
                {
                    showLineChart = true;
                    mainFragmentBinding.imageButtonPieLine.setImageResource(R.mipmap.line_chart);
                    loadLineChart();
                }
                else
                {
                    showLineChart = false;
                    mainFragmentBinding.imageButtonPieLine.setImageResource(R.mipmap.pie);
                    loadPieChart();
                }
            }
        });

        customAdapter = new CustomAdapter();
        mainFragmentBinding.recyclerViewBalance.setAdapter(customAdapter);
        mainFragmentBinding.recyclerViewBalance.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.balances.observe(getViewLifecycleOwner(), new Observer<Collection<AssetModel>>() {
            @Override
            public void onChanged(Collection<AssetModel> strings) {
                customAdapter.setLocalDataSet(strings);
            }
        });
        loadPieChart();
        return mainFragmentBinding.getRoot();
    }

    private void loadPieChart() {
        loadChart(new PieChartFragment());
    }

    private void loadLineChart()
    {
        loadChart(new LineChartFragment());
    }

    private Fragment chartfragment;
    private void loadChart(Fragment fragment)
    {
        FragmentManager fragmentManager = getChildFragmentManager();
        if (chartfragment != null)
        {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(chartfragment);
            transaction.commit();
        }
        chartfragment = fragment;

        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.chartholder, fragment);
        fragmentTransaction.commit();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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