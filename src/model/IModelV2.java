package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * This interface specifies the operations for an implementation of the main model of this program.
 * This V2 version has additional functionality compared to those only available to the
 * SimplePortfolio objects. This interface supports BetterPortfolios.
 */
public interface IModelV2 extends IModel {

  @Override
  //dates are needed, so methods extended from portfolio are not supported anymore
  //as we now need dates passed in
  default void addInvestmentToPortfolio(String portfolioName, Investment stock, int quantity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Adds a stock to one of user's portfolios.
   *
   * @param date The date on which to add the investment
   * @param portfolioName name of portfolio for stock to be added to
   * @param stock the stock to be added
   * @param quantity number of shares to be added
   */
  void addInvestmentToPortfolio(
          LocalDate date, String portfolioName, Investment stock, double quantity);

  /**
   * sells a stock from one of user's portfolios.
   *
   * @param date The date on which to sell the investment
   * @param portfolioName name of portfolio for stock to be sold from
   * @param ticker the name of stock to be sold
   * @param quantity number of shares to be sold
   */
  void sellInvestmentFromPortfolio(
          LocalDate date, String portfolioName, String ticker, double quantity);

  /**
   * Transactions must be made in chronological order. If a user messes up a transaction, they
   * can undo or reverse the transaction making it as if it never happened.
   *
   * @param date The date of the transaction to be reversed
   * @param portfolioName the name of the portfolio where the transaction resides
   * @param inv the investment involved
   * @param quantity the number of shares they would like to reverse (buy or sell)
   * @param type the type of the original transaction (was it a buy or a sell)
   */
  void reverseTransaction(
          LocalDate date, String portfolioName, Investment inv, double quantity, String type);

  /**
   * Rebalances a portfolio's investments to the specified weights.
   * Will conduct buy and sell transactions according to make the value of each stock in the
   * portfolio match the weight of the total value of entire portfolio.
   *
   * @param portfolioName the name of the portfolio that needs to be rebalanced
   * @param date the date on which to rebalance the portfolio
   * @param weights the weights specified for each stock, reasonable for it to be integers
   */
  void rebalance(String portfolioName, LocalDate date, Map<String, Integer> weights);

  /**
   * This method gives the user the composition of their portfolio at a specified date in time.
   * A composition consists of the names of investments they hold and the quantity of that
   * investment that they hold
   *
   * @param date the date on which the user would like the composition of their portfolio
   * @param portfolioName the name of the portfolio that the composition is desired for
   * @return a map of each ticker associated with number of shares
   */
  Map<String, Double> getComposition(LocalDate date, String portfolioName);

  /**
   * This method gives the user the distribution of value of their portfolio at a specified
   * date in time. A distribution of value consists of the names of investments they hold and the
   * value of that investment that they hold
   *
   * @param date the date on which the user would like the composition of their portfolio
   * @param portfolioName the name of the portfolio that the composition is desired for
   * @return a map of each ticker associated with value
   */
  Map<String, Double> getDistributionValue(LocalDate date, String portfolioName);

  /**
   * This method allows a user to save a created portfolio from this program to their computer.
   * The user specifies the absolute path of where they would like this file to be stored.
   *
   * @param portfolioName the name of the portfolio that the composition is desired for
   * @param fileName the absolute path of the location of where this file is to be stored.
   */
  void savePortfolio(String portfolioName, String fileName) throws IOException;

  /**
   * This method allows a user to load a saved portfolio from their program to this program.
   * The user specifies the absolute path of where they would like this file to be retrieved
   * from. Once loaded, this portfolio can do all the actions an internal program portfolio
   * can do.
   *
   * @param filename the absolute path of the location of where this file is to be acquired from.
   * @param name the name they would like to store it as and refer to in the program
   */
  void loadPortfolio(String filename, String name) throws IOException;

  /**
   * Returns a copy of the list of transactions for PortfolioV2's for the user to see their log.
   *
   * @param portfolioName the portfolio to be referenced.
   * @return a copy of the list of transactions in this portfolioV2 object
   */
  List<Transactions> getTransactions(String portfolioName);

  /**
   * Returns a text version of a bar chart for analyzing a portfolio over a time period.
   *
   * @param start the start date
   * @param end the end date
   * @param portName the portfolio to be analyzed
   * @return a bar chart of performance
   */
  String getPortfolioChart(LocalDate start, LocalDate end, String portName);

  /**
   * Returns a text version of a bar chart for analyzing a stock over a time period.
   *
   * @param start the start date
   * @param end the end date
   * @param ticker the stock to be analyzed
   * @param s the source of the stock desired
   * @return a bar chart of performance
   */
  String getStockChart(LocalDate start, LocalDate end, String ticker, StockDataSource s);
}
