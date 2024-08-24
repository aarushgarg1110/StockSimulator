package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import model.IModelV2;
import model.Investment;
import model.StockDataSource;
import model.Transactions;
import view.IView;

//command of reversing a transaction
class HandleReverse extends AbstractCommandsV2 {
  private String portfolioName;
  private String ticker;
  private LocalDate date;
  private double quantity;
  private String type;
  private StockDataSource source;

  HandleReverse(Scanner s) {
    super(s);
  }

  @Override
  public void prompterHelp(IView v) throws IOException {
    portfolioName = getInput(
            v, "In which portfolio would you like to undo a transaction? ");
  }

  //since we want to display the transactions list before getting more inputs,
  //we need a second prompter to be called after
  private void prompter2(IView v) throws IOException {
    ticker = getTickerSymbol(v);
    date = getDatesInput(v, "undo a transaction");
    quantity = Double.parseDouble(
            getInput(v, "How many shares would you like to undo? "));
    type = getInput(v, "Were those shares previously 'bought' or 'sold'? ");

    source = getSourceInput(v);
  }

  @Override
  public void execute(IModelV2 model, IView v) throws IOException {
    try {
      List<Transactions> transactionLog = model.getTransactions(portfolioName);

      v.showCustomMessage("Transactions in portfolio " + portfolioName + ":\n");
      for (int i = 0; i < transactionLog.size(); i++) {
        v.showCustomMessage(transactionLog.toString());
      }
      v.showCustomMessage("\n");

      prompter2(v);

      Investment stock = model.queryStock(ticker, source);
      model.reverseTransaction(date, portfolioName, stock, quantity, type);
      v.showCustomMessage("Successfully undone!\n");
    } catch (Exception e) {
      v.showCustomMessage("Error: " + e.getMessage() + "\n");
    }
  }

}
