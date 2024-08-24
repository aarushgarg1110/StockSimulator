package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import model.IModelV2;
import model.Investment;
import model.StockDataSource;
import view.IView;

//command of adding stock to portfolio
class HandleAddDateInvestment extends AbstractCommandsV2 {
  private String portfolioName;
  private String ticker;
  private LocalDate date;
  private int quantity;
  private StockDataSource source;

  HandleAddDateInvestment(Scanner s) {
    super(s);
  }

  HandleAddDateInvestment(String pn, String ticker, LocalDate date,
                          int quantity, StockDataSource source) {
    this.portfolioName = pn;
    this.ticker = ticker;
    this.date = date;
    this.quantity = quantity;
    this.source = source;
  }

  @Override
  public void prompterHelp(IView v) throws IOException {
    portfolioName = getInput(v, "Which portfolio would you like to add to? ");
    ticker = getTickerSymbol(v);
    date = getDatesInput(v, "add investment");

    quantity = Integer.parseInt(
            getInput(v, "How many whole (non-fractional) shares would you like to add? "));

    source = getSourceInput(v);
  }

  @Override
  public void execute(IModelV2 model, IView v) throws IOException {
    try {
      Investment stock = model.queryStock(ticker, source);
      model.addInvestmentToPortfolio(date, portfolioName, stock, quantity);
      v.showCustomMessage("Successfully added!\n");
    } catch (Exception e) {
      v.showCustomMessage("Error: " + e.getMessage() + "\n");
    }
  }

}
