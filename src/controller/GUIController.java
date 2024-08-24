package controller;

import java.io.IOException;
import java.time.LocalDate;

import model.IModel;
import model.StockDataSource;
import view.GUIView;

/**
 * This class represents the controller of a graphical user interface of
 * this investment application.
 */
public class GUIController implements IGUIController, Features {
  private final IModel model;
  private GUIView view;

  /**
   * Constructs a GUIController with a model to refer to.
   *
   * @param m the model to be referred to
   */
  public GUIController(IModel m) {
    this.model = m;
  }

  @Override
  public void setView(GUIView v) {
    view = v;
    view.addFeatures(this);
  }

  @Override
  public void createPortfolio(String name) throws IOException {
    execute(new HandleCreatePortfolio(name));
  }

  @Override
  public void addStock(
          LocalDate date, String portfolioName, String ticker, StockDataSource s, int quantity)
          throws IOException {
    execute(new HandleAddDateInvestment(portfolioName, ticker, date, quantity, s));
  }

  @Override
  public void sellStock(
          LocalDate date, String portfolioName, String ticker, double quantity)
          throws IOException {
    execute(new HandleSellInvestment(portfolioName, ticker, date, quantity));
  }

  @Override
  public void getValue(LocalDate date, String portfolioName) throws IOException {
    execute(new HandleGetPortfolioValue(portfolioName, date));
  }

  @Override
  public void getComposition(LocalDate date, String portfolioName) throws IOException {
    execute(new HandleComposition(portfolioName, date));
  }

  @Override
  public void savePortfolio(String portfolioName, String fileName) throws IOException {
    execute(new HandleSaveFile(portfolioName, fileName));
  }

  @Override
  public void loadPortfolio(String filename, String name) throws IOException {
    execute(new HandleLoadFile(filename, name));
  }

  //helper method to abstract the common execution of a command class instance
  private void execute(StocksCommands c) throws IOException {
    c.execute(model, view);
  }

}
