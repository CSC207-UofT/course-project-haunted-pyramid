package usecases;

import entities.Event;
import entities.OurCalendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeeklyCalendar extends GetCalendar {

    @Override
    public Map<Integer, List<Event>> getCalendar(CalendarManager cm) {
        Map<Integer, List<Event>> result = new HashMap<>();
        Map<Integer, List<Event>> currentCal = cm.getCurrentCalendar().getCalendarMap();
        int numTotalDays = cm.getCurrentCalendar().getDateInfo().get(2);
        weeklyCalGenerator(cm.getCurrentDate(), result, currentCal, numTotalDays, 0, cm.getFutureCalendar());
        return result;
    }

    public Map<Integer, List<Event>> getCalendar(CalendarManager cm, int year, int month, int date){
        if (year > cm.getCurrentYear()){
            month = month + 12;
        }
        else if (year < cm.getCurrentYear()){
            month = month - 12;
        }
        int currentMonth = cm.getCurrentMonth();
        Map<Integer, List<Event>> result = new HashMap<>();
        if (currentMonth == month){
            Map<Integer, List<Event>> currentCal = cm.getCurrentCalendar().getCalendarMap();
            int numTotalDays = cm.getCurrentCalendar().getDateInfo().get(2);
            weeklyCalGenerator(date, result, currentCal, numTotalDays, 0, cm.getFutureCalendar());
        }
        else if (currentMonth < month && month - currentMonth <= 3){
            Map<Integer, List<Event>> futureCal = cm.getFutureCalendar().get(month - currentMonth - 1).getCalendarMap();
            int numTotalDays = cm.getFutureCalendar().get(month - currentMonth - 1).getDateInfo().get(2);
            if (month - currentMonth == 3 && date > numTotalDays - 6){
                for (int i = date; i <= numTotalDays; i++){
                    result.put(i, futureCal.get(i));
                }
            }
            else {
                weeklyCalGenerator(date, result, futureCal, numTotalDays, month - currentMonth,
                        cm.getFutureCalendar());
            }
        }
        else if (currentMonth > month && currentMonth - month <= 3){
            Map<Integer, List<Event>> pastCal = cm.getPastCalendar().get(currentMonth - month - 1).getCalendarMap();
            int numTotalDays = cm.getPastCalendar().get(currentMonth - month - 1).getDateInfo().get(2);
            if (currentMonth - month == 3 && date > numTotalDays - 6){
                int shortage = 7 - numTotalDays - date + 1;
                for (int j = date; j <= numTotalDays; j++){
                    result.put(j, pastCal.get(j));
                }
                for (int k = 1; k <= shortage; k++){
                    result.put(k, cm.getCurrentCalendar().getCalendarMap().get(k));
                }
            }
            else {
                weeklyCalGenerator(date, result, pastCal, numTotalDays, currentMonth - month - 2,
                        cm.getPastCalendar());
            }
        }
        return result;
    }

    private void weeklyCalGenerator(int date, Map<Integer, List<Event>> result, Map<Integer,
            List<Event>> cal, int numTotalDays, int index, List<OurCalendar> futurePast) {

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
    }
}
