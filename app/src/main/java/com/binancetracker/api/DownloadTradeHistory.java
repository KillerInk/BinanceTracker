package com.binancetracker.api;

import android.util.Log;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.dao.HistoryTradeDao;
import com.binancetracker.room.dao.MarketDao;
import com.binancetracker.room.entity.HistoryTrade;
import com.binancetracker.room.entity.Market;

import java.util.List;

public class DownloadTradeHistory {

    public interface TradeHistoryEvent
    {
        void onSyncStart(int max_markets);
        void onSyncUpdate(int currentmarket);
        void onSyncEnd();
    }

    private BinanceApiClientFactory clientFactory;
    private TradeHistoryEvent historyEvent;

    public DownloadTradeHistory(BinanceApiClientFactory clientFactory)
    {
        this.clientFactory = clientFactory;
    }

    public void setHistoryEvent(TradeHistoryEvent historyEvent) {
        this.historyEvent = historyEvent;
    }

    public void getFullHistory()
    {
        Log.d(this.getClass().getSimpleName(),"getFullHistory");
        BinanceApiRestClient client = clientFactory.newRestClient();
        List<SymbolInfo> info = client.getExchangeInfo().getSymbols();
        MarketDao marketDao = SingletonDataBase.appDatabase.marketDao();
        List<Market> markets = marketDao.getAll();
        Log.d(this.getClass().getSimpleName(),"clear Market DB");
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
            Log.d(this.getClass().getSimpleName(),"add Market to DB " + market.symbol);
            marketDao.insert(market);
        }
        markets = marketDao.getAll();
        if (historyEvent != null)
            historyEvent.onSyncStart(markets.size());
        HistoryTradeDao historyTradeDao = SingletonDataBase.appDatabase.historyTradeDao();
        int i = 0;
        Log.d(this.getClass().getSimpleName(),"startParsing markets");
        for (Market m: markets) {
            String pair = m.baseAsset+m.quoteAsset;
            Log.d(this.getClass().getSimpleName(),"download Trades for: " + pair);
            if (historyEvent != null)
                historyEvent.onSyncUpdate(i++);
            getAllHistoryforPair(client,historyTradeDao,pair,0);
            Log.d(this.getClass().getSimpleName(),"download done for: " + pair);
        }
        Log.d(this.getClass().getSimpleName(),"finished download fullHistory");
        if (historyEvent != null)
            historyEvent.onSyncEnd();
        //List<HistoryTrade> historyTrades = historyTradeDao.getAll();
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
        historyTrade.buyer = t.isBuyer();
        historyTrade.maker = t.isMaker();
        historyTradeDao.insert(historyTrade);
    }
}
