package view;

import controller.Features;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.time.LocalDate;

//class representing the last panel of the gui that deals with querying a portfolio
class QueryPortfolioPanel extends AbstractPanel {
  private final JButton queryValueButton;
  private final JTextField queryValuePortfolioNameField;
  private final JTextField queryValueYearField;
  private final JTextField queryValueMonthField;
  private final JTextField queryValueDayField;
  private final JButton queryCompositionButton;
  private final JPanel resultsPanel;

  //constructor to build the panel
  QueryPortfolioPanel(GUIView view) {
    super(view);
    this.setLayout(new GridLayout(2, 1));
    JPanel queryPanel = new JPanel(new FlowLayout());
    queryPanel.setBorder(BorderFactory.createTitledBorder("Query Portfolio"));
    queryCompositionButton = new JButton("Get Composition");
    queryValueButton = new JButton("Get Value");

    queryValuePortfolioNameField = new GhostText("Enter portfolio name...", 20);
    queryPanel.add(new JLabel("Portfolio Name:"));
    queryPanel.add(queryValuePortfolioNameField);
    JPanel dateInputPanel = new JPanel(new FlowLayout());
    dateInputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
    queryValueYearField = new GhostText("YYYY",  4);
    queryValueMonthField = new GhostText("MM",  3);
    queryValueDayField = new GhostText("DD",  3);
    dateInputPanel.add(queryValueYearField);
    dateInputPanel.add(new JLabel("-"));
    dateInputPanel.add(queryValueMonthField);
    dateInputPanel.add(new JLabel("-"));
    dateInputPanel.add(queryValueDayField);
    queryPanel.add(dateInputPanel);
    JPanel buttons = new JPanel(new FlowLayout());
    buttons.add(queryValueButton);
    buttons.add(queryCompositionButton);
    queryPanel.add(buttons);
    this.add(queryPanel);

    resultsPanel = new JPanel(new FlowLayout());
    resultsPanel.setBorder(BorderFactory.createTitledBorder("Action Results"));
    this.add(resultsPanel);
  }

  //update the results panel
  protected void displayResult(String message) {
    resultsPanel.removeAll();

    JTextArea textArea = new JTextArea(message);
    textArea.setWrapStyleWord(true);
    textArea.setLineWrap(true);
    textArea.setEditable(false);
    textArea.setCaretPosition(0);

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new java.awt.Dimension(400, 200));
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    resultsPanel.add(scrollPane, BorderLayout.CENTER);

    resultsPanel.revalidate();
    resultsPanel.repaint();
  }

  //addFeatures method to activate the buttons
  void addFeatures(Features features) {
    queryValueButton.addActionListener(evt -> {
      try {
        LocalDate date = getDate(queryValueYearField, queryValueMonthField, queryValueDayField);
        features.getValue(
                date,
                queryValuePortfolioNameField.getText()
        );
        clear(queryValueYearField, queryValueMonthField,
                queryValueDayField, queryValuePortfolioNameField);
      } catch (Exception e) {
        sendError(e);
      }
    });

    queryCompositionButton.addActionListener(evt -> {
      try {
        LocalDate date = getDate(queryValueYearField, queryValueMonthField, queryValueDayField);
        features.getComposition(
                date,
                queryValuePortfolioNameField.getText()
        );
        clear(queryValueYearField, queryValueMonthField,
                queryValueDayField, queryValuePortfolioNameField);
      } catch (Exception e) {
        sendError(e);
      }
    });
  }
}
