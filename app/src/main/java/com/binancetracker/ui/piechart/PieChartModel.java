package com.binancetracker.ui.piechart;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.binancetracker.BR;
import com.binancetracker.repo.AssetRepo;
import com.binancetracker.repo.room.entity.AssetModel;
import com.binancetracker.utils.ConvertingUtil;
import com.binancetracker.utils.Settings;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;

public class PieChartModel extends BaseObservable {
    private final String TAG = PieChartModel.class.getSimpleName();
    private ArrayList<PieEntry> entries;
    private ArrayList<Integer> colors;
    private PieData pieData;
    private PieDataSet dataSet;
    private String piechartMidString;
    private final Handler piechartUpdateHandler;
    private final AssetRepo assetRepo;
    private Settings settings;

    public void setPieData(PieData pieData)
    {
        //this.pieData = pieData;
        notifyPropertyChanged(BR.pieData);
    }

    @Bindable
    public PieData getPieData() {
        return pieData;
    }

    public String getPiechartMidString() {
        return piechartMidString;
    }

    public PieChartModel(AssetRepo assetRepo,Settings settings)
    {
        this.assetRepo = assetRepo;
        this.settings = settings;
        piechartUpdateHandler = new Handler(Looper.getMainLooper());
        entries = new ArrayList<>();
        colors = createColors();
        dataSet = new PieDataSet(entries, "Assets");
        dataSet.setDrawIcons(false);
        dataSet.setColors(colors);
        dataSet.setSliceSpace(5f);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        //percent value outside the pie
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLineColor(Color.WHITE);
        pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(20f);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setDataSet(dataSet);
    }

    public void onResume()
    {
        Log.v(TAG,"onResume");
        piechartUpdateHandler.postDelayed(updatePieChart,1000);
    }

    public void onPause()
    {
        Log.v(TAG,"onPause");
        piechartUpdateHandler.removeCallbacks(updatePieChart);
    }

    private Runnable updatePieChart = new Runnable() {
        @Override
        public void run() {

            //Log.v(TAG,"updatePieChart");
            setPieChartData(assetRepo.getAssetModelHashMap());
            piechartUpdateHandler.postDelayed(updatePieChart,1000);
        }
    };

    public void setPieChartData(HashMap<String, AssetModel> strings)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                double smallvaluescount = 0;
                double totalval = 0d;
                double usdt = 0d;
                double choosenCurency = 0d;
                if (entries.size() == 0) {
                    for (AssetModel a : strings.values()) {
                        try {
                            totalval = a.getTotalValuePrice();
                            usdt += a.getTotalValuePrice();
                            choosenCurency += a.getTotalValueChoosenAssetPrice();
                        }catch (NullPointerException ex)
                        {
                            ex.printStackTrace();
                            totalval = 0;
                        }
                        if (totalval > 10d) {
                            PieEntry entry = new PieEntry((float) totalval, a.getAssetName());
                            entries.add(entry);
                        } else if (totalval > 0d) {
                            smallvaluescount += totalval;
                        }
                    }
                    if (smallvaluescount > 1d)
                    {
                        PieEntry small = new PieEntry((float) smallvaluescount, "Small");
                        entries.add(small);
                    }
                }
                else
                {
                    PieEntry small = null;
                    for (PieEntry entry : entries)
                    {
                        AssetModel asset = strings.get(entry.getLabel());
                        if (asset != null)
                        {
                            try {
                                totalval = asset.getTotalValuePrice();
                                usdt += asset.getTotalValuePrice();
                                choosenCurency += asset.getTotalValueChoosenAssetPrice();
                            }catch (NullPointerException ex)
                            {
                                ex.printStackTrace();
                                totalval = 0;
                            }
                            if (totalval > 10d)
                                entry.setY((float)totalval);
                            else if (totalval > 0d) {
                                smallvaluescount += totalval;
                            }
                            if (entry.getLabel().equals("Small"))
                                small = entry;
                        }
                    }
                    if (smallvaluescount > 0d && small != null)
                    {
                        small.setY((float) smallvaluescount);
                    }
                    else if(smallvaluescount < 1 && small != null)
                        entries.remove(small);
                }
                piechartMidString = "USDT:"+ ConvertingUtil.getDoubleString2F(usdt) + "\n" + settings.getDefaultAsset()+":"+ConvertingUtil.getDoubleString2F(choosenCurency);
                setPieData(pieData);
            }
        }).start();


    }

    private ArrayList<Integer> createColors()
    {
        ArrayList<Integer> colors = new ArrayList<>();

       /* for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);



        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);*/

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        //colors.add(ColorTemplate.getHoloBlue());
        return colors;
    }
}
