import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import model.Transactions;

import static org.junit.Assert.assertEquals;

/**
 * This is a test class for the Transactions class.
 */
public class TransactionsTest {
  Transactions buyGoogle;
  Transactions sellApple;

  @Before
  public void setUp() {
    buyGoogle = new Transactions(
            "GOOG", 10, LocalDate.parse("2024-05-21"), Transactions.Types.Buy);
    sellApple = new Transactions(
            "AAPL", 2.4, LocalDate.parse("2024-05-29"), Transactions.Types.Sell);
  }

  @Test
  public void getTicker() {
    assertEquals("GOOG", buyGoogle.getTicker());
    assertEquals("AAPL", sellApple.getTicker());
  }

  @Test
  public void getShares() {
    assertEquals(10, buyGoogle.getShares(), 0.01);
    assertEquals(2.4, sellApple.getShares(), 0.01);
  }

  @Test
  public void getDate() {
    assertEquals(LocalDate.parse("2024-05-21"), buyGoogle.getDate());
    assertEquals(LocalDate.parse("2024-05-29"), sellApple.getDate());
  }

  @Test
  public void getType() {
    assertEquals(Transactions.Types.Buy, buyGoogle.getType());
    assertEquals(Transactions.Types.Sell, sellApple.getType());
  }

  @Test
  public void testToString() {
    assertEquals("Ticker: GOOG\tShares: 10.0\tDate: 2024-05-21\tType: Buy", buyGoogle.toString());
    assertEquals("Ticker: AAPL\tShares: 2.4\tDate: 2024-05-29\tType: Sell", sellApple.toString());
  }

  @Test
  public void testEquals() {
    Transactions first = new Transactions(
            "GOOG", 30, LocalDate.of(2023, 6, 3), Transactions.Types.Buy);

    Transactions second = new Transactions(
            "GOOG", 30, LocalDate.of(2023, 6, 3), Transactions.Types.Buy);

    assertEquals(first, second);
  }

  @Test
  public void testHashCode() {
    Transactions first = new Transactions(
            "GOOG", 30, LocalDate.of(2023, 6, 3), Transactions.Types.Buy);

    Transactions second = new Transactions(
            "GOOG", 30, LocalDate.of(2023, 6, 3), Transactions.Types.Buy);

    assertEquals(first.hashCode(), second.hashCode());
  }
}