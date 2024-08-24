package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * This class represents enhanced and new functionality for the existing SimplePortfolio
 * class. Allows the user to perform more operations with their portfolio.
 */
public class BetterPortfolio implements PortfolioV2 {
  private final SimplePortfolio delegate;
  private final List<Transactions> transactions;
  private final Map<String, Investment> investments;

  /**
   * Constructs an empty Portfolio object initialized with nothing.
   */
  public BetterPortfolio() {
    delegate = new SimplePortfolio();
    transactions = new ArrayList<>();
    investments = new HashMap<>();
  }

  @Override
  public double getEndOfDayVal(LocalDate date) {
    if (date.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Cannot buy an investment on a date later than today.");
    }
    if (transactions.isEmpty() || date.isBefore(transactions.get(0).getDate())) {
      return 0.0;
    }
    double total = 0.0;
    for (Map.Entry<String, Double> entry : getComposition(date).entrySet()) {
      Investment inv = investments.get(entry.getKey());
      total += inv.getEndOfDayVal(date) * entry.getValue();
    }
    return total;
  }

  @Override
  public void addInvestment(LocalDate date, Investment inv, double quantity)
          throws IllegalArgumentException {
    if (date.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Cannot buy an investment on a date later than today.");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Cannot buy negative shares.");
    }
    if (!transactions.isEmpty()) {
      if (date.isBefore(transactions.get(transactions.size() - 1).getDate())) {
        throw new IllegalArgumentException("Must conduct buys in chronological order");
      }
    }
    delegate.addInvestment(inv, quantity);
    Transactions t =
            new Transactions(inv.toString(), quantity, date, Transactions.Types.Buy);
    transactions.add(t);
    investments.putIfAbsent(inv.toString(), inv);
  }

  @Override
  public void sellInvestment(LocalDate date, String ticker, double quantity)
          throws IllegalArgumentException {
    if (date.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Cannot sell an investment on a date later than today.");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Cannot sell negative shares.");
    }
    if (!transactions.isEmpty()) {
      if (date.isBefore(transactions.get(transactions.size() - 1).getDate())) {
        throw new IllegalArgumentException("Must conduct sales in chronological order");
      } else {
        double availQuant = getAvailableQuantity(date, ticker);
        if (quantity > availQuant) {
          throw new IllegalArgumentException(
                  "Not enough shares to sell. "
                          + "Available: " + availQuant
                          + ", Requested: " + quantity);
        }
        delegate.sellInvestment(ticker, quantity);
        Transactions t = new Transactions(
                ticker, quantity, date, Transactions.Types.Sell);
        transactions.add(t);
      }
    }
  }

  //helper method for logic determining if there are enough shares to sell
  private double getAvailableQuantity(LocalDate date, String investmentName) {
    double quantity = 0;
    for (Transactions t : transactions) {
      if (t.getDate().isAfter(date)) {
        break;
      }
      if (t.getTicker().equals(investmentName)) {
        if (t.getType() == Transactions.Types.Buy) {
          quantity += t.getShares();
        } else if (t.getType() == Transactions.Types.Sell) {
          quantity -= t.getShares();
        }
      }
    }
    return quantity;
  }

  @Override
  public void reversal(LocalDate date, Investment inv, double quantity, String type)
          throws IllegalArgumentException {
    if (!type.equals("bought") && !type.equals("sold")) {
      throw new IllegalArgumentException("unrecognized type of original transaction");
    }

    Transactions.Types buyOrSell;
    if (type.equals("sold")) {
      buyOrSell = Transactions.Types.Sell;
    } else {
      buyOrSell = Transactions.Types.Buy;
    }

    boolean found = false;
    for (Transactions tr : transactions) {
      if (tr.getDate().isEqual(date)
              && tr.getType().equals(buyOrSell)
              && tr.getTicker().equals(inv.toString())) {
        found = true;
        break;
      }
    }
    if (!found) {
      throw new IllegalArgumentException("You cannot undo a transaction that never happened.");
    }

    if (type.equals("bought")) {
      if (!canReverseBuy(date, inv, quantity)) {
        throw new IllegalArgumentException("Reversal would invalidate future sales");
      }
      sellInvestment(date, inv.toString(), quantity);
    } else {
      addInvestment(date, inv, quantity);
    }
  }

  // Helper method to check if reversing a buy is possible without invalidating future sales
  private boolean canReverseBuy(LocalDate date, Investment inv, double quantity) {
    double hypotheticalQuant = getAvailableQuantity(date, inv.toString()) - quantity;
    for (Transactions t : transactions) {
      if (t.getDate().isAfter(date) && t.getType() == Transactions.Types.Sell) {
        if (hypotheticalQuant < t.getShares()) {
          return false;
        }
        hypotheticalQuant -= t.getShares();
      }
    }
    return true;
  }

  @Override
  public void rebalance(LocalDate date, Map<String, Integer> weights)
          throws IllegalArgumentException {
    if (date.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Cannot rebalance shares on a date later than today.");
    }
    //add up the weights provided
    int sum = 0;
    for (Map.Entry<String, Integer> entry : weights.entrySet()) {
      sum += entry.getValue();
    }
    //see if weights add up to 100
    if (sum != 100) {
      throw new IllegalArgumentException("Total weights provided do not add up to 100");
    }

    Map<String, Double> currentComposition = getComposition(date);
    Map<String, Double> currentDistribution = getDistributionVal(date);

    //see if there are enough weights to match number of investments
    if (weights.size() != currentComposition.size()) {
      throw new IllegalArgumentException(
              "Number of weights does not match the number of investments.");
    }
    //see if each investment in the portfolio has a specified or desired weight
    for (String ticker : currentComposition.keySet()) {
      if (!weights.containsKey(ticker)) {
        throw new IllegalArgumentException("Weight not provided for investment: " + ticker);
      }
    }

    //total value of portfolio
    double totalValue = getEndOfDayVal(date);

    //the current values of each stock
    Map<String, Double> currentValues = new HashMap<>();
    for (Map.Entry<String, Double> entry : currentDistribution.entrySet()) {
      double currentValue = entry.getValue();
      currentValues.put(entry.getKey(), currentValue);
    }
    //the target values of each stock
    Map<String, Double> targetValues = new HashMap<>();
    for (Map.Entry<String, Integer> entry : weights.entrySet()) {
      double targetValue = (entry.getValue() * totalValue) / 100.0;
      targetValues.put(entry.getKey(), targetValue);
    }

    //rebalancing logic
    for (Map.Entry<String, Double> entry : currentValues.entrySet()) {
      String ticker = entry.getKey();
      double currentValue = entry.getValue();
      double targetValue = targetValues.get(ticker);
      Investment investment = investments.get(ticker);

      if (currentValue > targetValue) {
        double sellQuantity = (currentValue - targetValue) / investment.getEndOfDayVal(date);
        sellInvestment(date, investment.toString(), sellQuantity);
        double newQuantity = currentComposition.get(ticker)
                - Math.round(sellQuantity * 100.0) / 100.0;
        currentComposition.put(ticker, newQuantity);
      } else if (currentValue < targetValue) {
        double buyQuantity = (targetValue - currentValue) / investment.getEndOfDayVal(date);
        addInvestment(date, investment, buyQuantity);
        double newQuantity = currentComposition.get(ticker)
                + Math.round(buyQuantity * 100.0) / 100.0;
        currentComposition.put(ticker, newQuantity);
      }
    }
  }

  @Override
  public Map<String, Double> getComposition(LocalDate date) {
    if (date.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Cannot buy an investment on a date later than today.");
    }
    Map<String, Double> composition = new HashMap<>();

    for (Transactions t : transactions) {
      if (t.getDate().isAfter(date)) {
        break;
      }
      composition.putIfAbsent(t.getTicker(), 0.0);
      double currQuant = composition.get(t.getTicker());
      double change = 0;
      if (t.getType().equals(Transactions.Types.Buy)) {
        change += t.getShares();
      } else {
        change -= t.getShares();
      }
      composition.put(t.getTicker(), currQuant + change);
    }
    return composition;
  }

  @Override
  public Map<String, Double> getDistributionVal(LocalDate date) {
    if (date.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Cannot buy an investment on a date later than today.");
    }
    Map<String, Double> composition = getComposition(date);
    Map<String, Double> distribution = new HashMap<>();
    for (Map.Entry<String, Double> entry : composition.entrySet()) {
      String ticker = entry.getKey();
      double quantity = entry.getValue();
      Investment stock = investments.get(ticker);

      double closingPrice = stock.getEndOfDayVal(date);
      double value = closingPrice * quantity;
      distribution.put(ticker, value);
    }
    return distribution;
  }

  @Override
  public void saveToFile(String filename) throws IOException {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.newDocument();

      Element root = doc.createElement("Portfolio");
      doc.appendChild(root);

      for (Transactions t : transactions) {
        Element transactionElement = doc.createElement("transaction");
        root.appendChild(transactionElement);

        Element ticker = doc.createElement("ticker");
        ticker.appendChild(doc.createTextNode(t.getTicker()));
        transactionElement.appendChild(ticker);

        Element quantity = doc.createElement("quantity");
        quantity.appendChild(doc.createTextNode(Double.toString(t.getShares())));
        transactionElement.appendChild(quantity);

        Element date = doc.createElement("date");
        date.appendChild(doc.createTextNode(t.getDate().toString()));
        transactionElement.appendChild(date);

        Element type = doc.createElement("type");
        type.appendChild(doc.createTextNode(t.getType().toString()));
        transactionElement.appendChild(type);
      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(filename));
      transformer.transform(source, result);
    } catch (javax.xml.transform.TransformerException
             | javax.xml.parsers.ParserConfigurationException e) {
      throw new IOException("Error: Could not save portfolio");
    }
  }

  @Override
  public List<Transactions> getTransactions() {
    return new ArrayList<>(transactions);
  }

  @Override
  public String getChart(LocalDate start, LocalDate end) {
    return new LineChartGenerator(start, end, this).generate();
  }

}
