package controller;

import view.GUIView;

/**
 * This interface specifies the operations for controllers that support a graphical user
 * interface.
 */
public interface IGUIController {

  /**
   * Sets the view of the controller to the specified one and adds itself as a listener
   * to the features of the View.
   *
   * @param v the view that this controller should be set to
   */
  void setView(GUIView v);
}
