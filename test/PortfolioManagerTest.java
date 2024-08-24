import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import model.APIStockDataSource;
import model.FileStockDataSource;
import model.Investment;
import model.PortfolioManager;
import model.Stock;
import model.StockDataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * This is a test class for the PortfolioManager (model) class.
 */
public class PortfolioManagerTest {
  PortfolioManager manager;
  Investment googleStock;
  //initialize it here so API query limit doesn't get overloaded with each test
  //only called once every time the entire test file has to load.
  StockDataSource apiSource = new APIStockDataSource();

  //will be using fileGoogle as it does not require internet
  StockDataSource fileGoogle = new FileStockDataSource(
          "res/Stocks/GOOG");
  File timestamps = new File("res/timestamps");

  @Before
  public void setUp() {
    manager = new PortfolioManager();
    googleStock = new Stock("GOOG", fileGoogle);
  }

  @Test
  public void testHasPortfolioAndCreatePortfolio() {
    assertFalse(manager.hasPortfolio("first"));
    manager.createPortfolio("first");
    assertTrue(manager.hasPortfolio("first"));

    manager.createPortfolio("second");
    assertTrue(manager.hasPortfolio("first"));
    assertTrue(manager.hasPortfolio("second"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void alreadyCreated() {
    assertFalse(manager.hasPortfolio("first"));
    manager.createPortfolio("first");
    assertTrue(manager.hasPortfolio("first"));

    manager.createPortfolio("first");
  }

  @Test
  public void queryStock() {
    //tests queried stock to make sure it's correct by comparing its data against
    //example google stock
    Investment managerGoogle = manager.queryStock("GOOG", apiSource);
    Random rand = new Random(24);
    try {
      for (int i = 0; i < 10000; i++) {
        Scanner endTimes = new Scanner(timestamps);
        Scanner startTimes = new Scanner(timestamps);
        int randVal = rand.nextInt(500) + 50;
        LocalDate endDate = LocalDate.parse("2024-01-01");
        LocalDate startDate = LocalDate.parse("2024-01-01");

        for (int j = 1; j < randVal; j++) {
          startDate = LocalDate.parse(startTimes.nextLine());
        }

        for (int k = 1; k < randVal - 20; k++) {
          endDate = LocalDate.parse(endTimes.nextLine());
        }

        double actualGainOrLoss = googleStock.fetchGainOrLoss(startDate, endDate);
        double expectedGainOrLoss = managerGoogle.fetchGainOrLoss(startDate, endDate);

        double actualAvg = googleStock.calculateXDayMovingAvg(startDate, 30);
        double expectedAvg = managerGoogle.calculateXDayMovingAvg(startDate, 30);

        List<LocalDate> actualCross = googleStock.findCrossovers(startDate, endDate, 30);
        List<LocalDate> expectedCross = managerGoogle.findCrossovers(startDate, endDate, 30);

        assertEquals(expectedAvg, actualAvg, 0.01);
        assertEquals(expectedGainOrLoss, actualGainOrLoss, 0.01);
        assertEquals(expectedCross, actualCross);
      }
    } catch (FileNotFoundException e) {
      fail("Specified files not found");
    }
  }

  @Test
  public void testAddInvestmentToPortfolioAndValue() {
    //tests both the add investment and value methods
    LocalDate recent = LocalDate.parse("2024-05-29");
    manager.createPortfolio("business");
    manager.addInvestmentToPortfolio("business", googleStock, 6);

    //value of portfolio should be 6*the closing price of googlestock on 5-29-2024
    assertEquals(177.4, googleStock.getEndOfDayVal(recent), 0.01);
    assertEquals(177.4 * 6, manager.getValueOfPortfolio(
            "business", recent), 0.01);

    Investment appleStock = new Stock("AAPL", apiSource);
    manager.addInvestmentToPortfolio("business", appleStock, 2);
    assertEquals(190.29, appleStock.getEndOfDayVal(recent), 0.01);
    assertEquals(177.4 * 6 + 190.29 * 2, manager.getValueOfPortfolio(
            "business", recent), 0.01);
  }
}