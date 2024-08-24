package controller;

import java.io.IOException;

import model.IModel;
import model.IModelV2;
import view.IView;

/**
 * This interface represents the possible commands the controller can execute
 * Reinforces the command design pattern.
 */
public interface StocksCommandsV2 extends StocksCommands {

  @Override
  default void execute(IModel model, IView v) {
    try {
      execute((IModelV2) model, v);
    } catch (IOException e) {
      throw new UnsupportedOperationException(
              "Please Use PortfolioV2 objects for this controller.");
    }
  }

  /**
   * Every command that implements this interface can execute their respective action.
   *
   * @param model the model of V2 to be passed in and commands to be executed on
   * @param v     the view to be passed in so each command can transmit
   *              specific messages to display
   */
  void execute(IModelV2 model, IView v) throws IOException;
}
