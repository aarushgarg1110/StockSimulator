package model;

/**
 * This interface specifies the required operations for all forms of portfolios
 * A portfolio holds investment objects.
 */
public interface Portfolio extends ValueProviders {
  //CHANGES TO EXISTING DESIGN:
  //add sellInvestment method, the previous implementation only focused on buying which is why
  //this was overlooked, but it only makes sense for any sort of portfolio to be able to
  //remove objects if one can add objects to it.
  //SimplePortfolio now has a simple sellInvestment method that is the opposite of addInvestment.

  //now extends ValueProviders to unify action or command of retrieving a value

  //changed addInvestment to take in a double quantity, not int quantity for 2 reasons:
  //1. The controller already uses Integer.parseInt to ensure that the user types in an int and
  //not a double. That invalid input is already accounted for at time of input, so no need to
  //double enforce that constraint.
  //2. By removing this double constraint, the programmer now has more flexibility when it comes to
  //any new or existing functionality that involves adding shares. The programmer can add partial
  //shares as needed for any computations as all that matters is that the user cannot buy
  //partial shares.

  /**
   * addInvestment method takes in an investment and quantity desired.
   *
   * @param investment is the stock the user wishes to buy
   * @param quantity refers to how many shares the user would like to buy
   */
  void addInvestment(Investment investment, double quantity);

  /**
   * sellInvestment method takes in an investment and quantity desired.
   *
   * @param ticker is the name of the stock the user wishes to sell
   * @param quantity refers to how many shares the user would like to sell
   */
  void sellInvestment(String ticker, double quantity);
}
