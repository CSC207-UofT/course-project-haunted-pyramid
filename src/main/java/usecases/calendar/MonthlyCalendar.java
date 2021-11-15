package usecases.calendar;

import entities.Event;
import entities.OurCalendar;
import usecases.calendar.CalendarManager;
import usecases.calendar.GetCalendar;

import java.util.List;
import java.util.Map;

/**
 * Get Monthly Calendar Map
 * @author Seo Won Yi
 * @see GetCalendar
 * @see CalendarManager
 * @see OurCalendar
 */

public class MonthlyCalendar extends GetCalendar {

    /**
     * return the map of the monthly calendar for the current year and month
     * @param cm calendarManager object to consider from
     * @return a map of the monthly calendar
     */
    @Override
    public Map<Integer, List<Event>> getCalendar(CalendarManager cm) {
        return cm.getCurrentCalendar().getCalendarMap();
    }

    /**
     * Return the map of a monthly calendar with the given year and date
     * @param cm CalendarManager object that will be used to extract the map
     * @param year chosen year
     * @param month chosen month
     * @return a map of a monthly calendar
     */
    public Map<Integer, List<Event>> getCalendar(CalendarManager cm, int year, int month){
        int adjustedMonth = adjustMonth(cm.getCurrentYear(), year, month);
        if (adjustedMonth == cm.getCurrentMonth()){
            return cm.getCurrentCalendar().getCalendarMap();
        }
        else if (adjustedMonth > cm.getCurrentMonth() && adjustedMonth - cm.getCurrentMonth() <= 3){
            return cm.getFutureCalendar().get(adjustedMonth - cm.getCurrentMonth() - 1).getCalendarMap();
        }
        else {
            return cm.getPastCalendar().get((cm.getCurrentMonth() - adjustedMonth) - 1).getCalendarMap();
        }
    }
}
