package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;

import model.IModelV2;
import view.IView;

//abstract class to represent shared operations by each command
//uses adapter pattern to reuse same exact methods in abstract commands, but adapted
//to places where StocksCommandV2 is needed
abstract class AbstractCommandsV2 extends AbstractCommands implements StocksCommandsV2 {

  //abstract constructor for each command
  protected AbstractCommandsV2(Scanner scanner) {
    super(scanner);
  }

  //default constructor to take no arguments
  //this constructor is to be used for controllers that do not require a scanner or prompt
  protected AbstractCommandsV2() {
    super();
  }

  //helper to abstract duplicate logic needed in two places
  protected void displayDistribution(IModelV2 model, IView v, LocalDate date, String pName)
          throws IOException {
    Map<String, Double> dist = model.getDistributionValue(date, pName);
    StringBuilder message = new StringBuilder();
    message.append("\n").append(pName)
            .append("'s distribution of values on ")
            .append(date).append(":\n");
    for (Map.Entry<String, Double> entry : dist.entrySet()) {
      message.append(entry.getKey()).append(": $").append(entry.getValue()).append("\n");
    }
    v.showCustomMessage(message.toString());
  }

}