package controller;

import java.io.IOException;
import java.util.Scanner;

import model.IModel;
import view.IView;

/**
 * This class represents the controller of an interactive text-based investment's application.
 */
public class InvestmentController extends AbstractInvestmentController {

  /**
   * Create a controller to work with the specified investment (model)
   * and view (to take and display inputs).
   *
   * @param in    the readable input being passed in
   * @param model the portfolio manager to work with (the model)
   * @param view  the text view object for inputs and outputs (the view)
   */
  public InvestmentController(Readable in, IModel model, IView view) {
    super(in, model, view);
    knownCommands.put("5", HandleAddInvestmentToPortfolio::new);
    knownCommands.put("6", HandleGetPortfolioValue::new);
  }

  @Override
  protected void startHelp(IView view, Scanner scan, String in)
          throws IllegalStateException, IOException {

    if (in.equals("7")) {
      view.goodbye();
      running = false;
      return;
    }
    originalCommands(in, scan);
  }
}

