package com.binancetracker.api;

import android.icu.text.UFormat;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.domain.account.TradeHistoryItem;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binancetracker.room.AppDatabase;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.dao.HistoryTradeDao;
import com.binancetracker.room.dao.MarketDao;
import com.binancetracker.room.entity.HistoryTrade;
import com.binancetracker.room.entity.Market;

import java.util.List;

public class TradeHistory {

    public interface TradeHistoryEvent
    {
        void onSyncStart(int max_markets);
        void onSyncUpdate(int currentmarket);
        void onSyncEnd();
    }

    private BinanceApiClientFactory clientFactory;
    private TradeHistoryEvent historyEvent;

    public TradeHistory(BinanceApiClientFactory clientFactory)
    {
        this.clientFactory = clientFactory;
    }

    public void setHistoryEvent(TradeHistoryEvent historyEvent) {
        this.historyEvent = historyEvent;
    }

    public void getFullHistory()
    {
        BinanceApiRestClient client = clientFactory.newRestClient();
        List<SymbolInfo> info = client.getExchangeInfo().getSymbols();
        MarketDao marketDao = SingletonDataBase.appDatabase.marketDao();
        List<Market> markets = marketDao.getAll();

        for (Market market : markets) {
            marketDao.delete(market);
        }
        for (SymbolInfo i : info)
        {
            Market market = new Market();
            market.baseAsset = i.getBaseAsset();
            market.baseAssetPrecision = i.getBaseAssetPrecision();
            market.icebergAllowed = i.isIcebergAllowed();
            market.isMarginTradingAllowed = i.isMarginTradingAllowed();
            market.isSpotTradingAllowed = i.isSpotTradingAllowed();
            market.ocoAllowed = i.isOcoAllowed();
            market.quoteAsset = i.getQuoteAsset();
            market.quotePrecision = i.getQuotePrecision();
            market.quoteOrderQtyMarketAllowed = i.isQuoteOrderQtyMarketAllowed();
            //market.status = i.getStatus();
            market.symbol = i.getSymbol();
            marketDao.insert(market);
        }
        markets = marketDao.getAll();
        if (historyEvent != null)
            historyEvent.onSyncStart(markets.size());
        HistoryTradeDao historyTradeDao = SingletonDataBase.appDatabase.historyTradeDao();
        int i = 0;
        for (Market m: markets) {
            String pair = m.baseAsset+m.quoteAsset;
            if (historyEvent != null)
                historyEvent.onSyncUpdate(i++);
            getAllHistoryforPair(client,historyTradeDao,pair,0);
        }
        if (historyEvent != null)
            historyEvent.onSyncEnd();
        List<HistoryTrade> historyTrades = historyTradeDao.getAll();
    }

    private void getAllHistoryforPair(BinanceApiRestClient client,HistoryTradeDao historyTradeDao,String pair, long id)
    {
        List<Trade> histtrades = client.getMyTrades(pair, id);
        for (Trade t : histtrades)
        {
            insertHistotrade(historyTradeDao, t);
        }
        if (histtrades.size() > 1 && histtrades.size() == 500)
            getAllHistoryforPair(client,historyTradeDao,pair,histtrades.get(histtrades.size()-1).getId());
    }

    private void insertHistotrade(HistoryTradeDao historyTradeDao, Trade t) {
        HistoryTrade historyTrade = new HistoryTrade();
        historyTrade.commission = t.getCommission();
        historyTrade.commissionAsset = t.getCommissionAsset();
        historyTrade.id = t.getId();
        historyTrade.price = t.getPrice();
        historyTrade.qty = t.getQty();
        historyTrade.quoteQty = t.getQuoteQty();
        historyTrade.time = t.getTime();
        historyTrade.symbol = t.getSymbol();
        historyTradeDao.insert(historyTrade);
    }
}
