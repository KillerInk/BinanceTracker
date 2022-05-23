package com.binancetracker.repo.api.runnable.market;

import android.util.Log;

import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.api.runnable.JsonToDBConverter;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.dao.HistoryTradeDao;
import com.binancetracker.repo.room.dao.MarketDao;
import com.binancetracker.repo.room.entity.HistoryTradeEntity;
import com.binancetracker.repo.room.entity.Market;

import java.util.List;

public class DownloadTradeFullHistoryRunner extends ClientFactoryRunner<BinanceSpotApiClientFactory> {


    private final String TAG = DownloadTradeFullHistoryRunner.class.getSimpleName();

    private int i;
    private int tradescounter = 0;

    private final int LIMIT = 1000;

    public DownloadTradeFullHistoryRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory,singletonDataBase);
    }


    @Override
    public void processRun() {
        Log.d(TAG,"getFullHistory");
        singletonDataBase.binanceDatabase.historyTradeDao().deleteAll();
        BinanceApiSpotRestClient client = clientFactory.newRestClient();
        List<SymbolInfo> info = client.getExchangeInfo().getSymbols();
        MarketDao marketDao = singletonDataBase.binanceDatabase.marketDao();
        List<Market> markets = marketDao.getAll();
        Log.d(TAG,"clear Market DB");
        for (Market market : markets) {
            marketDao.delete(market);
        }
        for (SymbolInfo i : info)
        {
            Market market = JsonToDBConverter.getMarketEntity(i);
            Log.d(TAG,"add Market to DB " + market.symbol);
            marketDao.insert(market);
        }
        markets = marketDao.getAll();
        fireOnSyncStart(markets.size());
        HistoryTradeDao historyTradeDao = singletonDataBase.binanceDatabase.historyTradeDao();
        i = 0;
        Log.d(this.getClass().getSimpleName(),"startParsing markets");
        for (Market m: markets) {
            String pair = m.baseAsset+m.quoteAsset;
            tradescounter = 0;
            Log.d(this.getClass().getSimpleName(),"download Trades for: " + pair);
            fireOnSyncUpdate(i++, pair);
            getAllHistoryforPair(client,historyTradeDao,pair,0);
            Log.d(this.getClass().getSimpleName(),"download done for: " + pair + " found trades:" + tradescounter);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(this.getClass().getSimpleName(),"finished download fullHistory");
        fireOnSyncEnd();
    }



    private void getAllHistoryforPair(BinanceApiSpotRestClient client,HistoryTradeDao historyTradeDao,String pair, long id)
    {
        List<Trade> histtrades = client.getMyTrades(pair, id,LIMIT);
        tradescounter += histtrades.size();
        insertHistory(client, historyTradeDao, pair, histtrades);
    }

    protected void insertHistory(BinanceApiSpotRestClient client, HistoryTradeDao historyTradeDao, String pair, List<Trade> histtrades) {
        for (Trade t : histtrades)
        {
            insertHistotrade(historyTradeDao, t);
        }
        if (histtrades.size() > 1 && histtrades.size() == LIMIT)
            getAllHistoryforPair(client,historyTradeDao,pair,histtrades.get(histtrades.size()-1).getId());
    }

    private void insertHistotrade(HistoryTradeDao historyTradeDao, Trade t) {
        HistoryTradeEntity historyTradeEntity = new HistoryTradeEntity();
        historyTradeEntity.commission = t.getCommission();
        historyTradeEntity.commissionAsset = t.getCommissionAsset();
        historyTradeEntity.id = t.getId();
        historyTradeEntity.price = t.getPrice();
        historyTradeEntity.qty = t.getQty();
        historyTradeEntity.quoteQty = t.getQuoteQty();
        historyTradeEntity.time = t.getTime();
        historyTradeEntity.symbol = t.getSymbol();
        historyTradeEntity.buyer = t.isBuyer();
        historyTradeEntity.maker = t.isMaker();
        historyTradeDao.insert(historyTradeEntity);
    }
}
