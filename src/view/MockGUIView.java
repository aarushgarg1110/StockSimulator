package view;

import java.io.IOException;

import controller.Features;


/**
 * This class represents the mock of the view of the program in a GUI format.
 * It is used to verify that the callbacks work correctly.
 */
public class MockGUIView implements GUIView {
  private final Appendable out;


  /**
   * Initializes the mock.
   */
  public MockGUIView(Appendable out) {
    this.out = out;
  }

  @Override
  public void addFeatures(Features features) {
    QueryPortfolioPanel panel = new QueryPortfolioPanel(this);
    panel.addFeatures(features);
  }


  @Override
  public void showCustomMessage(String message) throws IOException {
    out.append("Showing custom message of: ").append(message);
  }

  @Override
  public void showMenu() throws IOException {
    out.append("We currently have the following features:\n"
            + "Create a Portfolio\n"
            + "Save a Portfolio\n"
            + "Load a Portfolio\n"
            + "Buy Stocks\n"
            + "Sell Stocks\n"
            + "Get the Value of your Portfolio\n"
            + "Get the Composition of your Portfolio\n"
            + "More features coming soon!");
  }

  @Override
  public void welcome() throws IOException {
    out.append("Welcome to the Investment Portfolio Manager!");
  }

  @Override
  public void goodbye() throws IOException {
    out.append("Goodbye!");
  }

}