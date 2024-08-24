import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;

import controller.Features;
import controller.GUIController;
import controller.IGUIController;
import model.APIStockDataSource;
import model.IModel;
import model.MockBetterPM;
import view.GUIView;
import view.MockGUIView;

import static org.junit.Assert.assertEquals;

/**
 * This is a test class for the GUI controller that sees if it passes the right inputs to
 * the model.
 */
public class GUIMocksAndFeaturesTest {
  StringBuilder log;
  StringBuilder logV;
  IModel mockModel;
  GUIView view;
  Features feature;
  IGUIController controller;
  LocalDate date;

  @Before
  public void setUp() {
    date = LocalDate.of(2024, 6, 3);
    log = new StringBuilder();
    logV = new StringBuilder();
    mockModel = new MockBetterPM(log);
    view = new MockGUIView(logV);
    GUIController controllerImpl = new GUIController(mockModel);
    controller = controllerImpl;
    feature = controllerImpl;
    controller.setView(view);
  }

  @Test
  public void addStockFeature() throws IOException {
    feature.addStock(date, "apple", "goog", new APIStockDataSource(), 3);
    String[] lines = log.toString().split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Added 3.000000 shares of GOOG Investment into "
            + "Portfolio apple on 2024-06-03", last);
  }

  @Test
  public void sellStockFeature() throws IOException {
    feature.sellStock(date, "apple", "goog", 3.3);
    String[] lines = log.toString().split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Sold 3.300000 shares of goog Investment from "
            + "Portfolio apple on 2024-06-03", last);
  }

  @Test
  public void createPort() throws IOException {
    feature.createPortfolio("apple");
    String[] lines = log.toString().split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Creating a portfolio with name of: apple", last);
  }

  @Test
  public void savePort() throws IOException {
    feature.savePortfolio("apple", "banana");
    String[] lines = log.toString().split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Saving apple to banana", last);
  }

  @Test
  public void loadPort() throws IOException {
    feature.loadPortfolio("apple", "abanana");
    String[] lines = log.toString().split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Loading abanana from apple", last);
  }

  @Test
  public void comp() throws IOException {
    feature.getComposition(date, "abanana");
    String[] lines = log.toString().split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Got composition of abanana on 2024-06-03", last);
  }

  @Test
  public void value() throws IOException {
    feature.getValue(date, "abanana");
    String[] lines = log.toString().split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Getting value of abanana on 2024-06-03", last);
  }

  @Test
  public void showMessage() throws IOException {
    view.showCustomMessage("abanana");
    String[] lines = logV.toString().split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Showing custom message of: abanana", last);
  }

  @Test
  public void showMenu() throws IOException {
    view.showMenu();
    assertEquals("We currently have the following features:\n"
            + "Create a Portfolio\n"
            + "Save a Portfolio\n"
            + "Load a Portfolio\n"
            + "Buy Stocks\n"
            + "Sell Stocks\n"
            + "Get the Value of your Portfolio\n"
            + "Get the Composition of your Portfolio\n"
            + "More features coming soon!", logV.toString());
  }

  @Test
  public void welcome() throws IOException {
    view.welcome();
    String[] lines = logV.toString().split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Welcome to the Investment Portfolio Manager!", last);
  }

  @Test
  public void goodbye() throws IOException {
    view.goodbye();
    String[] lines = logV.toString().split("\n");
    String last = lines[lines.length - 1];
    assertEquals("Goodbye!", last);
  }
}