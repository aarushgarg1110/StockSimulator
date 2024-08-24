package model;

import java.time.LocalDate;

/**
 * This interface specifies the operation of retrieving a value of an object that can be valued.
 * In this program so far, many operations center around obtaining the value of a portfolio or
 * investment at the end of the day. Instead of treating the retrieval of those values as two
 * separate actions, we can unify them under this one interface and one action.
 */
public interface ValueProviders {

  /**
   * gets the value of something that can be valued at end of specified day.
   * If weekend or holiday, cycles back 1 day at a time until a closing day value can be found.
   *
   * @param targetDate the date on which value is to be fetched
   * @return the value of the object at the end of the day
   * @throws IllegalArgumentException if no price available for any day currently or prior
   */
  double getEndOfDayVal(LocalDate targetDate) throws IllegalArgumentException;
}
