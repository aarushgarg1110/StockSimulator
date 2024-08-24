package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import model.IModel;
import model.IModelV2;
import view.IView;

/**
 * This class represents the controller of an interactive text-based investment's application.
 */
public class BetterInvestmentController extends AbstractInvestmentController {
  protected final Map<String, Function<Scanner, StocksCommandsV2>> knownCommands2;

  /**
   * Create a controller to work with the specified investment (model)
   * and view (to take and display inputs).
   *
   * @param in    the readable input being passed in
   * @param model the portfolio manager to work with (the model)
   * @param view  the text view object for inputs and outputs (the view)
   */
  public BetterInvestmentController(Readable in, IModel model, IView view) {
    super(in, model, view);
    this.knownCommands2 = new HashMap<>();
    knownCommands2.put("5", HandleAddDateInvestment::new);
    knownCommands2.put("6", HandleSellInvestment::new);
    knownCommands2.put("7", HandleRebalance::new);
    knownCommands2.put("8", HandleReverse::new);
    knownCommands2.put("C", HandleComposition::new);
    knownCommands2.put("D", HandleDistribution::new);
    knownCommands2.put("S", HandleSaveFile::new);
    knownCommands2.put("L", HandleLoadFile::new);
    knownCommands2.put("X", HandlePortfolioChart::new);
    knownCommands2.put("XS", HandleStockChart::new);

    knownCommands.put("9", HandleGetPortfolioValue::new);
  }

  @Override
  protected void startHelp(IView view, Scanner scan, String in)
          throws IllegalStateException, IOException {

    StocksCommandsV2 c2;
    if (in.equals("Q")) {
      view.goodbye();
      running = false;
      return;
    }
    Function<Scanner, StocksCommandsV2> cmd =
            knownCommands2.getOrDefault(in, null);
    if (cmd != null) {
      c2 = cmd.apply(scan);
      c2.prompter(view);
      c2.execute((IModelV2) model, view);
      view.showMenu();
    } else {
      originalCommands(in, scan);
    }
  }
}

