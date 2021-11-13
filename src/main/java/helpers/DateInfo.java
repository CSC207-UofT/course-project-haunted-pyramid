package helpers;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * return the year, month, number of days associated with given date. The date information is based on the month
 * that is being added by the amount of index.
 * For example, index 1 will return the next month's (current month + 1) year, month and number of days in the month
 * information
 */
public class DateInfo {
    public List<Integer> getDateInfo(int index) {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        return getYearMonthDays(cal, index);
    }

    /**
     * add the interval to the month.
     * Return the list that contains elements of year, month and number of days of that added month
     * @param cal Calendar object to help perform the addition of month
     * @param interval Interval (month) to be added on the calendar
     * @return the list with the elements of year, month and number of days in the month
     */
    private List<Integer> getYearMonthDays(Calendar cal, int interval) {
        List<Integer> yearMonthDays = new ArrayList<>();
        cal.add(Calendar.MONTH, interval);
        yearMonthDays.add(cal.get(Calendar.YEAR));
        yearMonthDays.add(cal.get(Calendar.MONTH) + 1);
        YearMonth tempYearMonth = YearMonth.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
        yearMonthDays.add(tempYearMonth.lengthOfMonth());
        return yearMonthDays;
    }
}
