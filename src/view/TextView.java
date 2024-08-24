package view;

import java.io.IOException;

/**
 * This class represents the View of the program in a text format
 * It utilizes Appendable to display an interactive
 * text interface in which the user can
 * type instructions to retrieve and examine data regarding investments.
 */
public class TextView implements IView {
  private final Appendable out;

  /**
   * Constructs a TextView object that displays an output.
   *
   * @param appendable the appendable output to be displayed
   */
  public TextView(Appendable appendable) {
    out = appendable;
  }

  @Override
  public void showCustomMessage(String message) throws IOException {
    out.append(message);
  }

  @Override
  public void showMenu() throws IOException {
    out.append("\nWhat would you like to do today?\n");
    out.append("1. Examine the gain or loss of a stock\n");
    out.append("2. Examine the x-day moving average of a stock\n");
    out.append("3. Find X-Day Crossovers for a stock\n");
    out.append("4. Create Portfolio\n");
    out.append("5. Add Stock to Portfolio\n");
    out.append("6. Get Portfolio Value\n");
    out.append("7. Exit\n");
    out.append("Choose an option by entering the corresponding number: ");
  }

  @Override
  public void welcome() throws IOException {
    out.append("Welcome to the Investment Portfolio Manager!\n");
  }

  @Override
  public void goodbye() throws IOException {
    out.append("Goodbye!\n");
  }
}
