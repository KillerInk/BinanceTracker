package com.binancetracker.ui.main;

import android.graphics.Color;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.binancetracker.BR;
import com.binancetracker.repo.AssetRepo;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.PortofolioHistory;
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

    public void setData()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, List<Entry>> allentries = getAllLast30DayEntries();
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
        HashMap<String, List<Entry>> entrysList = new HashMap<>();
        Date start = new Date((System.currentTimeMillis()/1000) *1000);
        start.setDate(start.getDate() - 180);
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);
        Date end = new Date(start.getTime());
        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);
        long today = System.currentTimeMillis();
        List<Entry> totalValueEntries = new ArrayList<>();
        while (end.getTime() < today)
        {
            long s = start.getTime();
            long e = end.getTime();
            List<PortofolioHistory> histories = SingletonDataBase.appDatabase.portofolioHistoryDao().getByTimeRange(s,e);
            for (PortofolioHistory portofolioHistory: histories)
            {
                if (!entrysList.containsKey(portofolioHistory.asset))
                    entrysList.put(portofolioHistory.asset,new ArrayList<>());
            }
            double position = 0;
            for (PortofolioHistory portofolioHistory: histories)
            {
                float pos = (float) (portofolioHistory.amount * portofolioHistory.price);
                position += pos;
                if(pos > 1f)
                    entrysList.get(portofolioHistory.asset).add(new Entry(start.getTime(), pos));
            }
            findMinMax(position);
            totalValueEntries.add(new Entry(start.getTime(), (float) position));
            end.setDate(end.getDate()+1);
            start.setDate(start.getDate()+1);
        }
        max = (float) (max + ((float)max*0.5));
        entrysList.put("TOTAL",totalValueEntries);
        return entrysList;
    }

    private void findMinMax(double position) {
        if (max == 0 && min == 0) {
            max = (float) position;
            min = (float) position;
        }
        if (position > max)
            max = (float) position;
        else if (position < min)
            min = (float) position;
    }

    private List<Entry> getLast30Days()
    {
        Date start = new Date((System.currentTimeMillis()/1000) *1000);
        start.setDate(start.getDate() - 30);
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);
        Date end = new Date(start.getTime());
        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);
        long today = System.currentTimeMillis();
        List<Entry> entries = new ArrayList<>();
        while (end.getTime() < today)
        {
            long s = start.getTime();
            long e = end.getTime();
            List<PortofolioHistory> histories = SingletonDataBase.appDatabase.portofolioHistoryDao().getByTimeRange(s,e);
            double position = 0;
            for (PortofolioHistory portofolioHistory : histories)
            {
                position += portofolioHistory.amount * portofolioHistory.price;
            }
            findMinMax(position);
            entries.add(new Entry(start.getTime(), (float) position));
            end.setDate(end.getDate()+1);
            start.setDate(start.getDate()+1);
        }
        max = (float) (max + ((float)max*0.4));
        return entries;
    }
}
