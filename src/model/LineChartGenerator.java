package model;

import java.time.LocalDate;
import java.time.YearMonth;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BiFunction;

//class that holds the logic for generating a bar chart representing performance of a
//portfolio or stock over time
class LineChartGenerator {
  TreeMap<LocalDate, Double> dateAndValues = new TreeMap<>();
  int eachAsteriskWorth;
  int baseAmount;
  LocalDate startDate;
  LocalDate endDate;
  TimeScale scale;

  //protected constructor as only the model needs to know about it
  LineChartGenerator(LocalDate startDate, LocalDate endDate, ValueProviders valHolder) {
    this.startDate = Objects.requireNonNull(startDate);
    this.endDate = Objects.requireNonNull(endDate);
    this.scale = scaleDecision();
    loadProvider(valHolder);
    setAsterisks();
  }

  // adjust the correct base amount and the scale of each asterisk
  private void setAsterisks() {
    int maxVal = rounding(Collections.max(dateAndValues.values()));
    int minVal = rounding(Collections.min(dateAndValues.values()));
    int range = maxVal - minVal;
    eachAsteriskWorth = 10;
    baseAmount = 0;
    if (maxVal > 500) {
      baseAmount = minVal;
      if (range / 50 > 100) {
        eachAsteriskWorth = ((int) Math.ceil((range / 50.0) / 100.0)) * 100;
      } else if (range / 50 > 10) {
        eachAsteriskWorth = ((int) Math.ceil((range / 50.0) / 10.0)) * 10;
      }
    }
  }

  //returns the asterisks to be displayed
  private String renderAsterisks(double value) {
    StringBuilder asterisks = new StringBuilder();
    int numberOfAsterisks;
    numberOfAsterisks = (int) Math.round((value - baseAmount)
            / eachAsteriskWorth);
    asterisks.append("*".repeat(Math.max(0, numberOfAsterisks)));
    asterisks.append("\n");
    return asterisks.toString();
  }

  // adjust the base and each asterisks value. if they are lower than 100, they will round to 10,
  // greater than 100 will just round to 100
  private int rounding(double valueToRound) {
    if (valueToRound > 100) {
      return ((int) valueToRound / 100) * 100;
    } else {
      return ((int) valueToRound / 10) * 10;
    }
  }

  // make a list that have maximum 30 lines. given the frequency
  private void loadProvider(ValueProviders valHolder) {
    int frequency = scale.factor;
    switch (scale.unit) {
      case DAYS:
        populateMap(startDate, endDate,
                frequency, (date, vh) -> vh.getEndOfDayVal(date), valHolder);
        break;
      case MONTHS:
        populateMap(startDate, endDate,
                frequency, (date, vh) ->
                        getEndOfMonthValue(date.getMonthValue(), date.getYear(), vh), valHolder);
        break;
      case YEARS:
        populateMap(startDate, endDate,
                frequency, (date, vh) -> getEndOfYearValue(date.getYear(), vh), valHolder);
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + scale.unit);
    }
  }

  // take a start date, end date, frequency, portfolio or investment,
  // and a bi-function which turns a date and a port/inv into the
  // value of that object in that given date.
  private void populateMap(LocalDate start, LocalDate end, int frequency,
                           BiFunction<LocalDate, ValueProviders, Double> valueFunction,
                           ValueProviders valHolder) {
    LocalDate date = start;
    while (date.isBefore(end) || date.isEqual(end)) {
      dateAndValues.put(date, valueFunction.apply(date, valHolder));
      date = date.plus(frequency, scale.unit);
    }
    removeLastDup(dateAndValues.lastKey(), end);
    dateAndValues.put(end, valueFunction.apply(end, valHolder));
  }

  // removeLastDup will check if the end date given has the same month/year
  // as the last item in the datesAndValues, then remove the last item if duplicate
  private void removeLastDup(LocalDate lastDateInList, LocalDate end) {
    if (scale.unit == ChronoUnit.MONTHS) {
      if (lastDateInList.getMonth() == end.getMonth()) {
        dateAndValues.pollLastEntry();
      }
    } else if (scale.unit == ChronoUnit.YEARS) {
      if (lastDateInList.getYear() == end.getYear()) {
        dateAndValues.pollLastEntry();
      }
    }
  }

  // get the closing price on the end of month day
  private double getEndOfMonthValue(int month, int year, ValueProviders valHolder) {
    LocalDate endOfMonth = YearMonth.of(year, month).atEndOfMonth();
    return valHolder.getEndOfDayVal(endOfMonth);
  }

  // get the closing price on the end of year day
  private double getEndOfYearValue(int year, ValueProviders valHolder) {
    LocalDate endOfYear = LocalDate.of(year, 12, 31);
    return valHolder.getEndOfDayVal(endOfYear);
  }

  // keep it. This method return Time gap between each line of chartTable
  private TimeScale scaleDecision() {
    int years = (int) ChronoUnit.YEARS.between(startDate, endDate);
    int months = (int) ChronoUnit.MONTHS.between(startDate, endDate);
    int days = (int) ChronoUnit.DAYS.between(startDate, endDate);
    int scale;
    ChronoUnit unit;
    if (years <= 4) {
      if (months <= 4) {
        unit = ChronoUnit.DAYS;
        scale = scaling(days);
      } else {
        unit = ChronoUnit.MONTHS;
        scale = scaling(months);
      }
    } else {
      unit = ChronoUnit.YEARS;
      scale = scaling(years);
    }
    return new TimeScale(unit, scale);
  }

  // calculate the scale of the unit if we want to
  // keep the rows less than 30
  private int scaling(int period) {
    return (period - 1) / 29 + 1;
  }

  // generate the whole chart table
  protected String generate() {
    StringBuilder builder = new StringBuilder();
    for (Map.Entry<LocalDate, Double> entry : dateAndValues.entrySet()) {
      switch (scale.unit) {
        case DAYS:
          builder.append(entry.getKey());
          builder.append(":");
          builder.append(renderAsterisks(entry.getValue()));
          break;

        case MONTHS:
          builder.append(entry.getKey().getYear());
          builder.append(" ");
          builder.append(entry.getKey().getMonth().toString(), 0, 3);
          builder.append(":");
          builder.append(renderAsterisks(entry.getValue()));
          break;

        case YEARS:
          builder.append(entry.getKey().getYear());
          builder.append(":");
          builder.append(renderAsterisks(entry.getValue()));
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + scale.unit);
      }
    }
    builder.append(String.format(
            "Scale: * = $%s more than a base amount of $%s\n", eachAsteriskWorth, baseAmount));
    return builder.toString();
  }

  private static class TimeScale {
    ChronoUnit unit;
    int factor;

    TimeScale(ChronoUnit unit, int factor) {
      this.unit = Objects.requireNonNull(unit);
      this.factor = factor;
    }
  }
}
