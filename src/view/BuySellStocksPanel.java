package view;

import controller.Features;
import model.APIStockDataSource;
import model.FileStockDataSource;
import model.StockDataSource;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.ButtonGroup;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;

//class representing the second panel of the gui that deals with buying and selling stocks
class BuySellStocksPanel extends AbstractPanel {
  private final JButton buyStockButton;
  private final JButton sellStockButton;
  private final JRadioButton localFileButton;
  private final JTextField dataSourceFileField;
  private boolean useAPI;
  private final JTextField yearField;
  private final JTextField monthField;
  private final JTextField dayField;
  private final JTextField portfolioNameField;
  private final JTextField tickerField;
  private final JTextField sellQuantityField;
  private final JTextField buyQuantityField;

  //constructor to build the panel
  BuySellStocksPanel(GUIView view) {
    super(view);
    this.setLayout(new GridLayout(2, 1));

    // Top panel for inputs
    JPanel topPanel = new JPanel(new GridBagLayout());
    topPanel.setBorder(BorderFactory.createTitledBorder("Buy/Sell Stock"));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    tickerField = new GhostText("Enter ticker...", 10);
    portfolioNameField = new GhostText("Enter portfolio name...", 20);
    yearField = new GhostText("YYYY", 4);
    monthField = new GhostText("MM", 3);
    dayField = new GhostText("DD", 3);

    gbc.gridx = 0;
    gbc.gridy = 0;
    topPanel.add(new JLabel("Stock Ticker:"), gbc);
    gbc.gridx = 1;
    topPanel.add(tickerField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    topPanel.add(new JLabel("Portfolio Name:"), gbc);
    gbc.gridx = 1;
    topPanel.add(portfolioNameField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    topPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);

    JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    datePanel.add(yearField);
    datePanel.add(new JLabel("-"));
    datePanel.add(monthField);
    datePanel.add(new JLabel("-"));
    datePanel.add(dayField);

    gbc.gridx = 1;
    topPanel.add(datePanel, gbc);

    this.add(topPanel);

    // Bottom panel for buy/sell actions
    JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 5, 0));

    // Buy panel
    JPanel buyPanel = new JPanel(new GridBagLayout());
    buyPanel.setBorder(BorderFactory.createTitledBorder("Buy"));
    buyQuantityField = new GhostText("e.g. 1", 5);
    buyStockButton = new JButton("Buy Stock");

    JRadioButton apiButton = new JRadioButton("API");
    localFileButton = new JRadioButton("File");
    ButtonGroup dataSourceButtonGroup = new ButtonGroup();
    dataSourceButtonGroup.add(apiButton);
    dataSourceButtonGroup.add(localFileButton);

    dataSourceFileField = new GhostText("Enter file path...", 20);
    dataSourceFileField.setVisible(false);

    gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 0;
    buyPanel.add(new JLabel("# of Whole Shares:"), gbc);
    gbc.gridy = 1;
    buyPanel.add(buyQuantityField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    buyPanel.add(new JLabel("Data Source:"), gbc);

    JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    radioPanel.add(apiButton);
    radioPanel.add(localFileButton);

    gbc.gridx = 1;
    gbc.gridy = 2;
    buyPanel.add(radioPanel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    buyPanel.add(dataSourceFileField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    buyPanel.add(buyStockButton, gbc);

    // Sell panel
    JPanel sellPanel = new JPanel(new GridBagLayout());
    sellPanel.setBorder(BorderFactory.createTitledBorder("Sell"));
    sellQuantityField = new GhostText("e.g 1.5...", 5);
    sellStockButton = new JButton("Sell Stock");

    gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 0;
    sellPanel.add(new JLabel("# of Partial/Whole Shares:"), gbc);
    gbc.gridy = 1;
    sellPanel.add(sellQuantityField, gbc);
    gbc.gridy = 2;
    sellPanel.add(sellStockButton, gbc);

    bottomPanel.add(buyPanel);
    bottomPanel.add(sellPanel);

    this.add(bottomPanel);

    apiButton.addItemListener(new RadioButtonListener());
    localFileButton.addItemListener(new RadioButtonListener());
  }

  //generate the source based on what the user has selected
  StockDataSource sourceGenerator() {
    if (this.useAPI) {
      return new APIStockDataSource();
    }
    return new FileStockDataSource(dataSourceFileField.getText());
  }

  //class to manage the radio button, only show file text field if file button selected
  private class RadioButtonListener implements ItemListener {
    @Override
    public void itemStateChanged(ItemEvent e) {
      if (localFileButton.isSelected()) {
        dataSourceFileField.setVisible(true);
        useAPI = false;
      } else {
        dataSourceFileField.setVisible(false);
        useAPI = true;
      }
      BuySellStocksPanel.this.revalidate();
      BuySellStocksPanel.this.repaint();
    }
  }

  //addFeatures method to activate the buttons
  void addFeatures(Features features) {
    buyStockButton.addActionListener(evt -> {
      try {
        LocalDate date = getDate(yearField, monthField, dayField);
        features.addStock(
                date,
                portfolioNameField.getText(),
                tickerField.getText(),
                sourceGenerator(),
                Integer.parseInt(buyQuantityField.getText())
        );
        clear(yearField, monthField, dayField, portfolioNameField, tickerField);
      } catch (Exception e) {
        sendError(e);
      }
    });

    sellStockButton.addActionListener(evt -> {
      try {
        LocalDate date = getDate(yearField, monthField, dayField);
        features.sellStock(
                date,
                portfolioNameField.getText(),
                tickerField.getText(),
                Double.parseDouble(sellQuantityField.getText())
        );
        clear(yearField, monthField, dayField, portfolioNameField, tickerField);
      } catch (Exception e) {
        sendError(e);
      }
    });
  }
}
