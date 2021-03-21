package com.binancetracker.utils;

import com.binancetracker.MyApplication;
import com.binancetracker.R;

public class MarketPair {
    //last item
    private String baseAsset;
    //first item
    private String quoteAsset;

    public MarketPair(String symbol)
    {
        String mergedmarkets[] = MyApplication.getStringArrayFromRes(R.array.allmarkets);
        for (String s : mergedmarkets)
        {
            if (symbol.endsWith(s))
            {
                baseAsset = s;
                quoteAsset = symbol.replace(s,"");
                break;
            }
        }
    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }
}
