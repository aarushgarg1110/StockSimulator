package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents one form of investment, stocks.
 * A stock has a ticker symbol associated with it and a list of historical data
 * regarding previous prices.
 */
public class Stock implements Investment {
  private final String ticker;
  private final List<StockData> data;


  /**
   * Constructs a Stock object with its ticker symbol.
   *
   * @param ticker the ticker value associated with the stock
   */
  public Stock(String ticker, StockDataSource source) {
    this.ticker = ticker;
    this.data = source.fetchData(ticker);
  }

  /**
   * a nested class representing a StockData object.
   * Holds information of prices for a specific day
   * Stock object will have a list of multiple StockData objects.
   */
  public static class StockData {
    private final LocalDate date;
    private final double openPrice;
    private final double highPrice;
    private final double lowPrice;
    private final double closePrice;
    private final int volume;

    /**
     * Constructor of StockData.
     *
     * @param d   is the date of the stock
     * @param op  is the open price of the stock
     * @param hp  is the high price of the stock
     * @param lp  is the low price of the stock
     * @param cp  is the close price of the stock
     * @param vol is the volume of the stock
     */
    public StockData(LocalDate d, double op, double hp, double lp, double cp, int vol) {
      this.date = d;
      this.openPrice = op;
      this.highPrice = hp;
      this.lowPrice = lp;
      this.closePrice = cp;
      this.volume = vol;
    }
  }

  @Override
  public double fetchGainOrLoss(LocalDate startDate, LocalDate endDate)
          throws IllegalArgumentException {

    int[] indices = findIndicesOf(startDate, endDate);
    int startIndex = indices[0];
    int endIndex = indices[1];

    double startingPrice = data.get(startIndex).closePrice;
    double endingPrice = data.get(endIndex).closePrice;

    return endingPrice - startingPrice;
  }

  @Override
  public double calculateXDayMovingAvg(LocalDate startDate, int days)
          throws IllegalArgumentException, IndexOutOfBoundsException {
    if (days <= 0) {
      throw new IllegalArgumentException("days must be greater than 0");
    }

    int recentIdx = -1;
    int olderIdx = -1;

    for (StockData point : this.data) {
      if (point.date.equals(startDate)) {
        recentIdx = data.indexOf(point);
        olderIdx = recentIdx + days;
        break;
      }
    }
    if (recentIdx == -1) {
      throw new IllegalArgumentException("data not available for specified date");
    } else if (olderIdx >= data.size()) {
      throw new IndexOutOfBoundsException("data cannot go back that far");
    }

    List<StockData> acquiredData = data.subList(recentIdx, olderIdx);
    double total = 0.0;
    for (StockData point : acquiredData) {
      total += point.closePrice;
    }
    return total / acquiredData.size();
  }

  @Override
  public List<LocalDate> findCrossovers(LocalDate startDate, LocalDate endDate, int days) {
    List<LocalDate> returnList = new ArrayList<>();

    int[] indices = findIndicesOf(startDate, endDate);
    int oldIndex = indices[0];
    int recentIndex = indices[1];

    for (int i = recentIndex; i <= oldIndex; i++) {
      if (data.get(i).closePrice > calculateXDayMovingAvg(data.get(i).date, days)) {
        returnList.add(data.get(i).date);
      }
    }
    return returnList;
  }

  //helper method to abstract common logic in findCrossovers and fetchGainOrLoss
  private int[] findIndicesOf(LocalDate startDate, LocalDate endDate)
          throws IllegalArgumentException {
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("End date must come after start date");
    }

    int olderIdx = -1;
    int recentIdx = -1;

    for (int i = 0; i < data.size(); i++) {
      if (data.get(i).date.equals(startDate)) {
        olderIdx = i;
      }
      if (data.get(i).date.equals(endDate)) {
        recentIdx = i;
      }
      if (olderIdx != -1 && recentIdx != -1) {
        break;
      }
    }

    if (olderIdx == -1 || recentIdx == -1) {
      throw new IllegalArgumentException("Invalid date range or data not available.");
    }

    return new int[]{olderIdx, recentIdx};
  }

  @Override
  public double getEndOfDayVal(LocalDate targetDate) throws IllegalArgumentException {
    LocalDate currentDate = targetDate;
    LocalDate oldestStock = LocalDate.parse("1960-01-01");
    while (!currentDate.isBefore(LocalDate.MIN) && currentDate.isAfter(oldestStock)) {
      for (StockData point : data) {
        if (point.date.equals(currentDate)) {
          return point.closePrice;
        }
      }
      currentDate = currentDate.minusDays(1);
    }
    throw new IllegalArgumentException(
            "No price available for the target date or any previous date");
  }

  @Override
  public String toString() {
    return ticker;
  }
}
