import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import controller.IController;
import controller.InvestmentController;
import model.IModel;
import model.MockPortfolioManager;
import model.PortfolioManager;
import view.IView;
import view.TextView;

import static org.junit.Assert.assertEquals;

/**
 * This is a test class for the controller that sees if it passes the right inputs to
 * the model.
 */
public class ModelAndViewMocksTest {

  @Test
  //from TA: tests that an API connection was made as no exception was thrown
  //since API keeps changing, you can't really write one test that will always pass
  //here, a connection is established as queryStock can only be called with a
  //StockDataSource passed in
  public void testQueryAPI() throws IOException {
    Readable in = new StringReader("1\nGOOG\n2024\n06\n03\n2024\n06\n04\nAPI\n");
    assertEquals("Is Source an API? true! Query the Stock GOOG\n", mockStart(in));
  }

  @Test
  //reverse logic to see if StockDataSource is not API, but file as we only have two sources
  //as of now
  public void testQueryFile() throws IOException {
    Readable in = new StringReader(
            "1\nGOOG\n2024\n06\n03\n2024\n06\n04\nFile\nres/GOOG");
    assertEquals("Is Source an API? false! Query the Stock GOOG\n", mockStart(in));
  }

  @Test
  public void testCreate() throws IOException {
    Readable in = new StringReader("4\nportfolio1\n");
    assertEquals("Portfolio created: portfolio1\n", mockStart(in));
  }

  @Test
  public void testAddStock() throws IOException {
    Readable in = new StringReader("4\nportfolio1\n5\nportfolio1\nGOOG\n7\nAPI\n");
    String[] lines = mockStart(in).split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Added 7 shares of GOOG Investment into Portfolio portfolio1", last);
  }

  @Test
  public void testGetVal() throws IOException {
    Readable in = new StringReader(
            "4\nportfolio1\n5\nportfolio1\nGOOG\n7\nAPI\n6\nportfolio1\n2024\n05\n29\n");
    String[] lines = mockStart(in).split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Get Value of Portfolio portfolio1 at date 2024-05-29", last);
  }

  @Test
  public void testViewGoodbye() throws IOException {
    Readable in = new StringReader(
            "4\nportfolio1\n7\n");
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
            + "\n4. Create Portfolio\n5. Add Stock to Portfolio"
            + "\n6. Get Portfolio Value\n7. Exit"
            + "\nChoose an option by entering the corresponding number: ", last);
  }

  @Test
  public void testViewFetchGain() throws IOException {
    Readable in = new StringReader("1\nGOOG\n2024\n05\n21\n2024\n05\n29\nAPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[10];
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
    String last = lines[10];
    last = last.split("File: ")[1];
    assertEquals(
            "Error: Insufficient/No data for those parameters. Please try again.", last);
  }

  @Test
  public void testViewInvStock() throws IOException {
    Readable in = new StringReader("1\nwack\n2027\n05\n21\n2024\n05\n29\nAPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[10];
    last = last.split("File: ")[1];
    assertEquals(
            "Error: Insufficient/No data for those parameters. Please try again.", last);
  }

  @Test
  public void testViewInvSource() throws IOException {
    Readable in = new StringReader("1\nwack\n2027\n05\n21\n2024\n05\n29\nBPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[10];
    last = last.split("File: ")[1];
    assertEquals(
            "Error: Source not recognized", last);
  }

  @Test
  public void testViewAvg() throws IOException {
    Readable in = new StringReader("2\nGOOG\n2024\n05\n21\n7\nAPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[10];
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
    String last = lines[10];
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
    String last = lines[10];
    assertEquals("Choose an option by entering the corresponding number: "
            + "What name would you like to give your portfolio? "
            + "Successfully created!", last);
  }

  @Test
  public void testViewAlreadyCreated() throws IOException {
    Readable in = new StringReader("4\napple\n4\napple\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[20];
    assertEquals("Choose an option by entering the corresponding number: "
            + "What name would you like to give your portfolio? "
            + "Error: Portfolio with that name already exists", last);
  }

  @Test
  public void testViewAdd() throws IOException {
    Readable in = new StringReader("4\napple\n5\napple\nGOOG\n7\nAPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[20];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to add to? "
            + "Enter ticker symbol of desired stock: "
            + "How many whole (non-fractional) shares would you like to add? "
            + "Source of stock data? API or File: "
            + "Successfully added!", last);
  }

  @Test
  public void testViewAddError() throws IOException {
    Readable in = new StringReader("4\napple\n5\nbanana\nGOOG\n7\nAPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[20];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to add to? "
            + "Enter ticker symbol of desired stock: "
            + "How many whole (non-fractional) shares would you like to add? "
            + "Source of stock data? API or File: "
            + "Error: Portfolio not found: banana", last);
  }

  @Test
  public void testViewAddErrorStock() throws IOException {
    Readable in = new StringReader("4\napple\n5\nbanana\nGOOG\n7\nBPI\n");
    String[] lines = mockView(in).split("\n");
    String last = lines[20];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to add to? "
            + "Enter ticker symbol of desired stock: "
            + "How many whole (non-fractional) shares would you like to add? "
            + "Source of stock data? API or File: "
            + "Error: Source not recognized", last);
  }

  @Test
  public void testViewGetVal() throws IOException {
    Readable in = new StringReader("4\none\n5\none\nGOOG\n7\nAPI\n6\none\n2024\n05\n29");
    String[] lines = mockView(in).split("\n");
    String last = lines[30];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to find the value of? "
            + "Year in which you would like to find the value (YYYY): "
            + "Month in which you would like to find the value (1-12): "
            + "Day on which you would like to find the value (1-28/31): "
            + "one was valued at $1241.8 on that date", last);
  }

  @Test
  public void testViewGetValNotThere() throws IOException {
    Readable in = new StringReader("4\none\n5\none\nGOOG\n7\nAPI\n6\ntwo\n2024\n05\n29");
    String[] lines = mockView(in).split("\n");
    String last = lines[30];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to find the value of? "
            + "Year in which you would like to find the value (YYYY): "
            + "Month in which you would like to find the value (1-12): "
            + "Day on which you would like to find the value (1-28/31): "
            + "Error: Portfolio not found: two", last);
  }

  @Test
  public void testViewGetValNoDate() throws IOException {
    Readable in = new StringReader("4\none\n5\none\nGOOG\n7\nAPI\n6\ntwo\n2024\n5\n39");
    String[] lines = mockView(in).split("\n");
    String last = lines[30];
    assertEquals("Choose an option by entering the corresponding number: "
            + "Which portfolio would you like to find the value of? "
            + "Year in which you would like to find the value (YYYY): "
            + "Month in which you would like to find the value (1-12): "
            + "Day on which you would like to find the value (1-28/31): "
            + "Error: Invalid value for DayOfMonth (valid values 1 - 28/31): 39", last);
  }

  private String mockStart(Readable in) throws IOException {
    StringBuilder log = new StringBuilder();
    IModel mockModel = new MockPortfolioManager(log);
    IView view = new TextView(new StringBuilder());
    IController controller = new InvestmentController(in, mockModel, view);
    controller.start();
    return log.toString();
  }

  private String mockView(Readable in) throws IOException {
    StringBuilder log = new StringBuilder();
    IModel model = new PortfolioManager();
    IView view = new TextView(log);
    IController controller = new InvestmentController(in, model, view);
    controller.start();
    return log.toString();
  }

}