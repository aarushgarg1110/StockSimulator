package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;

import model.IModelV2;
import view.IView;

//command of getting the composition of a portfolio
class HandleComposition extends AbstractCommandsV2 {
  private String portfolioName;
  private LocalDate date;

  HandleComposition(Scanner s) {
    super(s);
  }

  HandleComposition(String pn, LocalDate date) {
    this.portfolioName = pn;
    this.date = date;
  }

  @Override
  public void prompterHelp(IView v) throws IOException {
    portfolioName = getInput(
            v, "Which portfolio would you like to find the composition of? ");
    date = getDatesInput(v, "find the composition of this portfolio");
  }

  @Override
  public void execute(IModelV2 model, IView v) throws IOException {
    try {
      Map<String, Double> comp = model.getComposition(date, portfolioName);

      StringBuilder message = new StringBuilder();
      message.append(portfolioName).append(" composition on ").append(date).append(":\n");
      for (Map.Entry<String, Double> entry : comp.entrySet()) {
        message.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
      }

      v.showCustomMessage(message.toString());
    } catch (Exception e) {
      v.showCustomMessage("Error: " + e.getMessage() + "\n");
    }
  }

}
