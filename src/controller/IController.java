package controller;

import java.io.IOException;

/**
 * This interface represents the operations that a controller must have for this application.
 */
public interface IController {

  /**
   * The main method that relinquishes control of the application to the controller.
   *
   * @throws IllegalStateException if the controller is unable to transmit output
   */
  void start() throws IllegalStateException, IOException;
}
