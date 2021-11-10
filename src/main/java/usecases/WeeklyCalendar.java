package usecases;

import entities.Event;
import entities.OurCalendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeeklyCalendar extends GetCalendar {

    @Override
    public Map<Integer, List<Event>> getCalendar(CalendarManager cm) {
        Map<Integer, List<Event>> result;
        Map<Integer, List<Event>> currentCal = cm.getCurrentCalendar().getCalendarMap();
        result = weeklyCalGenerator(cm.getCurrentDate(), currentCal, cm.getFutureCalendar(), 0);
        return result;
    }

    public Map<Integer, List<Event>> getCalendar(CalendarManager cm, int year, int month, int date){
        int adjustedMonth = adjustMonth(cm.getCurrentYear(), year, month);
        int currentMonth = cm.getCurrentMonth();
        Map<Integer, List<Event>> result = new HashMap<>();
        if (currentMonth == adjustedMonth){
            Map<Integer, List<Event>> currentCal = cm.getCurrentCalendar().getCalendarMap();
            result = weeklyCalGenerator(date, currentCal, cm.getFutureCalendar(), 0);
        }
        else if (currentMonth < adjustedMonth && adjustedMonth - currentMonth <= 3){
            result = getFutureCalMap(cm, date, adjustedMonth, currentMonth);
        }
        else if (currentMonth > adjustedMonth && currentMonth - adjustedMonth <= 3){
            result = getPastCalMap(cm, date, adjustedMonth, currentMonth);
        }
        return result;
    }

    private Map<Integer, List<Event>> getPastCalMap(CalendarManager cm, int date, int adjustedMonth, int currentMonth)
    {
        Map<Integer, List<Event>> pastCal = cm.getPastCalendar().get(currentMonth - adjustedMonth - 1).getCalendarMap();
        Map<Integer, List<Event>> result = new HashMap<>();
        int numTotalDays = pastCal.size();
        if (currentMonth - adjustedMonth == 1 && date > numTotalDays - 6){
            int shortage = 7 - (numTotalDays - date + 1);
            for (int j = date; j <= numTotalDays; j++){
                result.put(j, pastCal.get(j));
            }
            for (int k = 1; k <= shortage; k++){
                result.put(k, cm.getCurrentCalendar().getCalendarMap().get(k));
            }
        }
        else {
            return weeklyCalGenerator(date, pastCal, cm.getPastCalendar(), currentMonth - adjustedMonth - 2);
        }
        return result;
    }

    private Map<Integer, List<Event>> getFutureCalMap(CalendarManager cm, int date, int adjustedMonth, int currentMonth)
    {
        Map<Integer, List<Event>> result = new HashMap<>();
        Map<Integer, List<Event>> futureCal =
                cm.getFutureCalendar().get(adjustedMonth - currentMonth - 1).getCalendarMap();
        int numTotalDays = cm.getFutureCalendar().get(adjustedMonth - currentMonth - 1).getDateInfo().get(2);
        if (adjustedMonth - currentMonth == 3 && date > numTotalDays - 6){
            for (int i = date; i <= numTotalDays; i++){
                result.put(i, futureCal.get(i));
            }
        }
        else {
            return weeklyCalGenerator(date, futureCal, cm.getFutureCalendar(), adjustedMonth - currentMonth);
        }
        return result;
    }

    private Map<Integer, List<Event>> weeklyCalGenerator(int date, Map<Integer, List<Event>> cal,
                                                         List<OurCalendar> futurePast, int index) {
        Map<Integer, List<Event>> result = new HashMap<>();
        int numTotalDays = cal.size();
        if (date <= numTotalDays - 6){
            for (int i = date; i < date + 7; i++){
                result.put(i, cal.get(i));
            }
        }
        else {
            int shortage = 7 - (numTotalDays - date + 1);
            for (int j = date; j <= numTotalDays; j++){
                result.put(j, cal.get(j));
                for (int k = 1; k <= shortage; k++){
                    Map<Integer, List<Event>> nextMonthCal = futurePast.get(index).getCalendarMap();
                    result.put(k, nextMonthCal.get(k));
                }
            }
        }
        return result;
    }
}
