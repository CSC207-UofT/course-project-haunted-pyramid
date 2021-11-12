package usecases.calendar;

import entities.Event;
import entities.OurCalendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyCalendar extends GetCalendar {

    /**
     * get daily calendar map for the current date
     * @param cm calendarManager object to consider from
     * @return map of the daily calendar
     */
    @Override
    public Map<Integer, List<Event>> getCalendar(CalendarManager cm) {
        Map<Integer, List<Event>> result = new HashMap<>();
        result.put(cm.getCurrentDate(), cm.getCurrentCalendar().getCalendarMap().get(cm.getCurrentDate()));
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
    public Map<Integer, List<Event>> getCalendar(CalendarManager cm, int year, int month, int date) {
        int adjustedMonth =  adjustMonth(cm.getCurrentYear(), year, month);
        Map<Integer, List<Event>> result = new HashMap<>();
        if (adjustedMonth == cm.getCurrentMonth()){
            result.put(date, cm.getCurrentCalendar().getCalendarMap().get(date));
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
