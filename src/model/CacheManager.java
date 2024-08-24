package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a centralized local cache for stock data. Any class that implements
 * StockDataSource interface will cache fetched data to this cacheManager, allowing local caching
 * and storage of stock data from any source, whether it be API, file, or manual entry, etc.
 * This class works and interacts with classes of type StockDataSource.
 * Only one instance should exist in the program.
 */
public class CacheManager {
  private static final Map<String, List<Stock.StockData>> stockCache = new HashMap<>();
  // Static member to hold the single instance
  private static CacheManager instance;

  private CacheManager(){}

  /**
   * returns one and only one instance, singleton pattern.
   * @return an instance of cache manager, only return a new one if one doesn't exist already
   */
  public static CacheManager getInstance() {
    if (instance == null) {
      instance = new CacheManager();
    }
    return instance;
  }

  //fetches cached data
  List<Stock.StockData> fetchCachedData(String stockSymbol) {
    return stockCache.get(stockSymbol);
  }

  //caches the data to this cache manager
  void cacheData(String stockSymbol, List<Stock.StockData> data) {
    stockCache.put(stockSymbol, data);
  }

  //checks if the data of the stock is in the cache or not
  public boolean isDataCached(String stockSymbol) {
    return stockCache.containsKey(stockSymbol);
  }
}
