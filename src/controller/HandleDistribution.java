package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import model.IModelV2;
import view.IView;

//command of getting the distribution of values of a portfolio
class HandleDistribution extends AbstractCommandsV2 {
  private String portfolioName;
  private LocalDate date;

  HandleDistribution(Scanner s) {
    super(s);
  }

  @Override
  public void prompterHelp(IView v) throws IOException {
    portfolioName = getInput(
            v, "Which portfolio would you like to find the distribution of values of? ");
    date = getDatesInput(v, "find the distribution of values");
  }

  @Override
  public void execute(IModelV2 model, IView v) throws IOException {
    try {
      displayDistribution(model, v, date, portfolioName);
    } catch (Exception e) {
      v.showCustomMessage("Error: " + e.getMessage() + "\n");
    }
  }

}
