package view;

import controller.Features;

import java.awt.GridLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

//class representing the first panel of the gui that deals with creating, saving, loading portfolio
class PortfolioPanel extends AbstractPanel {
  private final JButton createPortfolioButton;
  private final JButton loadButton;
  private final JButton saveButton;
  private final JTextField portfolioNameField;
  private final JTextField portfolioNameField2;
  private final JTextField fileName;

  //constructor to build the panel
  PortfolioPanel(GUIView view) {
    super(view);
    this.setLayout(new GridLayout(2, 1));
    JPanel inputPanel = new JPanel(new FlowLayout());
    inputPanel.setBorder(BorderFactory.createTitledBorder("Create Portfolio"));
    portfolioNameField = new GhostText("Enter portfolio name...", 20);
    createPortfolioButton = new JButton("Create Portfolio");
    inputPanel.add(new JLabel("Portfolio Name:"));
    inputPanel.add(portfolioNameField);
    inputPanel.add(createPortfolioButton);
    this.add(inputPanel);

    loadButton = new JButton("Load");
    saveButton = new JButton("Save");
    portfolioNameField2 = new GhostText("Enter portfolio name...", 20);
    fileName = new GhostText("Enter file path...", 20);
    JPanel loadSavePanel = new JPanel(new FlowLayout());
    loadSavePanel.setBorder(BorderFactory.createTitledBorder("Load/Save Files"));
    loadSavePanel.add(new JLabel("Name of Portfolio to Load/Save: "));
    loadSavePanel.add(portfolioNameField2);
    loadSavePanel.add(new JLabel("File path to save to/load from: "));
    loadSavePanel.add(fileName);
    JPanel loadSave = new JPanel(new FlowLayout());
    loadSave.add(loadButton);
    loadSave.add(saveButton);
    loadSavePanel.add(loadSave);
    this.add(loadSavePanel);
  }

  //addFeatures method to activate the buttons
  void addFeatures(Features features) {
    createPortfolioButton.addActionListener(evt -> {
      try {
        features.createPortfolio(portfolioNameField.getText());
        clear(portfolioNameField);
      } catch (Exception e) {
        sendError(e);
      }
    });

    saveButton.addActionListener(evt -> {
      try {
        features.savePortfolio(portfolioNameField2.getText(), fileName.getText());
        clear(portfolioNameField2, fileName);
      } catch (Exception e) {
        sendError(e);
      }
    });

    loadButton.addActionListener(evt -> {
      try {
        features.loadPortfolio(fileName.getText(), portfolioNameField2.getText());
        clear(portfolioNameField2, fileName);
      } catch (Exception e) {
        sendError(e);
      }
    });
  }
}
