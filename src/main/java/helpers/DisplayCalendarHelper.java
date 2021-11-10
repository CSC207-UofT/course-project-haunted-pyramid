package helpers;

import entities.Event;
import usecases.EventManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DisplayCalendarHelper {

    private final ArrayList<String> DATES = new ArrayList<>() {
        {
            add("SUNDAY");
            add("MONDAY");
            add("TUESDAY");
            add("WEDNESDAY");
            add("THURSDAY");
            add("FRIDAY");
            add("SATURDAY");
        }
    };

    private final int year;
    private final int month;

    public DisplayCalendarHelper(int year, int month){
        this.year = year;
        this.month = month;
    }

    public String startFrame(String startDayOfWeek, int additionalSpacer){
        StringBuilder result = new StringBuilder();
        result.append("Calendar for ").append(this.year).append("/").append(this.month).append("\n");
        String div = "-".repeat(Constants.CALENDAR_SIZE + additionalSpacer * 7);
        result.append(" ").append(div).append("\n");
        String space = " ".repeat(Constants.CAL_ROW_SPACER /2 + additionalSpacer/2);
        switch (startDayOfWeek) {
            case "SUNDAY":
                frameWithDifferentStartDay(result, 0, space);
                break;
            case "MONDAY":
                frameWithDifferentStartDay(result, 1, space);
                break;
            case "TUESDAY":
                frameWithDifferentStartDay(result, 2, space);
                break;
            case "WEDNESDAY":
                frameWithDifferentStartDay(result, 3, space);
                break;
            case "THURSDAY":
                frameWithDifferentStartDay(result, 4, space);
                break;
            case "FRIDAY":
                frameWithDifferentStartDay(result, 5, space);
                break;
            case "SATURDAY":
                frameWithDifferentStartDay(result, 6, space);
                break;
        }
        result.append("|").append("\n");
        result.append(" ").append(div).append("\n");
        return result.toString();
    }

    private void frameWithDifferentStartDay(StringBuilder result, int index, String space){
        for (String days : this.DATES.subList(index, 7)){
            result.append("|").append(space).append(days).append(space);
        }
        for (String days : this.DATES.subList(0, index)){
            result.append("|").append(space).append(days).append(space);
        }
    }

    public String endFrame(int additionalSpacer){
        StringBuilder result = new StringBuilder();
        String div = "-".repeat(Constants.CALENDAR_SIZE + additionalSpacer*7);
        result.append(" ").append(div).append("\n");
        return result.toString();
    }

    public void eventSorter(Map<Integer, List<Event>> calendarMap){
        EventManager em = new EventManager();
        List<Integer> keyList = new ArrayList<>(calendarMap.keySet());
        for (Integer key:keyList){
            List<Event> eventList = em.timeOrder(calendarMap.get(key));
            calendarMap.put(key, eventList);
        }
    }

    public List<String> updateTimeList(List<String> toBeUpdated, List<String> addLst){
        List<String> temp = new ArrayList<>(toBeUpdated);
        for (String time : addLst){
            if (!temp.contains(time)){
                temp.add(time);
            }
        }
        List<Integer> container = new ArrayList<>();
        List<String> sortedList = new ArrayList<>();
        for (String item : temp){
            container.add(convertTimeToInt(item));
        }
        Collections.sort(container);
        for (Integer number : container) {
            String convertedTime = String.valueOf(number);
            if (convertedTime.length() == 1) {
                sortedList.add("00:0" + convertedTime);
            } else if (convertedTime.length() == 2) {
                sortedList.add("00:" + convertedTime);
            } else if (convertedTime.length() == 3) {
                sortedList.add("0" + convertedTime.charAt(0) + ":" + convertedTime.substring(1, 3));
            } else if (convertedTime.length() == 4) {
                sortedList.add(convertedTime.substring(0, 2) + ":" + convertedTime.substring(2, 4));
            }
        }
        return sortedList;
    }

    public Integer convertTimeToInt(String time){
        String temp = time.substring(0, 2) + time.substring(3, 5);
        return Integer.parseInt(temp);
    }

    public String findStartDayOfWeekString(int year, int month, int date){
        int startingDayOfWeek = findStartDayOfWeekInteger(year, month, date);
        String chosenDayOfWeek = "Monday";
        switch (startingDayOfWeek) {
            case 1:
                chosenDayOfWeek = "MONDAY";
                break;
            case 2:
                chosenDayOfWeek = "TUESDAY";
                break;
            case 3:
                chosenDayOfWeek = "WEDNESDAY";
                break;
            case 4:
                chosenDayOfWeek = "THURSDAY";
                break;
            case 5:
                chosenDayOfWeek = "FRIDAY";
                break;
            case 6:
                chosenDayOfWeek = "SATURDAY";
                break;
            case 7:
                chosenDayOfWeek = "SUNDAY";
                break;
        }
        return chosenDayOfWeek;
    }

    public int findStartDayOfWeekInteger(int year, int month, int date){
        LocalDate localDate = LocalDate.of(year, month, date);
        DayOfWeek dayOfWeek = DayOfWeek.from(localDate);
        return dayOfWeek.getValue();
    }
}
