package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//abstract class to represent common operation of checking locally cached information before
//making a new query
abstract class AbstractDataSource implements StockDataSource {
  CacheManager cacheManager = CacheManager.getInstance();

  @Override
  public List<Stock.StockData> fetchData(String stockSymbol) {
    //check if the stock has already been queried
    //if so, use local cache instead of making a new query again
    if (cacheManager.isDataCached(stockSymbol)) {
      return cacheManager.fetchCachedData(stockSymbol);
    } else {
      // if stock has not been queried yet, call fetchNewData to query it.
      return fetchNewData(stockSymbol);
    }
  }

  // take in a scanner and a stock symbol, process them to a List of StockData
  protected List<Stock.StockData> scanDataToList(Scanner sc, String stockSymbol) {
    // build a new empty list that used for returning the final result
    List<Stock.StockData> results = new ArrayList<>();
    //skip header line
    sc.nextLine();
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      Stock.StockData data = getStockData(line);
      results.add(data);
    }
    sc.close();
    cacheManager.cacheData(stockSymbol, results);
    return results;
  }

  // getStockData read a String of line and return the stockData variable based on the given line
  private static Stock.StockData getStockData(String line) {
    String[] parameters = line.split(",");
    // read each variable seperated by comma
    LocalDate ld = LocalDate.parse(parameters[0]);
    double op = Double.parseDouble(parameters[1]);
    double hp = Double.parseDouble(parameters[2]);
    double lp = Double.parseDouble(parameters[3]);
    double cp = Double.parseDouble(parameters[4]);
    int vol = Integer.parseInt(parameters[5]);
    // build a new Stock object named data
    return new Stock.StockData(ld, op, hp, lp, cp, vol);
  }


  //this method will run if the given stock symbol is not found in cache history
  //this method will fetch new data based on the given stockSymbol, and the way to
  //fetch is based on the data source, so it's an abstract method here.
  protected abstract List<Stock.StockData> fetchNewData(String stockSymbol);
}
