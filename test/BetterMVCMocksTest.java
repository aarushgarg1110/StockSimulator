import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import controller.BetterInvestmentController;
import controller.IController;
import model.BetterPortfolioManager;
import model.IModel;
import model.MockBetterPM;
import view.IView;
import view.TextViewV2;

import static org.junit.Assert.assertEquals;

/**
 * This is a test class for the controller that sees if it passes the right inputs to
 * the model.
 */
public class BetterMVCMocksTest {

  @Test
  public void addInvestmentToPortfolio() throws IOException {
    Readable in = new StringReader("4\nportfolio1\n5\nportfolio1\nGOOG\n2024\n5\n29\n7\nAPI\n");
    String[] lines = mockStart(in).split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Added 7.000000 shares of GOOG Investment into "
            + "Portfolio portfolio1 on 2024-05-29", last);
  }

  @Test
  public void sellInvestmentFromPortfolio() throws IOException {
    Readable in = new StringReader("4\nportfolio1\n5\nportfolio1\nGOOG\n2024\n5\n"
            + "21\n7\nAPI\n6\nportfolio1\nGOOG\n2024\n5\n29\n2.3");
    String[] lines = mockStart(in).split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Sold 2.300000 shares of GOOG Investment from "
            + "Portfolio portfolio1 on 2024-05-29", last);
  }

  @Test
  public void reverseTransaction() throws IOException {
    Readable in = new StringReader("4\nportfolio1\n5\nportfolio1\nGOOG\n2024\n5\n"
            + "21\n7\nAPI\n6\nportfolio1\nGOOG\n2024\n5\n29\n2.3");
    String[] lines = mockStart(in).split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Sold 2.300000 shares of GOOG Investment from "
            + "Portfolio portfolio1 on 2024-05-29", last);
  }

  @Test
  public void rebalance() throws IOException {
    Readable in = new StringReader("4\nportfolio1\n5\nportfolio1\nGOOG\n2024\n5\n"
            + "21\n7\nAPI\n7\nportfolio1\n2024\n6\n3\ngoog\n100\n");
    String[] lines = mockStart(in).split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Rebalanced Portfolio portfolio1 on 2024-06-03 with weights of goog: 100", last);
  }

  @Test
  public void getComposition() throws IOException {
    Readable in = new StringReader("4\nportfolio1\n5\nportfolio1\nGOOG\n2024\n5\n"
            + "21\n7\nAPI\nc\nportfolio1\n2024\n5\n29");
    String[] lines = mockStart(in).split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Got composition of portfolio1 on 2024-05-29", last);
  }

  @Test
  public void getDistributionValue() throws IOException {
    Readable in = new StringReader("4\nportfolio1\n5\nportfolio1\nGOOG\n2024\n5\n"
            + "21\n7\nAPI\nd\nportfolio1\n2024\n5\n29");
    String[] lines = mockStart(in).split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Got distribution of portfolio1 on 2024-05-29", last);
  }

  @Test
  public void savePortfolio() throws IOException {
    Readable in = new StringReader("4\nportfolio1\n5\nportfolio1\nGOOG\n2024\n5\n"
            + "21\n7\nAPI\ns\nportfolio1\nwawa");
    String[] lines = mockStart(in).split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Saving portfolio1 to wawa", last);
  }

  @Test
  public void loadPortfolio() throws IOException {
    Readable in = new StringReader("4\nportfolio1\n5\nportfolio1\nGOOG\n2024\n5\n"
            + "21\n7\nAPI\ns\nportfolio1\nwawa\nl\nwawawa\nportfolio1");
    String[] lines = mockStart(in).split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Loading portfolio1 from wawawa", last);
  }

  @Test
  public void getTransactions() throws IOException {
    Readable in = new StringReader("4\nportfolio1\n5\nportfolio1\nGOOG\n2024\n5\n"
            + "21\n7\nAPI\n8\nportfolio1\n");
    String[] lines = mockStart(in).split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Getting transactions of portfolio1", last);
  }

  @Test
  public void testCreate() throws IOException {
    Readable in = new StringReader("4\nportfolio1\n");
    assertEquals("Creating a portfolio with name of: portfolio1\n", mockStart(in));
  }

  @Test
  //from TA: tests that an API connection was made as no exception was thrown
  //since API keeps changing, you can't really write one test that will always pass
  //here, a connection is established as queryStock can only be called with a
  //StockDataSource passed in
  public void testQueryAPI() throws IOException {
    Readable in = new StringReader("1\nGOOG\n2024\n06\n03\n2024\n06\n04\nAPI\n");
    assertEquals("Querying the stock GOOG from api\n", mockStart(in));
  }

  @Test
  //reverse logic to see if StockDataSource is not API, but file as we only have two sources
  //as of now
  public void testQueryFile() throws IOException {
    Readable in = new StringReader(
            "1\nGOOG\n2024\n06\n03\n2024\n06\n04\nFile\nres/GOOG");
    assertEquals("Querying the stock GOOG from file\n", mockStart(in));
  }

  @Test
  public void testGetVal() throws IOException {
    Readable in = new StringReader(
            "4\nportfolio1\n5\nportfolio1\nGOOG\n2024\n5\n21\n7\nAPI"
                    + "\n9\nportfolio1\n2024\n05\n29\n");
    String[] lines = mockStart(in).split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Getting value of portfolio1 on 2024-05-29", last);
  }

  @Test
  public void testViewGoodbye() throws IOException {
    Readable in = new StringReader(
            "4\nportfolio1\nq\n");
    String[] lines = mockView(in).split(" ");
    String last = lines[lines.length - 1];
    assertEquals("Goodbye!\n", last);
  }

  @Test
  public void testViewWelcome() throws IOException {
    Readable in = new StringReader("");
    String[] lines = mockView(in).split("\n");
    String last = lines[0];
    assertEquals("Welcome to the Investment Portfolio Manager!", last);
  }

  @Test
  public void testViewShowMenu() throws IOException {
    Readable in = new StringReader("");
    String[] lines = mockView(in).split("!");
    String last = lines[1];
    assertEquals("\n\nWhat would you like to do today?"
            + "\n1. Examine the gain or loss of a stock"
            + "\n2. Examine the x-day moving average of a stock"
            + "\n3. Find X-Day Crossovers for a stock"
            + "\n4. Create Portfolio"
            + "\n5. Add Stock to Portfolio"
            + "\n6. Sell Stock from Portfolio"
            + "\n7. Rebalance Portfolio with given weights"
            + "\n8. Undo a previous transaction (mistake)"
            + "\n9. Get Portfolio Value"
            + "\nC. Get Portfolio Composition (list of stocks and shares)"
            + "\nD. Get Portfolio Distribution of Values (list of stocks and values)"
            + "\nS. Save a Portfolio"
            + "\nL. Load a Portfolio from an XML file"
            + "\nX. See the performance of a portfolio over time"
            + "\nXS. See the performance of a stock over time"
            + "\nQ. Exit"
            + "\nChoose an option by entering the corresponding number: ", last);
  }

  @Test
  public void testViewFetchGain() throws IOException {
    Readable in = new StringReader("1\nGOOG\n2024\n05\n21\n2024\n05\n29\nAPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[19];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Enter ticker symbol of desired stock: "
            + "Year in which you would like to start finding the gain or loss (YYYY): "
            + "Month in which you would like to start finding the gain or loss (1-12): "
            + "Day on which you would like to start finding the gain or loss (1-28/31): "
            + "Year in which you would like to end finding the gain or loss (YYYY): "
            + "Month in which you would like to end finding the gain or loss (1-12): "
            + "Day on which you would like to end finding the gain or loss (1-28/31): "
            + "Source of stock data? API or File: Gain or Loss: "
            + "$-2.1399999999999864", last);
  }

  @Test
  public void testViewInvDate() throws IOException {
    Readable in = new StringReader("1\nGOOG\n2027\n05\n21\n2024\n05\n29\nAPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[19];
    last = last.split("File: ")[1];
    assertEquals(
            "Error: Insufficient/No data for those parameters. Please try again.", last);
  }

  @Test
  public void testViewInvStock() throws IOException {
    Readable in = new StringReader("1\nwack\n2027\n05\n21\n2024\n05\n29\nAPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[19];
    last = last.split("File: ")[1];
    assertEquals(
            "Error: Insufficient/No data for those parameters. Please try again.", last);
  }

  @Test
  public void testViewInvSource() throws IOException {
    Readable in = new StringReader("1\nwack\n2027\n05\n21\n2024\n05\n29\nBPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[19];
    last = last.split("File: ")[1];
    assertEquals(
            "Error: Source not recognized", last);
  }

  @Test
  public void testViewAvg() throws IOException {
    Readable in = new StringReader("2\nGOOG\n2024\n05\n21\n7\nAPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[19];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Enter ticker symbol of desired stock: "
            + "Year in which you would like to start the moving average from (YYYY): "
            + "Month in which you would like to start the moving average from (1-12): "
            + "Day on which you would like to start the moving average from (1-28/31): "
            + "How many days would you like to go back? "
            + "Source of stock data? API or File: 7 Days Moving Average: "
            + "$175.34714285714287", last);
  }

  @Test
  public void testViewCrossover() throws IOException {
    Readable in = new StringReader("3\nGOOG\n2024\n05\n21\n2024\n05\n29\n7\nAPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[19];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Enter ticker symbol of desired stock: "
            + "Year in which you would like to start examining the crossovers (YYYY): "
            + "Month in which you would like to start examining the crossovers (1-12): "
            + "Day on which you would like to start examining the crossovers (1-28/31): "
            + "Year in which you would like to end examining the crossovers (YYYY): "
            + "Month in which you would like to end examining the crossovers (1-12): "
            + "Day on which you would like to end examining the crossovers (1-28/31): "
            + "How many days would you like to go back? "
            + "Source of stock data? API or File: List of Crossover Dates: "
            + "[2024-05-28, 2024-05-22, 2024-05-21]", last);
  }

  @Test
  public void testViewCreate() throws IOException {
    Readable in = new StringReader("4\napple\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[19];
    assertEquals("Choose an option by entering the corresponding number: "
            + "What name would you like to give your portfolio? "
            + "Successfully created!", last);
  }

  @Test
  public void testViewAlreadyCreated() throws IOException {
    Readable in = new StringReader("4\napple\n4\napple\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[38];
    assertEquals("Choose an option by entering the corresponding number: "
            + "What name would you like to give your portfolio? "
            + "Error: Portfolio with that name already exists", last);
  }

  @Test
  public void testViewAdd() throws IOException {
    Readable in = new StringReader("4\napple\n5\napple\nGOOG\n2024\n5\n21\n7\nAPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[38];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to add to? "
            + "Enter ticker symbol of desired stock: "
            + "Year in which you would like to add investment (YYYY): "
            + "Month in which you would like to add investment (1-12): "
            + "Day on which you would like to add investment (1-28/31): "
            + "How many whole (non-fractional) shares would you like to add? "
            + "Source of stock data? API or File: "
            + "Successfully added!", last);
  }

  @Test
  public void testViewAddError() throws IOException {
    Readable in = new StringReader("4\napple\n5\nbanana\nGOOG\n2024\n5\n21\n7\nAPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[38];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to add to? "
            + "Enter ticker symbol of desired stock: "
            + "Year in which you would like to add investment (YYYY): "
            + "Month in which you would like to add investment (1-12): "
            + "Day on which you would like to add investment (1-28/31): "
            + "How many whole (non-fractional) shares would you like to add? "
            + "Source of stock data? API or File: "
            + "Error: Portfolio not found: banana", last);
  }

  @Test
  public void testViewAddErrorStock() throws IOException {
    Readable in = new StringReader("4\napple\n5\nbanana\nGOOG\n2024\n5\n21\n7\nBPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[38];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to add to? "
            + "Enter ticker symbol of desired stock: "
            + "Year in which you would like to add investment (YYYY): "
            + "Month in which you would like to add investment (1-12): "
            + "Day on which you would like to add investment (1-28/31): "
            + "How many whole (non-fractional) shares would you like to add? "
            + "Source of stock data? API or File: "
            + "Error: Source not recognized", last);
  }

  @Test
  public void testViewGetVal() throws IOException {
    Readable in = new StringReader("4\none\n5\none\nGOOG\n2024\n5\n21\n7\nAPI"
            + "\n9\none\n2024\n05\n29");
    String[] lines = mockView(in).split("\n");
    String last = lines[57];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to find the value of? "
            + "Year in which you would like to find the value (YYYY): "
            + "Month in which you would like to find the value (1-12): "
            + "Day on which you would like to find the value (1-28/31): "
            + "one was valued at $1241.8 on that date", last);
  }

  @Test
  public void testViewGetValNotThere() throws IOException {
    Readable in = new StringReader("4\none\n5\none\nGOOG\n7\nAPI\n9\ntwo\n2024\n05\n29");
    String[] lines = mockView(in).split("\n");
    String last = lines[58];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to find the value of? "
            + "Year in which you would like to find the value (YYYY): "
            + "Month in which you would like to find the value (1-12): "
            + "Day on which you would like to find the value (1-28/31): "
            + "Error: Portfolio not found: two", last);
  }

  @Test
  public void testViewGetValNoDate() throws IOException {
    Readable in = new StringReader("4\none\n5\none\nGOOG\n2024\n5\n21\n7\nAPI"
            + "\n9\ntwo\n2024\n5\n39");
    String[] lines = mockView(in).split("\n");
    String last = lines[57];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to find the value of? "
            + "Year in which you would like to find the value (YYYY): "
            + "Month in which you would like to find the value (1-12): "
            + "Day on which you would like to find the value (1-28/31): "
            + "Error: Invalid value for DayOfMonth (valid values 1 - 28/31): 39", last);
  }

  @Test
  public void testViewSell() throws IOException {
    Readable in = new StringReader("4\napple\n5\napple\nGOOG\n2024\n5\n21\n7\nAPI"
            + "\n6\napple\nGOOG\n2024\n5\n29\n2");
    String[] lines = mockView(in).split("\n");
    String last = lines[57];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to sell from? "
            + "Enter ticker symbol of desired stock: "
            + "Year in which you would like to sell investment (YYYY): "
            + "Month in which you would like to sell investment (1-12): "
            + "Day on which you would like to sell investment (1-28/31): "
            + "How many shares would you like to sell? "
            + "Successfully sold!", last);
  }

  @Test
  public void testViewSellChrono() throws IOException {
    Readable in = new StringReader("4\napple\n5\napple\nGOOG\n2024\n5\n21\n7\nAPI"
            + "\n6\napple\nGOOG\n2020\n5\n29\n2");
    String[] lines = mockView(in).split("\n");
    String last = lines[57];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to sell from? "
            + "Enter ticker symbol of desired stock: "
            + "Year in which you would like to sell investment (YYYY): "
            + "Month in which you would like to sell investment (1-12): "
            + "Day on which you would like to sell investment (1-28/31): "
            + "How many shares would you like to sell? "
            + "Error: Must conduct sales in chronological order", last);
  }

  @Test
  public void testViewSellNotEnough() throws IOException {
    Readable in = new StringReader("4\napple\n5\napple\nGOOG\n2024\n5\n21\n7\nAPI"
            + "\n6\napple\nGOOG\n2024\n5\n29\n200");
    String[] lines = mockView(in).split("\n");
    String last = lines[57];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to sell from? "
            + "Enter ticker symbol of desired stock: "
            + "Year in which you would like to sell investment (YYYY): "
            + "Month in which you would like to sell investment (1-12): "
            + "Day on which you would like to sell investment (1-28/31): "
            + "How many shares would you like to sell? "
            + "Error: Not enough shares to sell. Available: 7.0, Requested: 200.0", last);
  }

  @Test
  public void testViewReversal() throws IOException {
    Readable in = new StringReader("4\napple\n5\napple\nGOOG\n2024\n1\n10\n50\nAPI\n"
            + "5\napple\nAMZN\n2024\n1\n10\n10\nAPI\n"
            + "6\napple\nGOOG\n2024\n1\n10\n10\n"
            + "8\napple\nGOOG\n2024\n1\n10\n6\nbought\nAPI");
    String[] lines = mockView(in).split("\n");
    String last = lines[95];
    assertEquals("Choose an option by entering the corresponding number: "
            + "In which portfolio would you like to undo a transaction? "
            + "Transactions in portfolio apple:", last);
  }

  @Test
  public void testViewComposition() throws IOException {
    Readable in = new StringReader("4\napple\n5\napple\nGOOG\n2023\n6\n1\n10\nAPI\n"
            + "5\napple\nAMZN\n2024\n6\n5\n20\nAPI\n"
            + "6\napple\nGOOG\n2024\n6\n10\n5\n"
            + "c\napple\n2024\n6\n12\nGOOG\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[95];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to find the composition of? "
            + "Year in which you would like to find the composition of this portfolio (YYYY): "
            + "Month in which you would like to find the composition of this portfolio (1-12): "
            + "Day on which you would like to find the composition of this portfolio (1-28/31): "
            + "apple composition on 2024-06-12:", last);
  }

  @Test
  public void testViewDistribution() throws IOException {
    Readable in = new StringReader("4\napple\n5\napple\nGOOG\n2023\n6\n1\n10\nAPI\n"
            + "5\napple\nAMZN\n2024\n6\n5\n20\nAPI\n"
            + "6\napple\nGOOG\n2024\n6\n10\n5\n"
            + "d\napple\n2024\n6\n12\nGOOG\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[96];
    assertEquals("apple's distribution of values on 2024-06-12:", last);
  }

  @Test
  public void testViewSave() throws IOException {
    Readable in = new StringReader("4\napple\n5\napple\nGOOG\n2023\n6\n1\n10\nAPI\n"
            + "5\napple\nAMZN\n2024\n6\n5\n20\nAPI\n"
            + "6\napple\nGOOG\n2024\n6\n10\n5\n"
            + "s\napple\nwawawa\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[95];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to save? "
            + "Which absolute path on your computer would you like to save to? "
            + "Successfully saved apple to wawawa!", last);
  }

  @Test
  public void testViewLoad() throws IOException {
    Readable in = new StringReader("l\nres/xmlFile.xml\nportfolio1");
    String[] lines = mockView(in).split("\n");
    String last = lines[19] + lines[20];
    assertEquals("Choose an option by entering the corresponding number: "
            + "When loading your portfolio, historical data for each stock will be fetched from "
            + "the AlphaVantage API! Please make sure your XML file follows this format:", last);
  }

  @Test
  public void testChart() throws IOException {
    Readable in = new StringReader("l\nres/xmlFile.xml\nportfolio1\n"
            + "x\nportfolio1\n2024\n5\n29\n2024\n6\n3");
    String[] lines = mockStart(in).split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Getting chart for portfolio1 from 2024-05-29 to 2024-06-03", last);
  }

  @Test
  public void testViewChart() throws IOException {
    Readable in = new StringReader("l\nres/xmlFile.xml\nportfolio1\n"
            + "x\nportfolio1\n2024\n5\n29\n2024\n6\n3");
    String[] lines = mockView(in).split("\n");
    String last = lines[70];
    assertEquals("Scale: * = $10 more than a base amount of $7800", last);
  }

  @Test
  public void testStockChartAndView() throws IOException {
    Readable in = new StringReader("xs\ngoog\napi\n2024\n5\n29\n2024\n6\n3\n");
    String[] lines = mockStart(in).split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Getting chart for goog from 2024-05-29 to 2024-06-03", last);
  }

  @Test
  public void testStockChartView() throws IOException {
    Readable in = new StringReader("xs\ngoog\napi\n2024\n5\n29\n2024\n6\n3\n");
    String[] lines2 = mockView(in).split("\n");
    String last2 = lines2[29];
    assertEquals("Scale: * = $10 more than a base amount of $0", last2);
  }

  private String mockStart(Readable in) throws IOException {
    StringBuilder log = new StringBuilder();
    IModel mockModel = new MockBetterPM(log);
    IView view = new TextViewV2(new StringBuilder());
    IController controller = new BetterInvestmentController(in, mockModel, view);
    controller.start();
    return log.toString();
  }

  private String mockView(Readable in) throws IOException {
    StringBuilder log = new StringBuilder();
    IModel model = new BetterPortfolioManager();
    IView view = new TextViewV2(log);
    IController controller = new BetterInvestmentController(in, model, view);
    controller.start();
    return log.toString();
  }
}