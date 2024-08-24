package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import model.IModel;
import model.StockDataSource;
import view.IView;

//command for getting moving avg of a stock
class HandleMovingAvg extends AbstractCommands {
  private String ticker;
  private LocalDate start;
  private int days;
  private StockDataSource source;

  HandleMovingAvg(Scanner s) {
    super(s);
  }

  @Override
  public void prompterHelp(IView v) throws IOException {
    ticker = getTickerSymbol(v);
    start = getDatesInput(v, "start the moving average from");
    days = getDaysBack(v);
    source = getSourceInput(v);
  }

  @Override
  public void execute(IModel model, IView v) throws IOException {
    try {
      double avg = model.queryStock(ticker, source).calculateXDayMovingAvg(start, days);
      v.showCustomMessage(days + " Days Moving Average: $" + avg + "\n");
    } catch (Exception e) {
      v.showCustomMessage(
              "Error: Insufficient/No data for those parameters. Please try again.\n");
    }
  }

}
