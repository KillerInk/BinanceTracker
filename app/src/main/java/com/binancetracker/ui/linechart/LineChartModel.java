package com.binancetracker.ui.linechart;

import android.graphics.Color;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.binancetracker.BR;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.PortofolioHistory;
import com.binancetracker.utils.MyTime;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LineChartModel extends BaseObservable
{
    private LineData data;
    private float max = 170f;
    private float min = 0f;
    public SingletonDataBase singletonDataBase;

    public LineChartModel(SingletonDataBase singletonDataBase)
    {
        this.singletonDataBase = singletonDataBase;
    }


    public void setData(LineData data) {
        this.data = data;
        notifyPropertyChanged(BR.data);
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }

    @Bindable
    public LineData getData() {
        return data;
    }

    public void setData(TimeToFetch timeToFetch)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, List<Entry>> allentries = null;
                switch (timeToFetch)
                {
                    case week:
                        allentries = getAllLast7DayEntries();
                        break;
                    case month:
                        allentries = getAllLast30DayEntries();
                        break;
                    case year:
                        allentries = getAllLast365DayEntries();
                }
                List<ILineDataSet> sets = getLineData(allentries);

                // create a data object with the data sets
                LineData data = new LineData(sets);
                data.setValueTextColor(Color.BLACK);
                data.setValueTextSize(9f);
                setData(data);
            }
        }).start();
    }

    private List<ILineDataSet> getLineData(HashMap<String, List<Entry>> allentries)
    {
        List<ILineDataSet> dataSets = new ArrayList<>();
        int i = 0;
        for (String s: allentries.keySet())
        {
            LineDataSet set1 = new LineDataSet(allentries.get(s), s);
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(getRandomColor());
            set1.setValueTextColor(ColorTemplate.getHoloBlue());
            set1.setLineWidth(1f);
            set1.setDrawCircles(false);
            set1.setDrawValues(false);
            set1.setFillAlpha(85);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);
            dataSets.add(set1);
        }

        return dataSets;
    }

    private int getRandomColor()
    {
        Random rand = new Random();
        float r = rand.nextInt(255) / 2f + 0.5f;
        float g = rand.nextInt(255) / 2f + 0.5f;
        float b = rand.nextInt(255) / 2f + 0.5f;
        return Color.rgb(r,g,b);
    }

    private  HashMap<String, List<Entry>> getAllLast30DayEntries() {
        //List<List<Entry>> entrysList = new ArrayList<>();
        return getDaysEntries(30);
    }

    private  HashMap<String, List<Entry>> getAllLast7DayEntries() {
        //List<List<Entry>> entrysList = new ArrayList<>();
        return getDaysEntries(7);
    }

    private  HashMap<String, List<Entry>> getAllLast365DayEntries() {
        //List<List<Entry>> entrysList = new ArrayList<>();
        return getDaysEntries(365);
    }

    private HashMap<String, List<Entry>> getDaysEntries(int days) {
        min =0;
        max = 0;
        HashMap<String, List<Entry>> entrysList = new HashMap<>();
        MyTime start = new MyTime().setDays(-days).setDayToBegin();
        MyTime end = new MyTime(start.getTime()).setDayToEnd();
        long today = System.currentTimeMillis();
        List<Entry> totalValueEntries = new ArrayList<>();
        while (end.getTime() < today)
        {
            long s = start.getTime();
            long e = end.getTime();
            List<PortofolioHistory> histories = singletonDataBase.appDatabase.portofolioHistoryDao().getByTimeRange(s,e);
            for (PortofolioHistory portofolioHistory: histories)
            {
                if (!entrysList.containsKey(portofolioHistory.asset))
                    entrysList.put(portofolioHistory.asset,new ArrayList<>());
            }
            double position = 0;
            for (PortofolioHistory portofolioHistory: histories)
            {
                float pos = (float) (portofolioHistory.amount * portofolioHistory.price);

                if(pos > 10f) {
                    position += pos;
                    Entry entry = new Entry(start.getTime(), pos);
                    entry.setData(portofolioHistory.asset);
                    entrysList.get(portofolioHistory.asset).add(entry);
                    findMinMax(position,pos);
                }
            }

            Entry entry = new Entry(start.getTime(), (float) position);
            entry.setData("Total");
            totalValueEntries.add(entry);
            end.setDays(1);
            start.setDays(1);
        }
        max = (float) (max + ((float)max*0.3 ));
        entrysList.put("TOTAL",totalValueEntries);
        return entrysList;
    }

    private void findMinMax(double position,double assetpos) {
        if (max == 0 && min == 0) {
            max = (float) position;
            min = (float) position;
        }
        if (position > max)
            max = (float) position;
        else if (position < min)
            min = (float) position;
        if (assetpos < min)
            min = (float) assetpos;
    }

}
