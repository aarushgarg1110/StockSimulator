package controller;

import java.io.IOException;
import java.util.Scanner;

import model.IModel;
import model.Investment;
import model.StockDataSource;
import view.IView;

//command of adding stock to portfolio
class HandleAddInvestmentToPortfolio extends AbstractCommands {
  private String portfolioName;
  private String ticker;
  private int quantity;
  private StockDataSource source;

  HandleAddInvestmentToPortfolio(Scanner s) {
    super(s);
  }

  @Override
  public void prompterHelp(IView v) throws IOException {
    portfolioName = getInput(v, "Which portfolio would you like to add to? ");
    ticker = getTickerSymbol(v);
    quantity = Integer.parseInt(
            getInput(v, "How many whole (non-fractional) shares would you like to add? "));

    source = getSourceInput(v);
  }

  @Override
  public void execute(IModel model, IView v) throws IOException {
    try {
      Investment stock = model.queryStock(ticker, source);
      model.addInvestmentToPortfolio(portfolioName, stock, quantity);
      v.showCustomMessage("Successfully added!\n");
    } catch (Exception e) {
      v.showCustomMessage("Error: " + e.getMessage() + "\n");
    }
  }

}
