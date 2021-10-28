package usecases;

import entities.Event;

import java.util.List;
import java.util.Map;

public class MonthlyCalendar extends GetCalendar {

    @Override
    public Map<Integer, List<Event>> getCalendar(CalendarManager cm) {
        return cm.getCurrentCalendar().getCalendarMap();
    }

    public Map<Integer, List<Event>> getCalendar(CalendarManager cm, int year, int month){
        if (year > cm.getCurrentYear()){
            month = month + 12;
        }
        else if (year < cm.getCurrentYear()){
            month = month - 12;
        }
        if (month == cm.getCurrentMonth()){
            return cm.getCurrentCalendar().getCalendarMap();
        }
        else if (month > cm.getCurrentMonth() && month - cm.getCurrentMonth() <= 3){
            return cm.getFutureCalendar().get(month - cm.getCurrentMonth() - 1).getCalendarMap();
        }
        else {
            return cm.getPastCalendar().get((cm.getCurrentMonth() - month) - 1).getCalendarMap();
        }
    }
}
