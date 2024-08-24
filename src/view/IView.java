package view;

import java.io.IOException;

/**
 * This interfaces specifies the basic operations that any view class should have
 * for this program.
 */
public interface IView {

  /**
   * Allows view to display a custom message sent by controller.
   * @param message the custom output to be displayed
   */
  void showCustomMessage(String message) throws IOException;

  /**
   * Allows view to display a set menu of operations.
   */
  void showMenu() throws IOException;

  /**
   * Allows view to display a set welcome message.
   */
  void welcome() throws IOException;

  /**
   * Allows view to display a set goodbye message.
   */
  void goodbye() throws IOException;

}
