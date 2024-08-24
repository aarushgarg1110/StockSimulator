package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * This PortfolioV2 interface specifies the operations for a better and
 * more complex portfolio than defined before. It builds off of previously
 * specified operations by enhancing or adding functionality.
 */
public interface PortfolioV2 extends Portfolio {

  @Override
  //dates are needed, so methods extended from portfolio are not supported anymore
  //as we now need dates passed in
  default void addInvestment(Investment inv, double quantity) {
    throw new UnsupportedOperationException("PortfolioV2s require an additional date parameter");
  }

  /**
   * addInvestment method takes in an investment and quantity desired.
   * It also takes in a date on when the user wishes to buy the investment
   *
   * @param date       is when the user wishes to buy the investment
   * @param investment is the stock the user wishes to buy
   * @param quantity   refers to how many shares the user would like to buy
   */
  void addInvestment(LocalDate date, Investment investment, double quantity)
          throws IllegalArgumentException;

  @Override
  default void sellInvestment(String ticker, double quantity) {
    throw new UnsupportedOperationException("PortfolioV2s require an additional date parameter");
  }

  /**
   * sellInvestment method takes in an investment and quantity desired.
   * It also takes in a date on when the user wishes to sell the investment.
   * sells investment from user portfolio in valid cases when there are appropriate
   * investments to be sold
   *
   * @param date     is when the user wishes to buy the investment
   * @param ticker   is the name of the stock the user wishes to sell
   * @param quantity refers to how many shares the user would like to sell
   */
  void sellInvestment(LocalDate date, String ticker, double quantity)
          throws IllegalArgumentException;

  /**
   * Allows a user to reverse a sale if a previous transaction made mistakenly renders their
   * desired transaction to be invalid.
   *
   * @param date     the date on which to reverse transaction
   * @param inv      the investment to be reversed
   * @param quantity the amount of shares to buy or sell
   * @param type     the original type of transaction, buy or sell
   */
  void reversal(LocalDate date, Investment inv, double quantity, String type);

  /**
   * Rebalances a portfolio's investments to the specified weights.
   * Will conduct buy and sell transactions according to make the value of each stock in the
   * portfolio match the weight of the total value of entire portfolio.
   *
   * @param date    the date on which to rebalance the portfolio
   * @param weights the weights specified for each stock, reasonable for it to be integers
   */
  void rebalance(LocalDate date, Map<String, Integer> weights);

  /**
   * This method gives the user the composition of their portfolio at a specified date in time.
   * A composition consists of the names of investments they hold and the quantity of that
   * investment that they hold
   *
   * @param date the date on which the user would like the composition of their portfolio
   * @return a map holding the string representation of a stock as a key and the quantity as a
   *         value
   */
  Map<String, Double> getComposition(LocalDate date);

  /**
   * This method gives the user the distribution of value of their portfolio at a specified
   * date in time. A distribution of value consists of the names of investments they hold and the
   * value of that investment that they hold
   *
   * @param date the date on which the user would like the composition of their portfolio
   * @return a map holding the string representation of a stock as a key and the value as a
   *         value
   */
  Map<String, Double> getDistributionVal(LocalDate date);

  /**
   * This method allows a user to save a created portfolio from this program to their computer.
   * The user specifies the absolute path of where they would like this file to be stored.
   *
   * @param filename the absolute path of the location of where this file is to be stored.
   */
  void saveToFile(String filename) throws IOException;

  /**
   * Returns a copy of the list of transactions for PortfolioV2's for the user to see their log.
   *
   * @return a copy of the list of transactions in this portfolioV2 object
   */
  List<Transactions> getTransactions();

  /**
   * Returns a text version of a bar chart for analyzing a portfolio over a time period.
   *
   * @param start the start date
   * @param end   the end date
   * @return a bar chart of performance
   */
  String getChart(LocalDate start, LocalDate end);
}
