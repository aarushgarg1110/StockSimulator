package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * One way for the program to obtain stock data is through a specified file path of a csv file.
 * This class represents data obtained through local file paths, not needing an API
 */
public final class FileStockDataSource extends AbstractDataSource {
  private final String filePath;

  /**
   * Constructs a FileStockDataSource object with a filepath specified by user.
   *
   * @param filePath the file path on the computer that refers to a csv file
   */
  public FileStockDataSource(String filePath) {
    this.filePath = filePath;
  }

  @Override
  public List<Stock.StockData> fetchNewData(String stockSymbol) {
    try {
      File file = new File(filePath);
      Scanner sc = new Scanner(file);
      return scanDataToList(sc, stockSymbol);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File not found: " + filePath);
    }
  }
}
