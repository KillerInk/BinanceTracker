package com.binancetracker.api.runnable;

import android.util.Log;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binancetracker.api.DownloadTradeHistory;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.dao.HistoryTradeDao;
import com.binancetracker.room.dao.MarketDao;
import com.binancetracker.room.entity.HistoryTrade;
import com.binancetracker.room.entity.Market;

import java.util.List;

public class DownloadTradeFullHistoryRunner extends ClientFactoryRunner {


    private final String TAG = DownloadTradeFullHistoryRunner.class.getSimpleName();

    private DownloadTradeHistory.TradeHistoryEvent tradeHistoryEventListner;
    private int i;
    private int tradescounter = 0;

    private final int LIMIT = 1000;

    public DownloadTradeFullHistoryRunner(BinanceApiClientFactory clientFactory) {
        super(clientFactory);
    }

    public void setTradeHistoryEventListner(DownloadTradeHistory.TradeHistoryEvent tradeHistoryEventListner) {
        this.tradeHistoryEventListner = tradeHistoryEventListner;
    }

    @Override
    public void run() {
        Log.d(TAG,"getFullHistory");
        BinanceApiRestClient client = clientFactory.newRestClient();
        List<SymbolInfo> info = client.getExchangeInfo().getSymbols();
        MarketDao marketDao = SingletonDataBase.binanceDatabase.marketDao();
        List<Market> markets = marketDao.getAll();
        Log.d(TAG,"clear Market DB");
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
            Log.d(TAG,"add Market to DB " + market.symbol);
            marketDao.insert(market);
        }
        markets = marketDao.getAll();
        if (tradeHistoryEventListner != null)
            tradeHistoryEventListner.onSyncStart(markets.size());
        HistoryTradeDao historyTradeDao = SingletonDataBase.binanceDatabase.historyTradeDao();
        i = 0;
        Log.d(this.getClass().getSimpleName(),"startParsing markets");
        for (Market m: markets) {
            String pair = m.baseAsset+m.quoteAsset;
            tradescounter = 0;
            Log.d(this.getClass().getSimpleName(),"download Trades for: " + pair);
            if (tradeHistoryEventListner != null)
                tradeHistoryEventListner.onSyncUpdate(i++);
            getAllHistoryforPair(client,historyTradeDao,pair,0);
            Log.d(this.getClass().getSimpleName(),"download done for: " + pair + " found trades:" + tradescounter);
        }
        Log.d(this.getClass().getSimpleName(),"finished download fullHistory");
        if (tradeHistoryEventListner != null)
            tradeHistoryEventListner.onSyncEnd();
    }

    private void getAllHistoryforPair(BinanceApiRestClient client,HistoryTradeDao historyTradeDao,String pair, long id)
    {
        List<Trade> histtrades = client.getMyTrades(pair, id,LIMIT);
        tradescounter += histtrades.size();
        insertHistory(client, historyTradeDao, pair, histtrades);
    }

    protected void insertHistory(BinanceApiRestClient client, HistoryTradeDao historyTradeDao, String pair, List<Trade> histtrades) {
        for (Trade t : histtrades)
        {
            insertHistotrade(historyTradeDao, t);
        }
        if (histtrades.size() > 1 && histtrades.size() == LIMIT)
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
