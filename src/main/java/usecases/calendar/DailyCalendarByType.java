package usecases.calendar;

import entities.OurCalendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Get Daily Calendar Map
 * @author Seo Won Yi
 * @see CalendarByType
 * @see CalendarManager
 * @see OurCalendar
 */

public class DailyCalendarByType extends CalendarByType {

    /**
     * get daily calendar map for the current date
     * @param cm calendarManager object to consider from
     * @return map of the daily calendar
     */
    @Override
    public Map<Integer, List<UUID>> getCalendar(CalendarManager cm) {
        Map<Integer, List<UUID>> result = new HashMap<>();
        Map<Integer, List<UUID>> calendarMap = cm.getCurrentCalendar().getCalendarMap();
        result.put(cm.getCurrentDate(), calendarMap.get(cm.getCurrentDate()));
        return result;
    }

    /**
     * get daily calendar map for the selected year, month, date.
     * @param cm calendarManager object to consider from
     * @param year chosen year
     * @param month chosen month
     * @param date chosen date
     * @return map of the daily calendar
     */
    public Map<Integer, List<UUID>> getCalendar(CalendarManager cm, int year, int month, int date) {
        int adjustedMonth =  adjustMonth(cm.getCurrentYear(), year, month);
        Map<Integer, List<UUID>> result = new HashMap<>();
        if (adjustedMonth == cm.getCurrentMonth()){
            OurCalendar currentCal = cm.getCurrentCalendar();
            result.put(date, currentCal.getCalendarMap().get(date));
        }
        else if (adjustedMonth > cm.getCurrentMonth() && adjustedMonth - cm.getCurrentMonth() <= 3){
            OurCalendar futureCal = cm.getFutureCalendar().get(adjustedMonth - cm.getCurrentMonth() -1);
            result.put(date, futureCal.getCalendarMap().get(date));
        }
        else if (adjustedMonth < cm.getCurrentMonth() && cm.getCurrentMonth() - adjustedMonth <= 3){
            OurCalendar pastCal = cm.getPastCalendar().get(cm.getCurrentMonth() - adjustedMonth - 1);
            result.put(date, pastCal.getCalendarMap().get(date));
        }
        return result;
    }
}
