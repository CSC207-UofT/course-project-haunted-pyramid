package usecases.calendar;

import entities.Event;

import java.util.List;
import java.util.Map;

/**
 * Abstract class for getting different types of calendar
 * @author Seo Won Yi
 * @see DailyCalendar
 * @see WeeklyCalendar
 * @see MonthlyCalendar
 */
public abstract class GetCalendar {
    /**
     * return the map of the calendar (key: date, value: list of event)
     * @param cm calendarManager object to consider from
     * @return map of the calendar (key: date, value: list of event)
     */
    protected abstract Map<Integer, List<Event>> getCalendar(CalendarManager cm);

    /**
     * adjust the month according to the year
     * if the year is higher, add the number of the months to match
     * otherwise, subtract
     * @param currentYear current year
     * @param year given year input
     * @param month current month
     * @return adjusted month
     */
    protected int adjustMonth(int currentYear, int year, int month){
        if (year > currentYear){
            month = month + (12 * (year - currentYear));
        }
        else if (year < currentYear){
            month = month - (12 * (currentYear - year));
        }
        return month;
    }
}
