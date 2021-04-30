package com.binancetracker.utils;

import java.text.DateFormat;
import java.util.Date;

public class MyTime {
    private Date date;

    public MyTime(long time)
    {
        setDate(time);
    }

    public MyTime()
    {
        setDate(System.currentTimeMillis());
    }

    private void setDate(long time)
    {
        date = new Date((time/1000) *1000);
    }

    public MyTime setDayToBegin()
    {
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return this;
    }

    public MyTime setDayToEnd()
    {
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return this;
    }

    public long getTime()
    {
        return date.getTime();
    }

    public MyTime setDays(int days)
    {
        date.setDate(date.getDate() +days);
        return this;
    }

    public MyTime setMinutes(int min)
    {
        date.setMinutes(date.getMinutes() + min);
        return this;
    }

    public String getString()
    {
        return DateFormat.getDateTimeInstance().format(date);
    }

    public long getUtcTime()
    {
        return date.getTime() - (date.getTimezoneOffset()*60*1000);
    }


}
