package controller;

import java.io.IOException;
import java.util.Scanner;

import model.IModel;
import view.IView;

//command for creating a portfolio
class HandleCreatePortfolio extends AbstractCommands {
  private String portfolioName;

  HandleCreatePortfolio(Scanner s) {
    super(s);
  }

  HandleCreatePortfolio(String portfolioName) {
    this.portfolioName = portfolioName;
  }

  @Override
  public void prompterHelp(IView v) throws IOException {
    portfolioName = getInput(v, "What name would you like to give your portfolio? ");
  }

  @Override
  public void execute(IModel model, IView v) throws IOException {
    try {
      model.createPortfolio(portfolioName);
      v.showCustomMessage("Successfully created!\n");
    } catch (Exception e) {
      v.showCustomMessage("Error: " + e.getMessage() + "\n");
    }
  }

}
