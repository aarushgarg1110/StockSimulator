package model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * This class represents a transaction object. A transaction holds information such as
 * stock involved, amount of shares, date of transaction, and type of transaction
 */
public class Transactions {

  /**
   * Enum simply stating if some transaction was a buy or a sale.
   */
  public enum Types {
    Buy, Sell;
  }

  private final String ticker;
  private final double shares;
  private final LocalDate date;
  private final Types type;

  /**
   * Constructs a Transaction object serving as a log of a transaction made in a portfolio.
   *
   * @param ticker the ticker of the stock involved in the transaction
   * @param shares the number of shares involved in the transaction
   * @param date   the date on which this stock was bought or sold
   * @param type   whether this stock was bought or sold
   */
  public Transactions(String ticker, double shares, LocalDate date, Types type) {
    this.ticker = ticker;
    this.shares = shares;
    this.date = date;
    this.type = type;
  }

  /**
   * Convenient getter for referencing stock involved in transaction.
   *
   * @return String ticker
   */
  public String getTicker() {
    return ticker;
  }

  /**
   * Convenient getter for referencing shares involved in transaction.
   *
   * @return number of shares
   */
  public double getShares() {
    return shares;
  }

  /**
   * Convenient getter for referencing date involved in transaction.
   *
   * @return date of transaction
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Convenient getter for referencing type of transaction.
   *
   * @return Transaction type
   */
  public Types getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Ticker: " + ticker + "\tShares: " + shares + "\tDate: " + date + "\tType: " + type;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Transactions)) {
      return false;
    }
    Transactions that = (Transactions) obj;

    return this.toString().equals(that.toString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticker, shares, date, type);
  }
}
