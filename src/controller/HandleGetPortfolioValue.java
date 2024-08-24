package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import model.IModel;
import view.IView;

//command for getting value of portfolio
class HandleGetPortfolioValue extends AbstractCommands {
  private String portfolioName;
  private LocalDate date;

  HandleGetPortfolioValue(Scanner s) {
    super(s);
  }

  HandleGetPortfolioValue(String pn, LocalDate date) {
    this.portfolioName = pn;
    this.date = date;
  }

  @Override
  public void prompterHelp(IView v) throws IOException {
    portfolioName = getInput(v, "Which portfolio would you like to find the value of? ");
    date = getDatesInput(v, "find the value");
  }

  @Override
  public void execute(IModel model, IView v) throws IOException {
    try {
      double value = model.getValueOfPortfolio(portfolioName, date);
      v.showCustomMessage(portfolioName + " was valued at $" + value + " on that date\n");
    } catch (Exception e) {
      v.showCustomMessage("Error: " + e.getMessage() + "\n");
    }
  }

}
