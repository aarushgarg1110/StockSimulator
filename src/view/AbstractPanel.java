package view;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.JPanel;
import javax.swing.JTextField;

//abstract class to reduce common code for certain panels
abstract class AbstractPanel extends JPanel {
  protected GUIView view;

  //give the panel the view it will be on
  protected AbstractPanel(GUIView view) {
    this.view = view;
  }

  //helper to get the date from any text fields in a panel
  protected LocalDate getDate(JTextField y, JTextField m, JTextField d) {
    int year = Integer.parseInt(y.getText());
    int month = Integer.parseInt(m.getText());
    int day = Integer.parseInt(d.getText());
    return LocalDate.of(year, month, day);
  }

  //helper to clear text fields after user hits the appropriate button
  protected void clear(JTextField... fields) {
    for (JTextField field : fields) {
      field.setText("");
    }
  }

  //helper for showing error messages on view instead of in terminal/at runtime
  protected void sendError(Exception e) {
    try {
      view.showCustomMessage("Error: " + e.getMessage());
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  //nested class as it only makes sense to use it here as it would be common in multiple panels
  //ghost text to show user what is expected input
  protected static class GhostText extends JTextField implements FocusListener {

    private final String hint;
    private boolean showingHint;

    protected GhostText(final String hint, int size) {
      super(hint, size);
      super.setForeground(Color.LIGHT_GRAY);
      this.hint = hint;
      this.showingHint = true;
      super.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
      if (this.getText().isEmpty()) {
        super.setText("");
        showingHint = false;
      }
    }

    @Override
    public void focusLost(FocusEvent e) {
      if (this.getText().isEmpty()) {
        super.setText(hint);
        super.setForeground(Color.LIGHT_GRAY);
        showingHint = true;
      }
    }

    @Override
    public String getText() {
      if (showingHint) {
        return "";
      }
      return super.getText();
    }
  }

}
