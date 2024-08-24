
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

import model.FileStockDataSource;
import model.StockDataSource;
import model.Transactions;

import java.util.List;
import java.util.Map;

import model.APIStockDataSource;
import model.BetterPortfolioManager;
import model.Investment;
import model.Stock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This is a test class for the BetterPortfolioManagerClass.
 */
public class BetterPortfolioManagerTest {

  BetterPortfolioManager portfolioManager;
  BetterPortfolioManager portfolioManager2;
  Investment googleStock;
  Investment amazonStock;

  @Before
  public void setUp() throws Exception {
    // portfolioManager is a new portfolio without anything.
    // portfolioManager2 is a portfolio with a new portfolio named
    // portfolio2 inside.
    portfolioManager = new BetterPortfolioManager();
    portfolioManager2 = new BetterPortfolioManager();
    portfolioManager2.createPortfolio("portfolio2");
    googleStock = new Stock("GOOG", new FileStockDataSource("res/Stocks/GOOG"));
    amazonStock = new Stock("AMZN", new FileStockDataSource("res/Stocks/AMZN"));
  }

  @Test
  public void createPortfolio() {
    portfolioManager.createPortfolio("example");
    assertTrue(portfolioManager.hasPortfolio("example"));
    try {
      portfolioManager.createPortfolio("example");
      fail("should fail here");
    } catch (Exception e) {
      assertEquals("Portfolio with that name already exists",
              e.getMessage());
    }
    portfolioManager.createPortfolio("example2");
    assertTrue(portfolioManager.hasPortfolio("example2"));
  }

  @Test
  public void addInvestmentToPortfolio() {
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2020, 1, 1),
            "portfolio2", googleStock, 4);
    Map<String, Double> emptyMap = portfolioManager2.getComposition(LocalDate.of(2019,
            12, 31), "portfolio2");
    for (Map.Entry<String, Double> entry : emptyMap.entrySet()) {
      assertEquals(entry.getValue(), 0.0, 0.001);
    }

    Map<String, Double> googleMap = portfolioManager2.getComposition(LocalDate.of(2020,
            1, 1), "portfolio2");
    for (Map.Entry<String, Double> entry : googleMap.entrySet()) {
      assertEquals(4, entry.getValue(), 0.001);
      assertEquals("GOOG", entry.getKey());
    }

    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 3),
            "portfolio2", amazonStock, 5);
    Map<String, Double> googleAmzMap = portfolioManager2.getComposition(LocalDate.of(2022,
            1, 4), "portfolio2");
    assertEquals(4, googleAmzMap.getOrDefault("GOOG", 0.0), 0.01);
    assertEquals(5, googleAmzMap.getOrDefault("AMZN", 0.0), 0.01);


    Map<String, Double> oldGoogleAmzMap = portfolioManager2.getComposition(LocalDate.of(2022,
            1, 2), "portfolio2");
    assertEquals(4, oldGoogleAmzMap.getOrDefault("GOOG", 0.0), 0.01);
    assertEquals(0, oldGoogleAmzMap.getOrDefault("AMZN", 0.0), 0.01);
  }

  @Test
  public void sellInvestmentFromPortfolio() {
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2020, 1, 1),
            "portfolio2", googleStock, 4);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 3),
            "portfolio2", amazonStock, 5);
    Map<String, Double> googleAmzMap = portfolioManager2.getComposition(LocalDate.of(2022,
            1, 4), "portfolio2");
    assertEquals(4, googleAmzMap.getOrDefault("GOOG", 0.0), 0.01);
    assertEquals(5, googleAmzMap.getOrDefault("AMZN", 0.0), 0.01);

    portfolioManager2.sellInvestmentFromPortfolio(LocalDate.of(2023, 1, 3),
            "portfolio2", amazonStock.toString(), 3);
    Map<String, Double> amazonLessBy3Data = portfolioManager2.getComposition(LocalDate.of(2023,
                    1, 4),
            "portfolio2");
    assertEquals(4, amazonLessBy3Data.getOrDefault("GOOG", 0.0), 0.01);
    assertEquals(2, amazonLessBy3Data.getOrDefault("AMZN", 0.0), 0.01);
    // the previous date's value should remain
    Map<String, Double> oldAmazonLessBy3Data = portfolioManager2.getComposition(LocalDate.of(2023,
                    1, 2),
            "portfolio2");
    assertEquals(4, oldAmazonLessBy3Data.getOrDefault("GOOG", 0.0), 0.01);
    assertEquals(5, oldAmazonLessBy3Data.getOrDefault("AMZN", 0.0), 0.01);
  }

  @Test
  public void reverseTransaction() {
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2020, 1, 1),
            "portfolio2", googleStock, 4);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 3),
            "portfolio2", amazonStock, 5);
    Map<String, Double> googleAmzMap = portfolioManager2.getComposition(LocalDate.of(2022,
            1, 4), "portfolio2");
    assertEquals(4, googleAmzMap.getOrDefault("GOOG", 0.0), 0.01);
    assertEquals(5, googleAmzMap.getOrDefault("AMZN", 0.0), 0.01);

    // now reverse the transaction on Amazon
    portfolioManager2.reverseTransaction(LocalDate.of(2022, 1, 3), "portfolio2",
            amazonStock, 4, "bought");

    Map<String, Double> afterReverseMap = portfolioManager2.getComposition(LocalDate.of(2022,
            1, 4), "portfolio2");
    assertEquals(4, afterReverseMap.getOrDefault("GOOG", 0.0), 0.01);
    assertEquals(1, afterReverseMap.getOrDefault("AMZN", 0.0), 0.01);

    // now try to reverse more than it buy

    try {
      portfolioManager2.reverseTransaction(LocalDate.of(2022, 1, 3), "portfolio2",
              amazonStock, 7, "bought");
    } catch (Exception e) {
      assertEquals("Not enough shares to sell. Available: 1.0, Requested: 7.0", e.getMessage());
    }

    Map<String, Double> afterReverseFailMap = portfolioManager2.getComposition(LocalDate.of(2022,
            1, 4), "portfolio2");
    assertEquals(4, afterReverseFailMap.getOrDefault("GOOG", 0.0), 0.01);
    assertEquals(1, afterReverseFailMap.getOrDefault("AMZN", 0.0), 0.01);


  }

  @Test
  public void rebalance() {
    Investment asmlStock = new Stock("ASML", new APIStockDataSource());
    Investment taiwanSemiStock = new Stock("TSM", new APIStockDataSource());
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 2),
            "portfolio2", googleStock, 4);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 2),
            "portfolio2", amazonStock, 4);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 2),
            "portfolio2", asmlStock, 4);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 2),
            "portfolio2", taiwanSemiStock, 4);
    portfolioManager2.rebalance("portfolio2", LocalDate.of(2023, 1, 1),
            Map.of("GOOG", 25,
                    "AMZN", 25,
                    "ASML", 30, "TSM", 20
            ));
    Map<String, Double> afterRebalance = portfolioManager2.getDistributionValue(
            LocalDate.of(2023, 1,
            2), "portfolio2");
    // amazon and google's value are the same
    assertEquals(afterRebalance.getOrDefault("AMZN", -1.0),
            afterRebalance.getOrDefault("GOOG", 0.0), 0.01);
    // asml and tsm's value should be 3 : 2
    assertEquals(afterRebalance.getOrDefault("ASML", -1.0) / 3,
            afterRebalance.getOrDefault("TSM", 0.0) / 2, 0.01);
  }

  @Test
  public void getComposition() {
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2020, 1, 1),
            "portfolio2", googleStock, 4);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2020, 1, 1),
            "portfolio2", amazonStock, 6);
    Map<String, Double> amazonGoogle = portfolioManager2.getComposition(LocalDate.of(2020,
            12, 31), "portfolio2");
    assertEquals(amazonGoogle.getOrDefault("GOOG", 0.00), 4, 0.01);
    assertEquals(amazonGoogle.getOrDefault("AMZN", 0.00), 6, 0.01);


    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2020, 1, 1),
            "portfolio2", googleStock, 4);
    Map<String, Double> newAmazonGoogle = portfolioManager2.getComposition(LocalDate.of(2020,
            12, 31), "portfolio2");
    assertEquals(newAmazonGoogle.getOrDefault("GOOG", 0.00), 8, 0.01);
    assertEquals(newAmazonGoogle.getOrDefault("AMZN", 0.00), 6, 0.01);
  }

  @Test
  public void getDistributionValue() {
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2020, 1, 1),
            "portfolio2", googleStock, 4);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2020, 2, 1),
            "portfolio2", amazonStock, 6);
    Map<String, Double> distributeOnlyGoogle
            = portfolioManager2.getDistributionValue(LocalDate.of(2020, 1, 2),
            "portfolio2");
    assertEquals(distributeOnlyGoogle.getOrDefault("GOOG", 0.0), 5469.48, 0.01);
    Map<String, Double> distributionGoogleAMZN = portfolioManager2.getDistributionValue(
            LocalDate.of(2020, 2, 5),
            "portfolio2");
    assertEquals(distributionGoogleAMZN.getOrDefault("GOOG", 0.0),
            5792.92, 0.01);
    assertEquals(distributionGoogleAMZN.getOrDefault("AMZN", 0.0),
            12239.22, 0.01);
  }

  @Test
  public void hasPortfolio() {
    BetterPortfolioManager portfolioManager = new BetterPortfolioManager();
    portfolioManager.createPortfolio("this");
    assertTrue(portfolioManager.hasPortfolio("this"));
    assertFalse(portfolioManager.hasPortfolio("that"));
  }

  @Test
  public void getValueOfPortfolio() {
    Investment asmlStock = new Stock("ASML", new APIStockDataSource());
    Investment taiwanSemiStock = new Stock("TSM", new APIStockDataSource());
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 2),
            "portfolio2", googleStock, 4);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 2),
            "portfolio2", amazonStock, 4);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 3),
            "portfolio2", asmlStock, 4);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 3),
            "portfolio2", taiwanSemiStock, 4);
    assertEquals(24911.72,
            portfolioManager2.getValueOfPortfolio("portfolio2",
                    LocalDate.of(2022, 1, 2)), 0.01);
    assertEquals(28943.48,
            portfolioManager2.getValueOfPortfolio("portfolio2",
                    LocalDate.of(2022, 1, 3)), 0.01);
  }

  @Test
  public void getTransactions() {
    Investment asmlStock = new Stock("ASML", new APIStockDataSource());
    Investment taiwanSemiStock = new Stock("TSM", new APIStockDataSource());
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 2),
            "portfolio2", googleStock, 4);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 2),
            "portfolio2", amazonStock, 4);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 3),
            "portfolio2", asmlStock, 4);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 3),
            "portfolio2", taiwanSemiStock, 4);
    portfolioManager2.sellInvestmentFromPortfolio(LocalDate.of(2022, 1, 3),
            "portfolio2", taiwanSemiStock.toString(), 2);

    List<Transactions> transactHistory = portfolioManager2.getTransactions("portfolio2");
    StringBuilder builder = new StringBuilder();
    for (Transactions transaction : transactHistory) {
      builder.append(transaction);
    }
    assertEquals("Ticker: GOOG\tShares: 4.0\tDate: 2022-01-02\t"
            + "Type: BuyTicker: AMZN\tShares: 4.0\tDate: 2022-01-02\tType: "
            + "BuyTicker: ASML\tShares: 4.0\tDate: 2022-01-03\tType: BuyTic"
            + "ker: TSM\tShares: 4.0\tDate: 2022-01-03\tType: BuyTicker: TS"
            + "M\tShares: 2.0\tDate: 2022-01-03\tType: Sell", builder.toString());
  }

  @Test
  public void saveToFile() throws IOException {
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2023, 5, 29),
            "portfolio2", googleStock, 30);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2023, 6, 5),
            "portfolio2", amazonStock, 20);
    portfolioManager2.sellInvestmentFromPortfolio(LocalDate.of(2023, 6, 10),
            "portfolio2", "GOOG", 5);

    portfolioManager2.savePortfolio("portfolio2", "res/xmlFile.xml");
    StringBuilder fileContent = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader("res/xmlFile.xml"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        fileContent.append(line).append("\n");
      }
    }

    String expectedContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
            + "<Portfolio>" + "\n"
            + "    <transaction>" + "\n"
            + "        <ticker>GOOG</ticker>" + "\n"
            + "        <quantity>30.0</quantity>" + "\n"
            + "        <date>2023-05-29</date>" + "\n"
            + "        <type>Buy</type>" + "\n"
            + "    </transaction>" + "\n"
            + "    <transaction>" + "\n"
            + "        <ticker>AMZN</ticker>" + "\n"
            + "        <quantity>20.0</quantity>" + "\n"
            + "        <date>2023-06-05</date>" + "\n"
            + "        <type>Buy</type>" + "\n"
            + "    </transaction>" + "\n"
            + "    <transaction>" + "\n"
            + "        <ticker>GOOG</ticker>" + "\n"
            + "        <quantity>5.0</quantity>" + "\n"
            + "        <date>2023-06-10</date>" + "\n"
            + "        <type>Sell</type>" + "\n"
            + "    </transaction>" + "\n"
            + "</Portfolio>" + "\n";

    assertEquals(expectedContent, fileContent.toString());
  }

  @Test
  public void loadFile() throws IOException {
    portfolioManager2.loadPortfolio("res/xmlFile.xml", "apple");
    Transactions t1 = new Transactions(
            "GOOG", 30, LocalDate.of(2023, 5, 29), Transactions.Types.Buy);
    Transactions t2 = new Transactions(
            "AMZN", 20, LocalDate.of(2023, 6, 5), Transactions.Types.Buy);
    Transactions t3 = new Transactions(
            "GOOG", 5, LocalDate.of(2023, 6, 10), Transactions.Types.Sell);

    List<Transactions> res = List.of(t1, t2, t3);
    assertEquals(res, portfolioManager2.getTransactions("apple"));
  }

  @Test
  public void getChartMonths() {
    portfolioManager2.createPortfolio("apple");
    portfolioManager2.addInvestmentToPortfolio(
            LocalDate.of(2022, 1, 3), "apple", amazonStock, 5);

    String chart = portfolioManager2.getPortfolioChart(LocalDate.of(2021, 12, 1),
            LocalDate.of(2023, 4, 28), "apple");

    assertEquals(14957.349999999999,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2022, 1, 31)), 0.01);
    assertEquals(15356.300000000001,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2022, 2, 28)), 0.01);
    assertEquals(16299.75,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2022, 3, 31)), 0.01);
    assertEquals(12428.150000000001,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2022, 4, 30)), 0.01);
    assertEquals(12020.95,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2022, 5, 31)), 0.01);
    assertEquals(531.05,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2022, 6, 30)), 0.01);
    assertEquals(674.75,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2022, 7, 31)), 0.01);
    assertEquals(633.85,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2022, 8, 31)), 0.01);
    assertEquals(565.0,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2022, 9, 30)), 0.01);
    assertEquals(512.2,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2022, 10, 31)), 0.01);
    assertEquals(482.70000000000005,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2022, 11, 30)), 0.01);
    assertEquals(420.0,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2022, 12, 31)), 0.01);
    assertEquals(515.65,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2023, 1, 31)), 0.01);
    assertEquals(471.15000000000003,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2023, 2, 28)), 0.01);
    assertEquals(516.45,
            portfolioManager2.getValueOfPortfolio("apple", LocalDate.of(2023, 3, 31)), 0.01);

    assertEquals("2021 DEC:\n"
            + "2022 JAN:*************************************\n"
            + "2022 FEB:**************************************\n"
            + "2022 MAR:*****************************************\n"
            + "2022 APR:*******************************\n"
            + "2022 MAY:******************************\n"
            + "2022 JUN:*\n"
            + "2022 JUL:**\n"
            + "2022 AUG:**\n"
            + "2022 SEP:*\n"
            + "2022 OCT:*\n"
            + "2022 NOV:*\n"
            + "2022 DEC:*\n"
            + "2023 JAN:*\n"
            + "2023 FEB:*\n"
            + "2023 MAR:*\n"
            + "2023 APR:*\n"
            + "Scale: * = $400 more than a base amount of $0\n", chart);
  }

  @Test
  public void getStockChart() {
    StockDataSource s = new FileStockDataSource("res/Stocks/GOOG");
    LocalDate start = LocalDate.of(2024,5,21);
    LocalDate end = LocalDate.of(2024,6,29);
    String chart = portfolioManager2.getStockChart(start, end, "goog", s);

    LocalDate target = LocalDate.of(2024,6,2);

    assertEquals(173.96, googleStock.getEndOfDayVal(target), 0.01);
    assertEquals(174.42, googleStock.getEndOfDayVal(end), 0.01);

    assertEquals("2024-05-21:******************\n"
            + "2024-05-23:******************\n2024-05-25:******************\n"
            + "2024-05-27:******************\n2024-05-29:******************\n"
            + "2024-05-31:*****************\n2024-06-02:*****************\n"
            + "2024-06-04:*****************\n2024-06-06:*****************\n"
            + "2024-06-08:*****************\n2024-06-10:*****************\n"
            + "2024-06-12:*****************\n2024-06-14:*****************\n"
            + "2024-06-16:*****************\n2024-06-18:*****************\n"
            + "2024-06-20:*****************\n2024-06-22:*****************\n"
            + "2024-06-24:*****************\n2024-06-26:*****************\n"
            + "2024-06-28:*****************\n2024-06-29:*****************\n"
            + "Scale: * = $10 more than a base amount of $0\n", chart);
  }

  @Test
  public void getChart() {
    Investment nvidaStock = new Stock("NVDA", new FileStockDataSource("res/Stocks/NVDA"));
    Investment appleStock = new Stock("AAPL", new FileStockDataSource("res/Stocks/AAPL"));
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 3),
            "portfolio2", googleStock, 4);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 6),
            "portfolio2", amazonStock, 40);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 10),
            "portfolio2", nvidaStock, 400);
    portfolioManager2.addInvestmentToPortfolio(LocalDate.of(2022, 1, 15),
            "portfolio2", appleStock, 400);
    portfolioManager2.sellInvestmentFromPortfolio(LocalDate.of(2022, 1, 26),
            "portfolio2", appleStock.toString(), 200);
    portfolioManager2.sellInvestmentFromPortfolio(LocalDate.of(2022, 1, 26),
            "portfolio2", appleStock.toString(), 200);
    portfolioManager2.sellInvestmentFromPortfolio(LocalDate.of(2022, 1, 26),
            "portfolio2", nvidaStock.toString(), 400);

    String chart = portfolioManager2.getPortfolioChart(LocalDate.of(2022, 1, 1),
            LocalDate.of(2022, 1, 28), "portfolio2");
    assertEquals("2022-01-01:\n"
            + "2022-01-02:\n"
            + "2022-01-03:**\n"
            + "2022-01-04:**\n"
            + "2022-01-05:**\n"
            + "2022-01-06:**********************\n"
            + "2022-01-07:**********************\n"
            + "2022-01-08:**********************\n"
            + "2022-01-09:**********************\n"
            + "2022-01-10:***************************************\n"
            + "2022-01-11:****************************************\n"
            + "2022-01-12:****************************************\n"
            + "2022-01-13:***************************************\n"
            + "2022-01-14:***************************************\n"
            + "2022-01-15:**************************************************\n"
            + "2022-01-16:**************************************************\n"
            + "2022-01-17:**************************************************\n"
            + "2022-01-18:************************************************\n"
            + "2022-01-19:***********************************************\n"
            + "2022-01-20:**********************************************\n"
            + "2022-01-21:********************************************\n"
            + "2022-01-22:********************************************\n"
            + "2022-01-23:********************************************\n"
            + "2022-01-24:********************************************\n"
            + "2022-01-25:*******************************************\n"
            + "2022-01-26:*******************\n"
            + "2022-01-27:*******************\n"
            + "2022-01-28:********************\n"
            + "Scale: * = $6400 more than a base amount of $0\n", chart);
  }
}