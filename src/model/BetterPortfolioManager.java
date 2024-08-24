package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * This is a Better Portfolio Manager class that stores a user's created portfolios.
 * This Manager will allow a user to differentiate between multiple portfolios
 * when attempting to query a specific one. It has more functionality in regard to
 * what a user can do with a portfolio. It can also load previously saved portfolios
 * from an XML file.
 */
public class BetterPortfolioManager extends PortfolioManager implements IModelV2 {
  private final Map<String, PortfolioV2> portfolios;

  /**
   * Constructs an empty Portfolio object initialized with nothing.
   */
  public BetterPortfolioManager() {
    portfolios = new HashMap<>();
  }

  @Override
  public void createPortfolio(String name) {
    if (!hasPortfolio(name)) {
      portfolios.put(name, new BetterPortfolio());
    } else {
      throw new IllegalArgumentException("Portfolio with that name already exists");
    }
  }

  @Override
  public void addInvestmentToPortfolio(
          LocalDate date, String portfolioName, Investment stock, double quantity) {
    executeOnPortfolioV2(portfolioName, portfolio -> {
      portfolio.addInvestment(date, stock, quantity);
      return null;
    });
  }

  @Override
  public void sellInvestmentFromPortfolio(
          LocalDate date, String portfolioName, String ticker, double quantity) {
    executeOnPortfolioV2(portfolioName, portfolio -> {
      portfolio.sellInvestment(date, ticker, quantity);
      return null;
    });
  }

  @Override
  public void reverseTransaction(
          LocalDate date, String portfolioName, Investment inv, double quantity, String type) {
    executeOnPortfolioV2(portfolioName, portfolio -> {
      portfolio.reversal(date, inv, quantity, type);
      return null;
    });
  }

  @Override
  public void rebalance(String portfolioName, LocalDate date, Map<String, Integer> weights) {
    executeOnPortfolioV2(portfolioName, portfolio -> {
      portfolio.rebalance(date, weights);
      return null;
    });
  }

  @Override
  public Map<String, Double> getComposition(LocalDate date, String portfolioName) {
    return executeOnPortfolioV2(portfolioName, portfolio -> portfolio.getComposition(date));
  }

  @Override
  public Map<String, Double> getDistributionValue(LocalDate date, String portfolioName) {
    return executeOnPortfolioV2(portfolioName, portfolio -> portfolio.getDistributionVal(date));
  }

  @Override
  public void savePortfolio(String portfolioName, String fileName) throws RuntimeException {
    executeOnPortfolioV2(portfolioName, portfolio -> {
      try {
        portfolio.saveToFile(fileName);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return null;
    });
  }


  @Override
  public void loadPortfolio(String filePath, String name) throws IOException {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new File(filePath));
      PortfolioV2 portfolio = new BetterPortfolio();

      NodeList list = doc.getElementsByTagName("transaction");
      for (int i = 0; i < list.getLength(); i++) {
        Node transactionNode = list.item(i);
        if (transactionNode.getNodeType() == Node.ELEMENT_NODE) {
          Element transactionElement = (Element) transactionNode;

          String ticker = transactionElement.getElementsByTagName("ticker")
                  .item(0).getTextContent();
          double quantity = Double.parseDouble(transactionElement.getElementsByTagName("quantity")
                  .item(0).getTextContent());
          LocalDate date = LocalDate.parse(transactionElement.getElementsByTagName("date")
                  .item(0).getTextContent());
          String type = transactionElement.getElementsByTagName("type")
                  .item(0).getTextContent();

          Transactions transaction = new Transactions(
                  ticker, quantity, date, Transactions.Types.valueOf(type));
          Stock stock = new Stock(ticker, new APIStockDataSource());

          if (transaction.getType() == Transactions.Types.Buy) {
            portfolio.addInvestment(date, stock, transaction.getShares());
          } else {
            portfolio.sellInvestment(date, stock.toString(), transaction.getShares());
          }
          portfolios.put(name, portfolio);
        }
      }


    } catch (javax.xml.parsers.ParserConfigurationException
             | org.xml.sax.SAXException
             | java.io.IOException e) {
      throw new IOException("Error: Could not save portfolio");
    }
  }

  @Override
  public boolean hasPortfolio(String name) {
    return portfolios.containsKey(name);
  }

  @Override
  public double getValueOfPortfolio(String name, LocalDate date) {
    return executeOnPortfolioV2(name, portfolio -> portfolio.getEndOfDayVal(date));
  }

  @Override
  public List<Transactions> getTransactions(String name) {
    return executeOnPortfolioV2(name, PortfolioV2::getTransactions);
  }

  @Override
  public String getPortfolioChart(LocalDate start, LocalDate end, String portName) {
    return executeOnPortfolioV2(portName, portfolio -> portfolio.getChart(start, end));
  }

  @Override
  public String getStockChart(LocalDate start, LocalDate end, String ticker, StockDataSource s) {
    Investment inv = queryStock(ticker, s);
    return new LineChartGenerator(start, end, inv).generate();
  }

  //abstracts duplicate logic in method bodies regarding portfolioV2s
  protected <T> T executeOnPortfolioV2(
          String portfolioName, Function<PortfolioV2, T> function) {
    if (hasPortfolio(portfolioName)) {
      PortfolioV2 portfolio = portfolios.get(portfolioName);
      return function.apply(portfolio);
    } else {
      throw new IllegalArgumentException("Portfolio not found: " + portfolioName);
    }
  }
}
