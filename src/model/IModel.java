package model;

import java.time.LocalDate;

/**
 * This interface specifies the operations for an implementation of the main model of this program.
 * An IModel allows a user to create and store multiple portfolios, each represented by a name
 * of their choosing. Once created, a user can add investments, get the value, and examine data
 * on individual stocks.
 */
public interface IModel {

  /**
   * creates a portfolio with a specific name.
   */
  void createPortfolio(String name);

  /**
   * sees if portfolio exists in this manager.
   * @param name name of portfolio to see if it exists
   * @return whether it is in portfolio manager or not
   */
  boolean hasPortfolio(String name);

  /**
   * Returns a stock object that can be queried for further analysis.
   *
   * @param symbol ticker symbol of stock to be queried
   * @param source the source the use would like to use for the stock
   * @return a stock object matching the description entered
   */
  Investment queryStock(String symbol, StockDataSource source);

  /**
   * Adds a stock to one of user's portfolios.
   *
   * @param portfolioName name of portfolio for stock to be added to
   * @param stock the stock to be added
   * @param quantity number of shares to be added
   */
  void addInvestmentToPortfolio(String portfolioName, Investment stock, int quantity);

  /**
   * get the value of one of user's portfolios.
   *
   * @param name name of portfolio which user wants value of
   * @param date the date at which the portfolio is to be valued
   * @return the total value of all Investments in portfolio
   */
  double getValueOfPortfolio(String name, LocalDate date);
}
