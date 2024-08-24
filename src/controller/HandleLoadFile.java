package controller;

import java.io.IOException;
import java.util.Scanner;

import model.IModelV2;
import view.IView;

//command of loading a portfolio from a file
class HandleLoadFile extends AbstractCommandsV2 {
  private String path;
  private String name;

  HandleLoadFile(Scanner s) {
    super(s);
  }

  HandleLoadFile(String path, String name) {
    this.path = path;
    this.name = name;
  }

  @Override
  public void prompterHelp(IView v) throws IOException {
    String xml = "\n<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
            + "<Portfolio>\n"
            + "\t<transaction>\n"
            + "\t\t<ticker>AAPL</ticker>\n"
            + "\t\t<quantity>50.0</quantity>\n"
            + "\t\t<date>2022-06-01</date>\n"
            + "\t\t<type>Buy</type>\n"
            + "\t</transaction>\n"
            + "\t<transaction>\n"
            + "\t\t<ticker>GOOG</ticker>\n"
            + "\t\t<quantity>20.0</quantity>\n"
            + "\t\t<date>2022-06-02</date>\n"
            + "\t\t<type>Sell</type>\n"
            + "\t</transaction>\n"
            + "\t<transaction>\n"
            + "\t\t<ticker>MSFT</ticker>\n"
            + "\t\t<quantity>100.0</quantity>\n"
            + "\t\t<date>2022-06-03</date>\n"
            + "\t\t<type>Buy</type>\n"
            + "\t</transaction>\n"
            + "</Portfolio>";

    v.showCustomMessage(
            "When loading your portfolio, historical data for each stock "
                    + "\nwill be fetched from the AlphaVantage API!");
    v.showCustomMessage(" Please make sure your XML file follows this format:\n" + xml);
    path = getInput(
            v, "What is the absolute path on your computer of the portfolio "
                    + "you would like to load? ");
    name = getInput(v, "What would you like to name this portfolio? ");
  }

  @Override
  public void execute(IModelV2 model, IView v) throws IOException {
    try {
      model.loadPortfolio(path, name);
      v.showCustomMessage("Successfully loaded your portfolio!");
    } catch (Exception e) {
      v.showCustomMessage("Error: " + e.getMessage() + "\n");
    }
  }

}
