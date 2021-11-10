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
