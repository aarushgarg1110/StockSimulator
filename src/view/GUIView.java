package view;

import controller.Features;


/**
 * This interface specifies the sole operations of a GUI view.
 */
public interface GUIView extends IView {

  /**
   * adds a controller that can answer feature callbacks.
   *
   * @param features the Feature to be added.
   */
  void addFeatures(Features features);
}
