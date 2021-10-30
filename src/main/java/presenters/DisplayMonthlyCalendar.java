package presenters;

import entities.Event;
import helpers.CalendarFrame;
import usecases.CalendarManager;
import usecases.MonthlyCalendar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class DisplayMonthlyCalendar extends DisplayCalendar {
    private final int year;
    private final int month;
    private final List<Integer> keyList;
    private final Map<Integer, List<Event>> calendarMap;
    List<String> dayOfWeekCollection = new ArrayList<>(){{
        add("SUNDAY");
        add("MONDAY");
        add("TUESDAY");
        add("WEDNESDAY");
        add("THURSDAY");
        add("FRIDAY");
        add("SATURDAY");
    }};
    public DisplayMonthlyCalendar(CalendarManager cm, int year, int month) {
        super(cm);
        this.year = year;
        this.month = month;
        MonthlyCalendar mc = new MonthlyCalendar();
        this.keyList = new ArrayList<>(mc.getCalendar(cm, year, month).keySet());
        calendarMap =  mc.getCalendar(cm, year, month);
        Collections.sort(this.keyList);
    }

    @Override
    public String displayCalendar(){
        CalendarFrame cf = new CalendarFrame(this.year, this.month);
        StringBuilder result = new StringBuilder();
        List<Integer> usedDates = new ArrayList<>();
        List<Integer> usedContentDates = new ArrayList<>();
        result.append(cf.startFrame("SUNDAY", 0));
        addDateRowToCalendar(result, usedDates, usedContentDates);
        addContentsToCalendar(result, usedContentDates);
        usedContentDates = new ArrayList<>();
        result.append(cf.endFrame(0));
        int iteratorCounter = keyList.subList(usedDates.size(), keyList.size()).size();
        fillCalendar(cf, result, usedDates, usedContentDates, iteratorCounter);
        List<String> conflicts = cm.notifyConflict(this.year, this.month);
        if (conflicts.size() == 0){
            result.append("There is no conflict for this month");
        }
        else {
            result.append("The following items are having conflict: ");
            for (String name : conflicts){
                result.append(name);
            }
        }
        return result.toString();
    }

    private void fillCalendar(CalendarFrame cf, StringBuilder result, List<Integer> usedDates,
                              List<Integer> usedContentDates, int iteratorCounter) {
        if (iteratorCounter > 28){
            for (int i = 0; i < 5; i++){
                addDateRowToCalendar(result, usedDates, usedContentDates);
                addContentsToCalendar(result, usedContentDates);
                usedContentDates = new ArrayList<>();
                result.append(cf.endFrame(0));
            }
        }
        else if (21 < iteratorCounter){
            for (int j = 0; j < 4; j++){
                addDateRowToCalendar(result, usedDates, usedContentDates);
                addContentsToCalendar(result, usedContentDates);
                usedContentDates = new ArrayList<>();
                result.append(cf.endFrame(0));
            }
        }
        else {
            for (int k = 0; k < 3; k++){
                addDateRowToCalendar(result, usedDates, usedContentDates);
                addContentsToCalendar(result, usedContentDates);
                usedContentDates = new ArrayList<>();
                result.append(cf.endFrame(0));
            }
        }
    }

    private void addDateRowToCalendar(StringBuilder result, List<Integer> usedDates, List<Integer> usedContentDates){
        result.append("|");
        String startingDayOfWeek = startDayOfWeek(keyList, usedDates.size());
        int count = 0;
        for (String day : dayOfWeekCollection){
            if (!day.equals(startingDayOfWeek)){
                count += 1;
                String tempDiv = " ".repeat(day.length() + 24);
                result.append(tempDiv).append("|");
            }
            else {
                String tempDiv;
                if (keyList.get(usedDates.size()) < 10){
                    tempDiv = " ".repeat(day.length() + 22);
                }
                else {
                    tempDiv = " ".repeat(day.length() + 21);
                }
                result.append(" ").append(keyList.get(usedDates.size())).append(tempDiv).append("|");
                usedContentDates.add(keyList.get(usedDates.size()));
                usedDates.add(keyList.get(usedDates.size()));
                break;
            }
        }
        addRestDateToCalendar(result, usedDates, usedContentDates, count);
        result.append("\n");
    }

    private void addRestDateToCalendar(StringBuilder result,
                                       List<Integer> usedDates, List<Integer> usedContentDates, int count){
        int i = 0;
        while(i < this.dayOfWeekCollection.size() - 1 - count && usedDates.size() < keyList.size()) {
            result.append(" ").append(keyList.get(usedDates.size()));
            if (keyList.get(usedDates.size()) < 10) {
                result.append(" ".repeat(this.dayOfWeekCollection.get(count + 1 + i).length() + 22)).append("|");
            } else {
                result.append(" ".repeat(this.dayOfWeekCollection.get(count + 1 + i).length() + 21)).append("|");
            }
            usedContentDates.add(keyList.get(usedDates.size()));
            usedDates.add(keyList.get(usedDates.size()));
            i += 1;

        }

        if (usedDates.size() >= keyList.size()) {
            while (i < this.dayOfWeekCollection.size() - 1 - count) {
                String tempDiv = " ".repeat(this.dayOfWeekCollection.get(count + 1 + i).length() + 24);
                result.append(tempDiv).append("|");
                i += 1;
            }
        }
    }



    private String startDayOfWeek(List<Integer> keyList, int index) {
        String startingDayOfWeek = LocalDate.parse(keyList.get(index) + "/" + month
                        + "/" + year, DateTimeFormatter.ofPattern("d/M/uuuu"))
                .getDayOfWeek().getDisplayName(TextStyle.SHORT_STANDALONE, Locale.CANADA);

        switch (startingDayOfWeek) {
            case "Sun.":
                startingDayOfWeek = "SUNDAY";
                break;
            case "Mon.":
                startingDayOfWeek = "MONDAY";
                break;
            case "Tue.":
                startingDayOfWeek = "TUESDAY";
                break;
            case "Wed.":
                startingDayOfWeek = "WEDNESDAY";
                break;
            case "Thu.":
                startingDayOfWeek = "THURSDAY";
                break;
            case "Fri.":
                startingDayOfWeek = "FRIDAY";
                break;
            case "Sat.":
                startingDayOfWeek = "SATURDAY";
                break;
        }
        return startingDayOfWeek;
    }

    private void addContentsToCalendar(StringBuilder result, List<Integer> usedContentDates){
        int longestSizeEvent = 0;
        for (int keys : keyList){
            if (longestSizeEvent < calendarMap.get(keys).size()){
                longestSizeEvent = calendarMap.get(keys).size();
            }
        }
        String startingDayOfWeek = startDayOfWeek(usedContentDates, 0);
        int index = this.dayOfWeekCollection.indexOf(startingDayOfWeek);
        for (int j = 0; j < longestSizeEvent; j++) {
            result.append("|");
            int count = 0;
            int i = 0;
            while (i < index) {
                String tempDiv = " ".repeat(this.dayOfWeekCollection.get(i).length() + 24);
                result.append(tempDiv).append("|");
                i += 1;
            }
            while (count < usedContentDates.size()) {
                if (calendarMap.get(usedContentDates.get(count)).size() - 1 >= j &&
                        calendarMap.get(usedContentDates.get(count)).size() != 0) {
                    String eventName = cm.getEventNames(year, month, usedContentDates.get(count)).get(j);
                    if (eventName.length() > 12){
                        eventName = eventName.substring(0, 12) + "...";
                    }
                    StringBuilder eventTime = cm.getEventTimes(year, month, usedContentDates.get(count)).get(j);
                    String tempDiv = " ".repeat(this.dayOfWeekCollection.get(i + count).length() + 24 -
                            eventName.length() - 3 - eventTime.length());
                    result.append(" ").append(eventName).append(": ").append(eventTime).append(tempDiv).append("|");
                }
                else if (calendarMap.get(usedContentDates.get(count)).size() - 1< j){
                    String tempDiv = " ".repeat(this.dayOfWeekCollection.get(count).length() + 24);
                    result.append(tempDiv).append("|");
                }
                count += 1;
            }
            if (usedContentDates.size() < 7 && usedContentDates.get(0) != 1){
                while (i + count < this.dayOfWeekCollection.size()){
                    String tempDiv = " ".repeat(this.dayOfWeekCollection.get(i + count).length() + 24);
                    result.append(tempDiv).append("|");
                    count += 1;
                }
            }

            result.append("\n");
        }
    }
}
