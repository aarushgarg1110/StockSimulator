package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import model.IModelV2;
import view.IView;

//command of producing a chart of a portfolio's performance
class HandlePortfolioChart extends AbstractCommandsV2 {
  private String portfolioName;
  private LocalDate start;
  private LocalDate end;

  HandlePortfolioChart(Scanner s) {
    super(s);
  }

  @Override
  public void prompterHelp(IView v) throws IOException {
    portfolioName = getInput(
            v, "Which portfolio would you like to see the performance of? ");
    start = getDatesInput(v, "start examining the performance");
    end = getDatesInput(v, "end examining the performance\n");
  }

  @Override
  public void execute(IModelV2 model, IView v) throws IOException {
    try {
      v.showCustomMessage(String.format(
              "Performance of portfolio %s from %s to %s", portfolioName, start, end));
      String output = model.getPortfolioChart(start, end, portfolioName);
      v.showCustomMessage("\n" + output + "\n");
    } catch (Exception e) {
      v.showCustomMessage("Error: " + e.getMessage() + "\n");
    }
  }

}
