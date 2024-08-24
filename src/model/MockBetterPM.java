package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A mock class for the PortfolioManager class to see if this model receives the correct
 * inputs from the controller.
 */
public class MockBetterPM implements IModelV2 {
  private final StringBuilder log;

  /**
   * Constructs a mock model object.
   *
   * @param log the string builder to record the inputs passed in to the model
   */
  public MockBetterPM(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
  }


  @Override
  public void addInvestmentToPortfolio(
          LocalDate date, String portfolioName, Investment stock, double quantity) {
    log.append(String.format(
            "Added %f shares of %s Investment into Portfolio %s on " + date.toString() + "\n",
            quantity, stock.toString(), portfolioName));
  }

  @Override
  public void sellInvestmentFromPortfolio(
          LocalDate date, String portfolioName, String ticker, double quantity) {
    log.append(String.format(
            "Sold %f shares of %s Investment from Portfolio %s on " + date.toString() + "\n",
            quantity, ticker, portfolioName));
  }

  @Override
  public void reverseTransaction(
          LocalDate date, String portfolioName, Investment inv, double quantity, String type) {
    log.append(String.format(
            "Reversed %f shares of %s Investment in Portfolio %s on " + date.toString() + "\n",
            quantity, inv.toString(), portfolioName));
  }

  @Override
  public void rebalance(String portfolioName, LocalDate date, Map<String, Integer> weights) {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, Integer> entry : weights.entrySet()) {
      sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
    }

    log.append(String.format("Rebalanced Portfolio %s on " + date.toString()
            + " with weights of " + sb, portfolioName));
  }

  @Override
  public Map<String, Double> getComposition(LocalDate date, String portfolioName) {
    log.append("Got composition of ").append(portfolioName)
            .append(" on ")
            .append(date.toString())
            .append("\n");
    Map<String, Double> res = new HashMap<>();
    res.put("GOOG", 2.0);
    return res;
  }

  @Override
  public Map<String, Double> getDistributionValue(LocalDate date, String portfolioName) {
    log.append("Got distribution of ")
            .append(portfolioName)
            .append(" on ")
            .append(date.toString())
            .append("\n");
    Map<String, Double> res = new HashMap<>();
    res.put("GOOG", 200.0);
    return res;
  }

  @Override
  public void savePortfolio(String portfolioName, String fileName) throws IOException {
    log.append("Saving ").append(portfolioName).append(" to ").append(fileName).append("\n");
  }

  @Override
  public void loadPortfolio(String filename, String name) throws IOException {
    log.append("Loading ").append(name).append(" from ").append(filename).append("\n");
  }

  @Override
  public List<Transactions> getTransactions(String portfolioName) {
    log.append("Getting transactions of ").append(portfolioName).append("\n");
    Transactions one = new Transactions(
            "GOOG", 0, LocalDate.of(
                    2024, 5, 21), Transactions.Types.Buy);
    List<Transactions> res = new ArrayList<>();
    res.add(one);
    return res;
  }

  @Override
  public String getPortfolioChart(LocalDate start, LocalDate end, String portName) {
    log.append(String.format("Getting chart for %s from %s to %s", portName, start, end));
    return "";
  }

  @Override
  public String getStockChart(LocalDate start, LocalDate end, String ticker, StockDataSource s) {
    log.append(String.format("Getting chart for %s from %s to %s", ticker, start, end));
    return "";
  }

  @Override
  public void createPortfolio(String name) {
    log.append("Creating a portfolio with name of: ").append(name).append("\n");
  }

  @Override
  public boolean hasPortfolio(String name) {
    log.append("Does this have portfolio ").append(name).append("\n");
    return false;
  }

  @Override
  public Investment queryStock(String symbol, StockDataSource source) {
    String s = "";
    if (source instanceof APIStockDataSource) {
      s = "api";
    } else if (source instanceof FileStockDataSource) {
      s = "file";
    }
    log.append("Querying the stock ").append(symbol).append(" from ").append(s).append("\n");
    return new Stock("GOOG", new FileStockDataSource("res/Stocks/GOOG"));
  }

  @Override
  public double getValueOfPortfolio(String name, LocalDate date) {
    log.append("Getting value of ").append(name).append(" on ")
            .append(date.toString())
            .append("\n");
    return 0;
  }
}
