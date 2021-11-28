package usecases.calendar;

import entities.OurCalendar;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Get Monthly Calendar Map
 * @author Seo Won Yi
 * @see CalendarByType
 * @see CalendarManager
 * @see OurCalendar
 */

public class MonthlyCalendarByType extends CalendarByType {

    /**
     * return the map of the monthly calendar for the current year and month
     * @param cm calendarManager object to consider from
     * @return a map of the monthly calendar
     */
    @Override
    public Map<Integer, List<UUID>> getCalendar(CalendarManager cm) {
        return cm.getCurrentCalendar().getCalendarMap();
    }

    /**
     * Return the map of a monthly calendar with the given year and date
     * @param cm CalendarManager object that will be used to extract the map
     * @param year chosen year
     * @param month chosen month
     * @return a map of a monthly calendar
     */
    public Map<Integer, List<UUID>> getCalendar(CalendarManager cm, int year, int month){
        int adjustedMonth = adjustMonth(cm.getCurrentYear(), year, month);
        if (adjustedMonth == cm.getCurrentMonth()){
            return cm.getCurrentCalendar().getCalendarMap();
        }
        else if (adjustedMonth > cm.getCurrentMonth() && adjustedMonth - cm.getCurrentMonth() <= 3){
            OurCalendar futureCalendar = cm.getFutureCalendar().get(adjustedMonth - cm.getCurrentMonth() - 1);
            return futureCalendar.getCalendarMap();
        }
        else {
            OurCalendar pastCalendar = cm.getPastCalendar().get((cm.getCurrentMonth() - adjustedMonth) - 1);
            return pastCalendar.getCalendarMap();
        }
    }
}
