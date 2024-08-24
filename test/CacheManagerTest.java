import org.junit.Before;
import org.junit.Test;

import model.APIStockDataSource;
import model.CacheManager;
import model.FileStockDataSource;
import model.Investment;
import model.Stock;
import model.StockDataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * This is a test class for the CacheManager class.
 */
public class CacheManagerTest {
  //initialize it here so API query limit doesn't get overloaded with each test
  //only called once every time the entire test file has to load.
  StockDataSource apiSource = new APIStockDataSource();

  //will be using fileGoogle as it does not require internet
  StockDataSource fileGoogle = new FileStockDataSource(
          "res/Stocks/GOOG");
  Investment googleStock;
  CacheManager cacheManager;

  @Before
  public void setUp() {
    googleStock = new Stock("GOOG", fileGoogle);
    cacheManager = CacheManager.getInstance();
  }

  @Test
  //test singleton pattern by making sure only one instance exists
  public void testGetInstance() {
    // Get two instances of CacheManager
    CacheManager instance1 = CacheManager.getInstance();
    CacheManager instance2 = CacheManager.getInstance();

    // Ensure that both instances are the same
    assertEquals(instance1, instance2);
  }

  @Test
  //from the setup, google stock is already fetched, and should now be in local cache
  public void isDataCached() {
    assertTrue(cacheManager.isDataCached("GOOG"));

    //new stock desired not in cache
    assertFalse(cacheManager.isDataCached("AAPL"));

    new Stock("AAPL", apiSource);

    //new stock now in cache for future use
    assertTrue(cacheManager.isDataCached("AAPL"));
  }
}