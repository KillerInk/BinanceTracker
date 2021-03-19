package com.binancetracker.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ConvertingUtil
{
    public static double trimDoubleToPlaces(double in, int places)
    {
        if (in == 0)
            return 0;
        try {
            return new BigDecimal(in).setScale(places, RoundingMode.HALF_EVEN).doubleValue();
        }
        catch (java.lang.NumberFormatException ex)
        {

        }
        return 0;
    }

    private static DecimalFormat decimalFormat = new DecimalFormat("0");

    public static String trimDecimalFormatToString(double in, int places)
    {
        decimalFormat.setMaximumFractionDigits(places);
        return decimalFormat.format(in);
    }
}
