package model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * A mock class for the PortfolioManager class to see if this model receives the correct
 * inputs from the controller.
 */
public class MockPortfolioManager implements IModel {
  private final StringBuilder log;

  /**
   * Constructs a mock model object.
   * @param log the string builder to record the inputs passed in to the model
   */
  public MockPortfolioManager(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
  }

  @Override
  public void createPortfolio(String name) {
    log.append(String.format("Portfolio created: %s\n", name));
  }

  @Override
  public boolean hasPortfolio(String name) {
    log.append(String.format("Check whether Portfolio exists: %s\n", name));
    return false;
  }

  @Override
  public Stock queryStock(String symbol, StockDataSource source) {
    log.append(String.format("Is Source an API? " + (source instanceof APIStockDataSource) + "! "
            + "Query the Stock %s\n", symbol));
    return new Stock("GOOG", new FileStockDataSource("res/Stocks/GOOG"));
  }

  @Override
  public void addInvestmentToPortfolio(String portfolioName, Investment stock, int quantity) {
    log.append(String.format("Added %d shares of %s Investment into Portfolio %s\n",
            quantity, stock.toString(), portfolioName));
  }

  @Override
  public double getValueOfPortfolio(String name, LocalDate date) {
    log.append(String.format("Get Value of Portfolio %s at date %s\n", name, date.toString()));
    return 0;
  }
}
