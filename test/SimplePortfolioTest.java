import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import model.APIStockDataSource;
import model.FileStockDataSource;
import model.Investment;
import model.Portfolio;
import model.SimplePortfolio;
import model.Stock;
import model.StockDataSource;

import static org.junit.Assert.assertEquals;

/**
 * This is a test class for the SimplePortfolio class.
 */
public class SimplePortfolioTest {
  Investment googleStock;
  //initialize it here so API query limit doesn't get overloaded with each test
  //only called once every time the entire test file has to load.
  StockDataSource apiSource = new APIStockDataSource();

  //will be using fileGoogle as it does not require internet
  StockDataSource fileGoogle = new FileStockDataSource(
          "res/Stocks/GOOG");

  @Before
  public void setUp() {
    googleStock = new Stock("GOOG", fileGoogle);
  }

  @Test
  //tests both adding value and date at the same time
  //in order to check if an investment was added, you assert against the getValue
  //in order to check get value, you have to add investments
  //in other words, the code would be identical for testing both
  public void addInvestmentAndValue() {
    LocalDate recent = LocalDate.parse("2024-05-29");
    Portfolio port = new SimplePortfolio();
    port.addInvestment(googleStock, 6);
    assertEquals(177.4 * 6, port.getEndOfDayVal(recent), 0.01);
  }

  @Test
  public void sellInvestment() {
    LocalDate recent = LocalDate.parse("2024-05-29");
    Portfolio port = new SimplePortfolio();
    port.addInvestment(googleStock, 6);
    port.sellInvestment(googleStock.toString(), 2);
    assertEquals(177.4 * 4, port.getEndOfDayVal(recent), 0.01);
  }

}