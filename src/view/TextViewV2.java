package view;

import java.io.IOException;

/**
 * This class represents an advanced TextViewV2 of the program in a text format
 * It displays more options than TextView.
 */
public class TextViewV2 extends TextView {

  /**
   * Constructs a TextViewV2 object that displays an output.
   *
   * @param out the appendable output to be displayed
   */
  public TextViewV2(Appendable out) {
    super(out);
  }

  @Override
  public void showMenu() throws IOException {
    super.showCustomMessage("\nWhat would you like to do today?\n");
    super.showCustomMessage("1. Examine the gain or loss of a stock\n");
    super.showCustomMessage("2. Examine the x-day moving average of a stock\n");
    super.showCustomMessage("3. Find X-Day Crossovers for a stock\n");
    super.showCustomMessage("4. Create Portfolio\n");
    super.showCustomMessage("5. Add Stock to Portfolio\n");
    super.showCustomMessage("6. Sell Stock from Portfolio\n");
    super.showCustomMessage("7. Rebalance Portfolio with given weights\n");
    super.showCustomMessage("8. Undo a previous transaction (mistake)\n");
    super.showCustomMessage("9. Get Portfolio Value\n");
    super.showCustomMessage("C. Get Portfolio Composition (list of stocks and shares)\n");
    super.showCustomMessage(
            "D. Get Portfolio Distribution of Values (list of stocks and values)\n");
    super.showCustomMessage("S. Save a Portfolio\n");
    super.showCustomMessage("L. Load a Portfolio from an XML file\n");
    super.showCustomMessage("X. See the performance of a portfolio over time\n");
    super.showCustomMessage("XS. See the performance of a stock over time\n");
    super.showCustomMessage("Q. Exit\n");
    super.showCustomMessage("Choose an option by entering the corresponding number: ");
  }
}
