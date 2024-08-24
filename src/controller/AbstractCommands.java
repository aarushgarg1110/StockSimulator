package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import model.APIStockDataSource;
import model.FileStockDataSource;
import model.StockDataSource;
import view.IView;

//abstract class to represent shared operations by each command
abstract class AbstractCommands implements StocksCommands {
  private final Scanner scanner;

  //abstract constructor for each command
  protected AbstractCommands(Scanner scanner) {
    this.scanner = scanner;
  }

  //default constructor to take no arguments
  //this constructor is to be used for controllers that do not require a scanner or prompt
  protected AbstractCommands() {
    this.scanner = null;
  }

  @Override
  public void prompter(IView v) throws IOException {
    try {
      prompterHelp(v);
    } catch (Exception e) {
      v.showCustomMessage("Error: " + e.getMessage() + "\n");
    }
  }

  //helper method for prompter that asks for command specific inputs
  protected abstract void prompterHelp(IView view) throws IOException;

  //combines the process of view displaying text and controller getting user input
  protected String getInput(IView view, String prompt) throws IOException {
    if (scanner != null) {
      view.showCustomMessage(prompt);
      return scanner.nextLine();
    } else {
      throw new UnsupportedOperationException("Scanner not available");
    }
  }

  //set method for retrieving stock ticker symbol
  protected String getTickerSymbol(IView view) throws IOException {
    String input = getInput(view, "Enter ticker symbol of desired stock: ");
    return input.toUpperCase();
  }

  //set method for retrieving a number of days for any applicable command
  protected int getDaysBack(IView view) throws IOException {
    return Integer.parseInt(getInput(view, "How many days would you like to go back? "));
  }

  //gets the specified source of where the stock data has to come from
  protected StockDataSource getSourceInput(IView view) throws IOException {
    if (scanner != null) {
      view.showCustomMessage("Source of stock data? API or File: ");
      String sourceType = scanner.nextLine().toLowerCase();
      switch (sourceType) {
        case "api":
          return new APIStockDataSource();
        case "file":
          view.showCustomMessage("Provide the file path: ");
          return new FileStockDataSource(scanner.nextLine());
        default:
          throw new IllegalArgumentException("Source not recognized");
      }
    } else {
      throw new UnsupportedOperationException("Scanner not available");
    }
  }

  //CHANGE TO EXISTING CODE: added a helper method to make inputting the date more user-friendly
  //and less error-prone, removed previous dateInput method
  //gets the specified source of where the stock data has to come from
  protected LocalDate getDatesInput(IView v, String message) throws IOException {
    int year = Integer.parseInt(
            getInput(v, "Year in which you would like to " + message + " (YYYY): "));
    int month = Integer.parseInt(
            getInput(v, "Month in which you would like to " + message + " (1-12): "));
    int day = Integer.parseInt(
            getInput(v, "Day on which you would like to " + message + " (1-28/31): "));
    return LocalDate.of(year, month, day);
  }

}