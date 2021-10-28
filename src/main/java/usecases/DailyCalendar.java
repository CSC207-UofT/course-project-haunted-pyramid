package usecases;

import entities.Event;
import entities.OurCalendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyCalendar extends GetCalendar {
    @Override
    public Map<Integer, List<Event>> getCalendar(CalendarManager cm) {
        Map<Integer, List<Event>> result = new HashMap<>();
        result.put(cm.getCurrentDate(), cm.getCurrentCalendar().getCalendarMap().get(cm.getCurrentDate()));
        return result;
    }

    public Map<Integer, List<Event>> getCalendar(CalendarManager cm, int year, int month, int date) {
        if (year > cm.getCurrentYear()){
            month = month + 12;
        }
        else if (year < cm.getCurrentYear()){
            month = month - 12;
        }
        Map<Integer, List<Event>> result = new HashMap<>();
        if (month == cm.getCurrentMonth()){
            result.put(date, cm.getCurrentCalendar().getCalendarMap().get(date));
        }
        else if (month > cm.getCurrentMonth() && month - cm.getCurrentMonth() <= 3){
            OurCalendar futureCal = cm.getFutureCalendar().get(month - cm.getCurrentMonth() -1);
            result.put(date, futureCal.getCalendarMap().get(date));
        }
        else if (month < cm.getCurrentMonth() && cm.getCurrentMonth() - month <= 3){
            OurCalendar pastCal = cm.getPastCalendar().get(cm.getCurrentMonth() - month - 1);
            result.put(date, pastCal.getCalendarMap().get(date));
        }
        return result;
    }
}
