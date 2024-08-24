package controller;

import java.io.IOException;
import java.time.LocalDate;

import model.StockDataSource;

/**
 * This interface represents the features that the current GUI user interface supports.
 * Essentially, the user should be able to perform all of these specified operations through
 * the GUI.
 */
public interface Features {

  /**
   * callback function for creating a portfolio.
   *
   * @param name the name to be given to portfolio
   */
  void createPortfolio(String name) throws IOException;

  /**
   * callback function for adding a stock.
   *
   * @param date the date on which to buy a stock
   * @param portfolioName the name of portfolio in which to buy
   * @param quantity the quantity to buy
   * @param ticker the stock to be bought
   * @param s the source from which to buy the stock.
   */
  void addStock(LocalDate date, String portfolioName,
                String ticker, StockDataSource s, int quantity) throws IOException;

  /**
   * callback function for selling a stock.
   *
   * @param date the date on which to sell a stock
   * @param portfolioName the name of portfolio in which to sell
   * @param quantity the quantity to sell
   * @param ticker the stock to be sold
   */
  void sellStock(LocalDate date, String portfolioName,
                 String ticker, double quantity) throws IOException;

  /**
   * callback function for getting the value of a portfolio.
   *
   * @param date the date on which to query the value.
   * @param portfolioName the name of the portfolio to be queried.
   */
  void getValue(LocalDate date, String portfolioName) throws IOException;

  /**
   * callback function for getting the composition of a portfolio.
   *
   * @param date the date on which to query the composition.
   * @param portfolioName the name of the portfolio to be queried.
   */
  void getComposition(LocalDate date, String portfolioName) throws IOException;

  /**
   * callback function for creating a portfolio.
   *
   * @param portfolioName the portfolio to be saved
   * @param fileName the file path to be saved to
   */
  void savePortfolio(String portfolioName, String fileName) throws IOException;

  /**
   * callback function for loading a portfolio.
   *
   * @param filename the file path to be loaded from
   * @param name the name to be given to portfolio
   */
  void loadPortfolio(String filename, String name) throws IOException;
}
