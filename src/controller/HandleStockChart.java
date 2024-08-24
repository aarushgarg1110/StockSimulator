package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import model.IModelV2;
import model.StockDataSource;
import view.IView;

//command of producing a stock performance chart
class HandleStockChart extends AbstractCommandsV2 {
  private String ticker;
  private StockDataSource s;
  private LocalDate start;
  private LocalDate end;

  HandleStockChart(Scanner s) {
    super(s);
  }

  @Override
  public void prompterHelp(IView v) throws IOException {
    ticker = getInput(
            v, "Which stock would you like to see the performance of? ");
    s = getSourceInput(v);
    start = getDatesInput(v, "start examining the performance");
    end = getDatesInput(v, "end examining the performance\n");
  }

  @Override
  public void execute(IModelV2 model, IView v) throws IOException {
    try {
      v.showCustomMessage(String.format(
              "Performance of stock %s from %s to %s", ticker, start, end));
      String output = model.getStockChart(start, end, ticker, s);
      v.showCustomMessage("\n" + output + "\n");
    } catch (Exception e) {
      v.showCustomMessage("Error: " + e.getMessage() + "\n");
    }
  }

}
