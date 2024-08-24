package model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This is a Portfolio Manager class that stores a user's created portfolios.
 * This Manager will allow a user to differentiate between multiple portfolios
 * when attempting to query a specific one.
 */
public class PortfolioManager implements IModel {
  private final Map<String, Portfolio> portfolios;

  /**
   * Constructs an empty Portfolio object initialized with nothing.
   */
  public PortfolioManager() {
    portfolios = new HashMap<>();
  }

  @Override
  public void createPortfolio(String name) {
    if (!hasPortfolio(name)) {
      portfolios.put(name, new SimplePortfolio());
    } else {
      throw new IllegalArgumentException("Portfolio with that name already exists");
    }
  }

  @Override
  public boolean hasPortfolio(String name) {
    return portfolios.containsKey(name);
  }

  @Override
  public Investment queryStock(String symbol, StockDataSource source) {
    return new Stock(symbol, source);
  }

  @Override
  public void addInvestmentToPortfolio(String portfolioName, Investment stock, int quantity) {
    executeOnPortfolio(portfolioName, portfolio -> {
      portfolio.addInvestment(stock, quantity);
      return null;
    });
  }

  @Override
  public double getValueOfPortfolio(String name, LocalDate date) {
    return executeOnPortfolio(name, portfolio -> portfolio.getEndOfDayVal(date));
  }

  //CHANGE TO EXISTING CODE: modified PortfolioManager to abstract duplicate logic in addPortfolio
  //                         and create portfolio methods, resulting in less duplicate code
  //                         Now able to override this protected method in the extended
  //                         BetterPortfolioManager class to utilize PortfolioV2 objects and
  //                         reduce duplication across logic in methods there as well
  protected <T> T executeOnPortfolio(String portfolioName, Function<Portfolio, T> function) {
    if (hasPortfolio(portfolioName)) {
      Portfolio portfolio = portfolios.get(portfolioName);
      return function.apply(portfolio);
    } else {
      throw new IllegalArgumentException("Portfolio not found: " + portfolioName);
    }
  }
}
