package model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * One way for the program to obtain stock data is through an API.
 * This class represents data obtained through the Alpha Vantage API.
 */
public final class APIStockDataSource extends AbstractDataSource {

  //utilize java's default constructor as no fields are stored or needed

  @Override
  public List<Stock.StockData> fetchNewData(String stockSymbol) {
    URL url;
    try {
      String apiKey = "W0M1JOKC82EZEQA8";
      url = new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + stockSymbol + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }
    try {
      Scanner sc = new Scanner(url.openStream());
      return scanDataToList(sc, stockSymbol);
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + stockSymbol);
    }
  }
}
