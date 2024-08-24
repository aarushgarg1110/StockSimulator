package view;

import java.awt.GridLayout;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Features;


/**
 * This class represents the View of the program in a GUI format.
 * It displays the features of this application in a user-friendly visual
 * application.
 */
public class GraphicsView2 implements GUIView {
  private PortfolioPanel createPortfolioSaveLoadPanel;
  private BuySellStocksPanel buySellStockPanel;
  private QueryPortfolioPanel queryPortfolioPanel;
  private JFrame frame;


  /**
   * Initializes the frame of the gui with all the panels.
   */
  public GraphicsView2() {
    initialize();
  }

  //set all the panels up accordingly
  private void initialize() {
    frame = new JFrame("Investment Application");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);

    JPanel mainPanel = new JPanel(new GridLayout(1, 3));

    createPortfolioSaveLoadPanel = new PortfolioPanel(this);

    buySellStockPanel = new BuySellStocksPanel(this);

    queryPortfolioPanel = new QueryPortfolioPanel(this);

    mainPanel.add(createPortfolioSaveLoadPanel);
    mainPanel.add(buySellStockPanel);
    mainPanel.add(queryPortfolioPanel);

    frame.add(mainPanel);
    frame.pack();
    frame.setVisible(true);

    welcome();
    showMenu();
  }

  @Override
  public void addFeatures(Features features) {
    this.createPortfolioSaveLoadPanel.addFeatures(features);
    this.queryPortfolioPanel.addFeatures(features);
    this.buySellStockPanel.addFeatures(features);
  }


  @Override
  public void showCustomMessage(String message) {
    queryPortfolioPanel.displayResult(message);
  }

  @Override
  public void showMenu() {
    JOptionPane.showMessageDialog(frame, "We currently have the following features:\n"
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
  public void welcome() {
    JOptionPane.showMessageDialog(frame, "Welcome to the Investment Portfolio Manager!");
  }

  @Override
  public void goodbye() {
    JOptionPane.showMessageDialog(frame, "Goodbye!");
  }

}