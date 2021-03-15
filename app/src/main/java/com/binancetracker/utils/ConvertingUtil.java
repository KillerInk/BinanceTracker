package com.binancetracker.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ConvertingUtil
{
    public static double trimDoubleToPlaces(double in, int places)
    {
        return new BigDecimal(in).setScale(places, RoundingMode.HALF_EVEN).doubleValue();
    }

    private static DecimalFormat decimalFormat = new DecimalFormat("0");

    public static String trimDecimalFormatToString(double in, int places)
    {
        decimalFormat.setMaximumFractionDigits(places);
        return decimalFormat.format(in);
    }
}
