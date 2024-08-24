package model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a SimplePortfolio object, which can consist of only Stock objects.
 * Each simple portfolio will have a value that can be calculated in regard to a specific date.
 * This class is not meant to be overridden or extended, but meant to be used for code recomposition
 * if other types of investments are to be a part of a portfolio
 */
public final class SimplePortfolio implements Portfolio {
  private final Map<Investment, Double> stocks;

  /**
   * Constructs an empty Portfolio object initialized with nothing.
   */
  public SimplePortfolio() {
    this.stocks = new HashMap<>();
  }

  @Override
  public void addInvestment(Investment stock, double quantity) {
    if (quantity > 0) {
      stocks.put(stock, stocks.getOrDefault(stock, 0.0) + quantity);
    }
  }

  //Refer to Portfolio Interface for comments justifying this added method
  @Override
  public void sellInvestment(String stockName, double quantity) {
    for (Map.Entry<Investment, Double> entry : stocks.entrySet()) {
      if (entry.getKey().toString().equals(stockName) && quantity > 0) {
        Investment stock = entry.getKey();
        stocks.put(stock, stocks.getOrDefault(stock, 0.0) - quantity);
        break;
      }
    }
  }

  @Override
  public double getEndOfDayVal(LocalDate date) {
    double totalPrice = 0.00;
    for (Map.Entry<Investment, Double> entry : stocks.entrySet()) {
      totalPrice += entry.getKey().getEndOfDayVal(date) * entry.getValue();
    }
    return totalPrice;
  }
}
