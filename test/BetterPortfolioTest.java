import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.APIStockDataSource;
import model.BetterPortfolio;
import model.FileStockDataSource;
import model.Investment;
import model.PortfolioV2;
import model.Stock;
import model.StockDataSource;
import model.Transactions;

import static org.junit.Assert.assertEquals;

/**
 * This is a test class for the BetterPortfolio class.
 */
public class BetterPortfolioTest {
  PortfolioV2 port;
  Investment google;
  Investment amazon;
  StockDataSource fileGoogle = new FileStockDataSource(
          "res/Stocks/GOOG");
  StockDataSource fileAmazon = new FileStockDataSource(
          "res/Stocks/AMZN");

  @Before
  public void setUp() {
    port = new BetterPortfolio();
    google = new Stock("GOOG", fileGoogle);
    amazon = new Stock("AMZN", fileAmazon);
    port.addInvestment(LocalDate.of(2023, 5, 29), google, 30);
  }

  @Test
  public void testGetEndOfDayVal() {
    double resultVal = port.getEndOfDayVal(LocalDate.of(2024, 6, 6));
    assertEquals(5232.6, resultVal, 0.01);
    assertEquals(30, port.getComposition(LocalDate.of(2024, 6, 6)).get("GOOG"), 0.1);

    double extra = port.getEndOfDayVal(LocalDate.of(2020, 6, 6));
    assertEquals(0, extra, 0.01);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddInvalidInvestment() {
    port.addInvestment(google, 30.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddFutureDate() {
    port.addInvestment(LocalDate.of(2029, 6, 14), google, 30.0);
  }

  @Test
  public void testAddInvestment() {
    port.addInvestment(LocalDate.of(2023, 6, 14), google, 30.0);
    Map<String, Double> resultMap = port.getComposition(LocalDate.of(2023, 6,
            14));
    assertEquals(60.0, resultMap.get("GOOG"), 0.01);
  }

  @Test
  public void testSellInvestment() {
    port.sellInvestment(LocalDate.of(2024, 6, 25),
            "GOOG", 10);
    Map<String, Double> resultMap = port.getComposition(LocalDate.of(2024, 6,
            26));
    assertEquals(20, resultMap.getOrDefault("GOOG", 0.0), 0.01);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testInvalidSellInvestment() {
    port.sellInvestment("GOOG", 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidChronologicalSell() {
    port.sellInvestment(LocalDate.of(2022, 6, 2),
            "GOOG", 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void NotEnoughSell() {
    port.sellInvestment(LocalDate.of(2024, 6, 5),
            "GOOG", 40);
  }

  @Test
  public void testReversal() {
    port.addInvestment(LocalDate.of(2024, 1, 10),
            google, 50);
    port.addInvestment(LocalDate.of(2024, 1, 10),
            amazon, 10);
    port.sellInvestment(LocalDate.of(2024, 1, 10),
            "GOOG", 10);

    port.reversal(LocalDate.of(2024, 1, 10),
            amazon, 6, "bought");

    assertEquals(4, port.getComposition(LocalDate.of(2024,1,
            10)).get("AMZN"), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReversalUnknown() {
    port.addInvestment(LocalDate.of(2024, 1, 10),
            google, 50);
    port.addInvestment(LocalDate.of(2024, 1, 10),
            amazon, 10);
    port.sellInvestment(LocalDate.of(2024, 1, 10),
            "GOOG", 10);

    port.reversal(LocalDate.of(2024, 1, 10),
            amazon, 6, "bough"
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReversalUnknownSell() {
    port.addInvestment(LocalDate.of(2024, 1, 10),
            google, 50);
    port.addInvestment(LocalDate.of(2024, 1, 10),
            amazon, 10);
    port.sellInvestment(LocalDate.of(2024, 1, 10),
            "GOOG", 10);

    port.reversal(LocalDate.of(2024, 1, 10),
            amazon, 6, "sel"
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReversalInvalidate() {
    port.addInvestment(LocalDate.of(2024, 1, 10),
            google, 50);
    port.addInvestment(LocalDate.of(2024, 1, 10),
            amazon, 10);
    port.sellInvestment(LocalDate.of(2024, 1, 14),
            "GOOG", 20);

    port.reversal(LocalDate.of(2024, 1, 10),
            google, 50, "bought"
    );
  }

  @Test
  public void testRebalance() {
    port.addInvestment(LocalDate.of(2024, 1, 10),
            amazon, 30);
    port.addInvestment(LocalDate.of(2024, 1, 10),
            new Stock("ASML", new APIStockDataSource()), 30);
    port.addInvestment(LocalDate.of(2024, 1, 10),
            new Stock("TSM", new APIStockDataSource()), 30);
    Map<String, Integer> mapOfStocks = new HashMap<>();
    Map<String, Double> result;
    mapOfStocks.put("GOOG", 25);
    mapOfStocks.put("AMZN", 25);
    mapOfStocks.put("ASML", 25);
    mapOfStocks.put("TSM", 25);
    double totalVal = port.getEndOfDayVal(LocalDate.of(2024, 5, 15));
    port.rebalance(LocalDate.of(2024, 5, 15), mapOfStocks);
    result = port.getComposition(LocalDate.of(2024, 5, 15));
    assertEquals(62.6669, result.get("GOOG"), 0.0001);
    assertEquals(58.5866, result.get("AMZN"), 0.0001);
    assertEquals(11.6239, result.get("ASML"), 0.0001);
    assertEquals(70.0380, result.get("TSM"), 0.0001);
    // compare their stock price times their shares on May 15
    assertEquals(result.get("GOOG") * 173.68, result.get("AMZN") * 185.99, 100);
    assertEquals(result.get("GOOG") * 173.68, result.get("ASML") * 937.42, 100);
    assertEquals(result.get("TSM") * 155.58, result.get("ASML") * 937.42, 100);
    assertEquals(totalVal, port.getEndOfDayVal(LocalDate.of(2024, 5, 15)), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRebalanceNotAddedUp() {
    port.addInvestment(LocalDate.of(2024, 1, 10),
            amazon, 30);
    port.addInvestment(LocalDate.of(2024, 1, 10),
            new Stock("ASML", new APIStockDataSource()), 30);
    port.addInvestment(LocalDate.of(2024, 1, 10),
            new Stock("TSM", new APIStockDataSource()), 30);
    Map<String, Integer> mapOfStocks = new HashMap<>();
    mapOfStocks.put("GOOG", 25);
    mapOfStocks.put("AMZN", 25);
    mapOfStocks.put("ASML", 20);
    mapOfStocks.put("TSM", 25);
    port.rebalance(LocalDate.of(2024, 5, 15), mapOfStocks);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRebalanceNotEnough() {
    port.addInvestment(LocalDate.of(2024, 1, 10),
            amazon, 30);
    port.addInvestment(LocalDate.of(2024, 1, 10),
            new Stock("ASML", new APIStockDataSource()), 30);
    port.addInvestment(LocalDate.of(2024, 1, 10),
            new Stock("TSM", new APIStockDataSource()), 30);
    Map<String, Integer> mapOfStocks = new HashMap<>();
    mapOfStocks.put("GOOG", 25);
    mapOfStocks.put("AMZN", 25);
    mapOfStocks.put("ASML", 20);
    port.rebalance(LocalDate.of(2024, 5, 15), mapOfStocks);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRebalanceMissingWeight() {
    port.addInvestment(LocalDate.of(2024, 1, 10),
            amazon, 30);
    port.addInvestment(LocalDate.of(2024, 1, 10),
            new Stock("ASML", new APIStockDataSource()), 30);
    port.addInvestment(LocalDate.of(2024, 1, 10),
            new Stock("TSM", new APIStockDataSource()), 30);
    Map<String, Integer> mapOfStocks = new HashMap<>();
    mapOfStocks.put("GOOG", 25);
    mapOfStocks.put("AMZN", 25);
    mapOfStocks.put("ASML", 25);
    mapOfStocks.put("ASL", 25);
    port.rebalance(LocalDate.of(2024, 5, 15), mapOfStocks);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRebalanceFutureDate() {
    port.addInvestment(LocalDate.of(2024, 1, 10),
            amazon, 30);
    port.addInvestment(LocalDate.of(2024, 1, 10),
            new Stock("ASML", new APIStockDataSource()), 30);
    port.addInvestment(LocalDate.of(2024, 1, 10),
            new Stock("TSM", new APIStockDataSource()), 30);
    Map<String, Integer> mapOfStocks = new HashMap<>();
    mapOfStocks.put("GOOG", 25);
    mapOfStocks.put("AMZN", 25);
    mapOfStocks.put("ASML", 25);
    mapOfStocks.put("TSM", 25);
    port.rebalance(LocalDate.of(2024, 11, 15), mapOfStocks);
  }

  @Test
  public void testGetComposition() {

    port.sellInvestment(LocalDate.of(2023, 5, 29), "GOOG", 30);
    port.addInvestment(LocalDate.of(2023, 6, 1), google, 10);
    port.addInvestment(LocalDate.of(2023, 6, 5), amazon, 20);
    port.sellInvestment(LocalDate.of(2023, 6, 10), "GOOG", 5);

    LocalDate date = LocalDate.of(2023, 6, 12);
    Map<String, Double> expectedComposition = new HashMap<>();
    expectedComposition.put("GOOG", 5.0);
    expectedComposition.put("AMZN", 20.0);
    Map<String, Double> actualComposition = port.getComposition(date);

    assertEquals(expectedComposition, actualComposition);

    date = LocalDate.of(2023, 6, 3);
    expectedComposition = new HashMap<>();
    expectedComposition.put("GOOG", 10.0);
    actualComposition = port.getComposition(date);

    assertEquals(expectedComposition, actualComposition);
  }

  @Test
  public void getDistributionVal() {
    port.addInvestment(LocalDate.of(2023, 6, 1), google, 10);
    port.addInvestment(LocalDate.of(2023, 6, 5), amazon, 20);
    port.sellInvestment(LocalDate.of(2023, 6, 10), "GOOG", 5);

    LocalDate date = LocalDate.of(2023, 6, 12);
    Map<String, Double> expectedDistVal = new HashMap<>();
    expectedDistVal.put("GOOG", 4352.25);
    expectedDistVal.put("AMZN", 2531.3999999999996);
    Map<String, Double> actualDistVal = port.getDistributionVal(date);

    assertEquals(expectedDistVal, actualDistVal);

    date = LocalDate.of(2023, 6, 3);
    expectedDistVal = new HashMap<>();
    expectedDistVal.put("GOOG", 5009.2);
    actualDistVal = port.getDistributionVal(date);

    assertEquals(expectedDistVal, actualDistVal);
  }

  @Test
  public void saveToFile() throws IOException {
    port.addInvestment(LocalDate.of(2023, 6, 5), amazon, 20);
    port.sellInvestment(LocalDate.of(2023, 6, 10), "GOOG", 5);

    port.saveToFile("res/xmlFile.xml");
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
  public void getTransactions() {
    Transactions first = new Transactions(
            "GOOG", 30, LocalDate.of(2023, 5, 29), Transactions.Types.Buy);
    Transactions second = new Transactions(
            "GOOG", 12.3, LocalDate.of(2023, 6, 10), Transactions.Types.Sell);

    List<Transactions> results = new ArrayList<>();
    results.add(first);
    assertEquals(results, port.getTransactions());

    port.sellInvestment(LocalDate.of(2023, 6, 10),"GOOG", 12.3);
    results.add(second);
    assertEquals(results, port.getTransactions());
  }

  @Test
  public void getChart() {
    StockDataSource nvdafile = new FileStockDataSource("res/Stocks/NVDA");
    StockDataSource aaplfile = new FileStockDataSource("res/Stocks/AAPL");
    Investment nvidaStock = new Stock("NVDA", nvdafile);
    Investment appleStock = new Stock("AAPL", aaplfile);
    PortfolioV2 port2 = new BetterPortfolio();
    port2.addInvestment(LocalDate.of(2022,1,3),
            google, 4);
    port2.addInvestment(LocalDate.of(2022,1,6),
            amazon, 40);
    port2.addInvestment(LocalDate.of(2022,1,10),
            nvidaStock, 400);
    port2.addInvestment(LocalDate.of(2022,1,15),
            appleStock, 400);
    port2.sellInvestment(LocalDate.of(2022,1,26),
            appleStock.toString(), 200);
    port2.sellInvestment(LocalDate.of(2022,1,26),
            appleStock.toString(), 200);
    port2.sellInvestment(LocalDate.of(2022,1,26),
            nvidaStock.toString(), 400);

    String chart = port2.getChart(LocalDate.of(2022, 1, 1),
            LocalDate.of(2022, 1, 28));

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