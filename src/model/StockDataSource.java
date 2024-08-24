package model;

import java.util.List;

/**
 * Data for Stocks can be obtained through various sources (API, manual entry, files etc.)
 * This interface represents common operations that should be applicable to all those various
 * sources.
 */
public interface StockDataSource {

  /**
   * This method fetches the data from the specified source. It retrieves data in the form
   * of StockData objects, and then returns a Stock object containing all of that historical data.
   *
   * @param stockSymbol the ticker symbol of the stock the user would like to get
   * @return a Stock object with historical data.
   */
  List<Stock.StockData> fetchData(String stockSymbol);
}
