package presenters;

import entities.Event;
import helpers.Constants;
import helpers.DisplayCalendarHelper;
import usecases.calendar.CalendarManager;
import usecases.calendar.MonthlyCalendar;

import java.util.*;

public class DisplayMonthlyCalendar extends DisplayCalendar {
    private final int year;
    private final int month;
    private final List<Integer> keyList;
    private final Map<Integer, List<Event>> calendarMap;
    private final DisplayCalendarHelper cf;
    List<String> dayOfWeekCollection = new ArrayList<>() {{
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
        calendarMap = mc.getCalendar(cm, year, month);
        Collections.sort(this.keyList);
        this.cf = new DisplayCalendarHelper(year, month);
    }

    @Override
    public String displayCalendar() {
        cf.eventSorter(calendarMap);
        StringBuilder result = new StringBuilder();
        List<Integer> usedDates = new ArrayList<>();
        List<Integer> usedContentDates = new ArrayList<>();
        usedContentDates = initialSetup(result, usedDates, usedContentDates);
        int iteratorCounter = keyList.subList(usedDates.size(), keyList.size()).size();
        fillCalendar(cf, result, usedDates, usedContentDates, iteratorCounter);
        List<String> conflicts = cm.notifyConflict(this.year, this.month);
        showConflict(result, conflicts);
        return result.toString();
    }

    private void showConflict(StringBuilder result, List<String> conflicts) {
        if (conflicts.size() == 0){
            result.append("There is no conflict for this month");
        }
        else {
            result.append("The following Events are having conflict: ");
            for (String name : conflicts) {
                result.append(name).append("; ");
            }
        }
    }

    private List<Integer> initialSetup(StringBuilder result, List<Integer> usedDates, List<Integer> usedContentDates) {
        result.append(cf.startFrame("SUNDAY", 0));
        addDateRowToCalendar(result, usedDates, usedContentDates);
        addContentsToCalendar(result, usedContentDates);
        usedContentDates = new ArrayList<>();
        result.append(cf.endFrame(0));
        return usedContentDates;
    }

    private void fillCalendar(DisplayCalendarHelper cf, StringBuilder result, List<Integer> usedDates,
                              List<Integer> usedContentDates, int iteratorCounter) {
        if (iteratorCounter > 28) {
            for (int i = 0; i < 5; i++) {
                usedContentDates = fillCalendarHelper(cf, result, usedDates, usedContentDates);
            }
        } else if (21 < iteratorCounter) {
            for (int j = 0; j < 4; j++) {
                usedContentDates = fillCalendarHelper(cf, result, usedDates, usedContentDates);
            }
        } else {
            for (int k = 0; k < 3; k++) {
                usedContentDates = fillCalendarHelper(cf, result, usedDates, usedContentDates);
            }
        }
    }

    private List<Integer> fillCalendarHelper(DisplayCalendarHelper cf, StringBuilder result, List<Integer> usedDates,
                                             List<Integer> usedContentDates) {
        addDateRowToCalendar(result, usedDates, usedContentDates);
        addContentsToCalendar(result, usedContentDates);
        usedContentDates = new ArrayList<>();
        result.append(cf.endFrame(0));
        return usedContentDates;
    }

    private void addDateRowToCalendar(StringBuilder result, List<Integer> usedDates, List<Integer> usedContentDates) {
        result.append("|");
        String startingDayOfWeek = cf.findStartDayOfWeekString(this.year, this.month, this.keyList.get(usedDates.size()));
        int count = 0;
        for (String day : dayOfWeekCollection) {
            if (!day.equals(startingDayOfWeek)) {
                count += 1;
                String tempDiv = " ".repeat(day.length() + Constants.CAL_ROW_SPACER);
                result.append(tempDiv).append("|");
            } else {
                String tempDiv;
                if (keyList.get(usedDates.size()) < 10) {
                    tempDiv = " ".repeat(day.length() + Constants.CAL_ROW_SPACER - 2);
                } else {
                    tempDiv = " ".repeat(day.length() + Constants.CAL_ROW_SPACER - 3);
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
                                       List<Integer> usedDates, List<Integer> usedContentDates, int count) {
        int i = 0;
        while (i < this.dayOfWeekCollection.size() - 1 - count && usedDates.size() < keyList.size()) {
            result.append(" ").append(keyList.get(usedDates.size()));
            if (keyList.get(usedDates.size()) < 10) {
                result.append(" ".repeat(this.dayOfWeekCollection.get(count + 1 + i).length() +
                        Constants.CAL_ROW_SPACER - 2)).append("|");
            } else {
                result.append(" ".repeat(this.dayOfWeekCollection.get(count + 1 + i).length() +
                        Constants.CAL_ROW_SPACER - 3)).append("|");
            }
            usedContentDates.add(keyList.get(usedDates.size()));
            usedDates.add(keyList.get(usedDates.size()));
            i += 1;

        }

        if (usedDates.size() >= keyList.size()) {
            while (i < this.dayOfWeekCollection.size() - 1 - count) {
                String tempDiv = " ".repeat(this.dayOfWeekCollection.get(count + 1 + i).length() +
                        Constants.CAL_ROW_SPACER);
                result.append(tempDiv).append("|");
                i += 1;
            }
        }
    }


    private void addContentsToCalendar(StringBuilder result, List<Integer> usedContentDates) {
        int longestSizeEvent = 0;
        for (int keys : keyList) {
            if (longestSizeEvent < calendarMap.get(keys).size()) {
                longestSizeEvent = calendarMap.get(keys).size();
            }
        }
        String startingDayOfWeek = cf.findStartDayOfWeekString(this.year, this.month, usedContentDates.get(0));
        int dayOfWeekIndex = this.dayOfWeekCollection.indexOf(startingDayOfWeek);
        for (int eventIndex = 0; eventIndex < longestSizeEvent; eventIndex++) {
            result.append("|");
            int contentCount = 0;
            int startingIndex = 0;
            while (startingIndex < dayOfWeekIndex) {
                startingIndex = fillBeforeStartDate(result, startingIndex);
            }
            while (contentCount < usedContentDates.size()) {
                contentCount = addContent(result, usedContentDates, eventIndex, contentCount, startingIndex);
            }
            if (usedContentDates.size() < 7 && usedContentDates.get(0) != 1) {
                fillRestRow(result, contentCount, startingIndex);
            }

            result.append("\n");
        }
    }

    private int addContent(StringBuilder result, List<Integer> usedContentDates, int eventIndex, int contentCount,
                           int startingIndex) {
        if (calendarMap.get(usedContentDates.get(contentCount)).size() - 1 >= eventIndex &&
                calendarMap.get(usedContentDates.get(contentCount)).size() != 0) {
            String eventName = cm.getEventNames(year, month, usedContentDates.get(contentCount)).get(eventIndex);
            if (eventName.length() > 14) {
                eventName = eventName.substring(0, 11) + "...";
            }
            String eventTime = cm.getEventTimes(year, month, usedContentDates.get(contentCount)).get(eventIndex);
            String tempDiv = " ".repeat(this.dayOfWeekCollection.get(startingIndex + contentCount).length() +
                    Constants.CAL_ROW_SPACER - eventName.length() - 3 - eventTime.length());
            result.append(" ").append(eventName).append(": ").append(eventTime).append(tempDiv).append("|");
        } else if (calendarMap.get(usedContentDates.get(contentCount)).size() - 1 < eventIndex) {
            String tempDiv = " ".repeat(this.dayOfWeekCollection.get(startingIndex + contentCount).length() +
                    Constants.CAL_ROW_SPACER);
            result.append(tempDiv).append("|");
        }
        contentCount += 1;
        return contentCount;
    }

    private int fillBeforeStartDate(StringBuilder result, int startingIndex) {
        String tempDiv = " ".repeat(this.dayOfWeekCollection.get(startingIndex).length() + Constants.CAL_ROW_SPACER);
        result.append(tempDiv).append("|");
        startingIndex += 1;
        return startingIndex;
    }

    private void fillRestRow(StringBuilder result, int count, int index) {
        while (index + count < this.dayOfWeekCollection.size()) {
            String tempDiv = " ".repeat(this.dayOfWeekCollection.get(index + count).length() +
                    Constants.CAL_ROW_SPACER);
            result.append(tempDiv).append("|");
            count += 1;
        }
    }
}
