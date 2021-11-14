package helpers;

import entities.Event;
import usecases.events.EventManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Seo Won Yi
 */
public class DisplayCalendarHelper {
    private final int year;
    private final int month;
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

    public DisplayCalendarHelper(int year, int month){
        this.year = year;
        this.month = month;
    }

    /**
     * Make the starting frame of the calendar image for monthly and weekly calendar
     * Contain Day of Week information
     * @param startDayOfWeek the starting day of week for the calendar frame
     * @param additionalSpacer horizontal space flexibility (0 makes the default size of the frame)
     * @return the string of the calendar frame with the day of week information
     */
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

    /**
     * Helper method for starFrame. Determines which day of week to show first on the startFrame
     * @param result StringBuilder object that the output of the method will be appended on
     * @param index the index representing each day of the week (Sunday = 0, Monday = 1, etc...)
     * @param space external space to have between each of the day of the week strings
     */
    private void frameWithDifferentStartDay(StringBuilder result, int index, String space){
        for (String days : this.DATES.subList(index, 7)){
            result.append("|").append(space).append(days).append(space);
        }
        for (String days : this.DATES.subList(0, index)){
            result.append("|").append(space).append(days).append(space);
        }
    }

    /**
     * End frame of the monthly and weekly calendar images. Associated with dashes
     * @param additionalSpacer horizontal space flexibility that will be added on to make the frame larger if needed
     * @return the string of the dashed lines that will be used as end frame
     */
    public String endFrame(int additionalSpacer){
        StringBuilder result = new StringBuilder();
        String div = "-".repeat(Constants.CALENDAR_SIZE + additionalSpacer*7);
        result.append(" ").append(div).append("\n");
        return result.toString();
    }

    /**
     * Sort the events associated in the calendarMap
     * @param calendarMap calendarMap object that has events inside
     */
    public void eventSorter(Map<Integer, List<Event>> calendarMap){
        EventManager em = new EventManager();
        List<Integer> keyList = new ArrayList<>(calendarMap.keySet());
        for (Integer key:keyList){
            List<Event> eventList = em.timeOrder(calendarMap.get(key));
            calendarMap.put(key, eventList);
        }
    }

    /**
     * Add all the items from addLst to toBeUpdated. Avoid adding duplicates.
     * Sort the items (the elements of the list are all numerical strings) and return the sorted result
     * @param toBeUpdated the list that will act as a base
     * @param addLst the list that will be added to toBeUpdated
     * @return the sorted list of numerical strings that contain elements of toBeUpdated and addLst
     */
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
        addTimeString(container, sortedList);
        return sortedList;
    }

    /**
     * Convert the numbers into time string and add them to sortedList
     * For example, 0 -> 00:00, 13 -> 13:00 etc...
     * @param container list of integers that will be converted to time string
     * @param sortedList sorted time list string that will have time strings added from container
     */
    private void addTimeString(List<Integer> container, List<String> sortedList) {
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
    }

    /**
     * Return the string of day of week of the given year, month and date
     * @param year provided year
     * @param month provided month
     * @param date provided date
     * @return the string of day of week for the given year, month and date
     */
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

    /**
     * Return the day of week in terms of integer where, 1 = MONDAY, 7 = SUNDAY
     * @param year the given year
     * @param month the given month
     * @param date the given date
     * @return the day of week in terms of integer
     */
    public int findStartDayOfWeekInteger(int year, int month, int date){
        LocalDate localDate = LocalDate.of(year, month, date);
        DayOfWeek dayOfWeek = DayOfWeek.from(localDate);
        return dayOfWeek.getValue();
    }

    /**
     * Convert the time string into Integer.
     * For example, 20:00 = 2000, 03:00 = 300 etc...
     * @param time time string that will be converted
     * @return integer that represents the time string
     */
    public int convertTimeToInt(String time){
        String temp = time.substring(0, 2) + time.substring(3, 5);
        return Integer.parseInt(temp);
    }
}
