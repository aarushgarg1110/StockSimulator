package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import model.IModelV2;
import view.IView;

//command of selling stock from portfolio
class HandleSellInvestment extends AbstractCommandsV2 {
  private String portfolioName;
  private String ticker;
  private LocalDate date;
  private double quantity;

  HandleSellInvestment(Scanner s) {
    super(s);
  }

  HandleSellInvestment(String pn, String ticker, LocalDate date, double quantity) {
    this.portfolioName = pn;
    this.ticker = ticker;
    this.date = date;
    this.quantity = quantity;
  }

  @Override
  public void prompterHelp(IView v) throws IOException {
    portfolioName = getInput(v, "Which portfolio would you like to sell from? ");
    ticker = getTickerSymbol(v);
    date = getDatesInput(v, "sell investment");
    quantity = Double.parseDouble(
            getInput(v, "How many shares would you like to sell? "));
  }

  @Override
  public void execute(IModelV2 model, IView v) throws IOException {
    try {
      model.sellInvestmentFromPortfolio(date, portfolioName, ticker, quantity);
      v.showCustomMessage("Successfully sold!\n");
    } catch (Exception e) {
      v.showCustomMessage("Error: " + e.getMessage() + "\n");
    }
  }

}
