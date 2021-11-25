package presenters.CalendarFactory;

import helpers.Constants;
import helpers.DisplayCalendarHelper;
import presenters.ConflictPresenters.DisplayConflict;
import presenters.ConflictPresenters.DisplayMonthlyConflict;
import usecases.calendar.CalendarManager;
import usecases.calendar.MonthlyCalendarByType;
import usecases.events.EventManager;

import java.time.YearMonth;
import java.util.*;

/**
 * Build the image of monthly calendar and display if asked
 * @author Seo Won Yi
 * @see DisplayCalendar
 * @see DisplayCalendarFactory
 */
public class DisplayMonthlyCalendar extends DisplayCalendar {
    private final int year;
    private final int month;
    private final List<Integer> keyList;
    private final Map<Integer, List<UUID>> calendarMap;
    private final DisplayCalendarHelper cf;
    private final DisplayConflict conflictDisplay;
    List<String> dayOfWeekCollection = new ArrayList<>() {{
        add("SUNDAY");
        add("MONDAY");
        add("TUESDAY");
        add("WEDNESDAY");
        add("THURSDAY");
        add("FRIDAY");
        add("SATURDAY");
    }};

    /**
     * Initialize the class by setting up with the given year, month and CalendarManager object
     * @param cm CalendarManager object to get information from
     * @param year given year
     * @param month given month
     */
    public DisplayMonthlyCalendar(CalendarManager cm, EventManager em, int year, int month) {
        super(cm, em);
        this.year = year;
        this.month = month;
        MonthlyCalendarByType mc = new MonthlyCalendarByType();
        this.keyList = new ArrayList<>(mc.getCalendar(cm, year, month).keySet());
        calendarMap = mc.getCalendar(cm, year, month);
        Collections.sort(this.keyList);
        this.cf = new DisplayCalendarHelper(year, month);
        YearMonth tempYearMonth = YearMonth.of(year, month);
        this.conflictDisplay = new DisplayMonthlyConflict(cm, em, year, month, tempYearMonth.lengthOfMonth());
    }

    /**
     * Display the image of the monthly calendar
     * @return String image of the monthly calendar
     */
    @Override
    public String displayCalendar() {
        cf.eventSorter(calendarMap, em);
        StringBuilder result = new StringBuilder();
        List<Integer> usedDates = new ArrayList<>();
        List<Integer> usedContentDates = new ArrayList<>();
        usedContentDates = initialSetup(result, usedDates, usedContentDates);
        int iteratorCounter = keyList.subList(usedDates.size(), keyList.size()).size();
        fillCalendar(cf, result, usedDates, usedContentDates, iteratorCounter);
        result.append("\n").append(this.conflictDisplay.displayConflict());
        return result.toString();
    }

    /**
     * Set up the calendar and update the usedDates and usedContentsDates as dates and contents get added on the
     * StringBuilder
     * @param result StringBuilder object to be added on
     * @param usedDates list of dates that are used on the calendar already
     * @param usedContentDates list of dates that contents need to be added
     * @return list of updated usedContentDates
     */
    private List<Integer> initialSetup(StringBuilder result, List<Integer> usedDates, List<Integer> usedContentDates) {
        result.append(cf.startFrame("SUNDAY", 0));
        addDateRowToCalendar(result, usedDates, usedContentDates);
        addContentsToCalendar(result, usedContentDates);
        usedContentDates = new ArrayList<>();
        result.append(cf.endFrame(0));
        return usedContentDates;
    }

    /**
     * Depending on how many days are left, iterate to fill up the calendar with the contents and dates
     * @param cf DisplayCalendarHelper object that has various methods to be used
     * @param result StringBuilder object to be added on
     * @param usedDates list of dates that are used on the calendar already
     * @param usedContentDates list of dates that contents need to be added
     * @param iteratorCounter remaining dates after being used. determines the number of iterations
     */
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

    /**
     * Fill the calendar by adding dates and contents to the StringBuilder object result.
     * Update usedContentDates list
     * @param cf DisplayCalendarHelper object that has various methods to be used
     * @param result StringBuilder object to be added on
     * @param usedDates list of dates that are used on the calendar already
     * @param usedContentDates list of dates that contents need to be added
     * @return updated usedContentDates list
     */
    private List<Integer> fillCalendarHelper(DisplayCalendarHelper cf, StringBuilder result, List<Integer> usedDates,
                                             List<Integer> usedContentDates) {
        addDateRowToCalendar(result, usedDates, usedContentDates);
        addContentsToCalendar(result, usedContentDates);
        usedContentDates = new ArrayList<>();
        result.append(cf.endFrame(0));
        return usedContentDates;
    }

    /**
     * Add the first date in the row of calendar to the StringBuilder object result.
     * @param result StringBuilder object to be added on
     * @param usedDates list of dates that are used on the calendar already
     * @param usedContentDates list of dates that contents need to be added
     */
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

    /**
     * Add the rest of the dates after addDateRowToCalendar method to the calendar
     * @param result StringBuilder object to be added on
     * @param usedDates list of dates that are used on the calendar already
     * @param usedContentDates list of dates that contents need to be added
     * @param count index that helps track the day of the week
     */
    private void addRestDateToCalendar(StringBuilder result,
                                       List<Integer> usedDates, List<Integer> usedContentDates, int count) {

        while (count < this.dayOfWeekCollection.size() - 1 && usedDates.size() < keyList.size()) {
            result.append(" ").append(keyList.get(usedDates.size()));
            if (keyList.get(usedDates.size()) < 10) {
                result.append(" ".repeat(this.dayOfWeekCollection.get(count + 1).length() +
                        Constants.CAL_ROW_SPACER - 2)).append("|");
            } else {
                result.append(" ".repeat(this.dayOfWeekCollection.get(count + 1).length() +
                        Constants.CAL_ROW_SPACER - 3)).append("|");
            }
            usedContentDates.add(keyList.get(usedDates.size()));
            usedDates.add(keyList.get(usedDates.size()));
            count += 1;

        }

        if (usedDates.size() >= keyList.size()) {
            fillBlankSpace(result, count);
        }
    }

    /**
     * If the dates run out but the calendar still needs to be filled, fill it with the blank spaces
     * @param result StringBuilder object to be added on
     * @param count index that helps track the day of the week
     */
    private void fillBlankSpace(StringBuilder result, int count) {
        while (count < this.dayOfWeekCollection.size() - 1) {
            String tempDiv = " ".repeat(this.dayOfWeekCollection.get(count + 1).length() +
                    Constants.CAL_ROW_SPACER);
            result.append(tempDiv).append("|");
            count += 1;
        }
    }

    /**
     * Add contents to the StringBuilder result
     * @param result StringBuilder object to be added on
     * @param usedContentDates list of dates that contents need to be added
     */
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

    /**
     * Add content's ID, name and time information to the StringBuilder result
     * Update the contentCount to indicate the contents have been added
     * @param result StringBuilder object to be added on
     * @param usedContentDates list of dates that contents need to be added
     * @param eventIndex index of events on the same day
     * @param contentCount index to track the dates in usedContentDates
     * @param startingIndex Starting day of Week
     * @return Update the contentCount and increase it by one, so it can move to the next date
     */
    private int addContent(StringBuilder result, List<Integer> usedContentDates, int eventIndex, int contentCount,
                           int startingIndex) {
        if (calendarMap.get(usedContentDates.get(contentCount)).size() - 1 >= eventIndex &&
                calendarMap.get(usedContentDates.get(contentCount)).size() != 0) {
            UUID eventID = cm.getEventID(year, month, usedContentDates.get(contentCount)).get(eventIndex);
            int eventIntID = this.converter.getIntFromUUID(eventID);
            String eventName = em.getName(em.get(eventID));
            eventName = "ID:" + eventIntID + " " + eventName;
            if (eventName.length() > 17) {
                eventName = eventName.substring(0, 14) + "...";
            }
            String eventStartTime = em.getStartTimeString(eventID);
            String eventEndTime = em.getEndTimeString(eventID);
            if (eventStartTime == null) {
                String extra = " " + ": " + "Due ";
                String tempDiv = " ".repeat(this.dayOfWeekCollection.get(startingIndex + contentCount).length() +
                        Constants.CAL_ROW_SPACER - eventName.length() - extra.length() - eventEndTime.length());
                result.append(" ").append(eventName).append(": ")
                        .append("Due ").append(eventEndTime).append(tempDiv).append("|");
            }
            else {
                String eventTime = eventStartTime + "-" + eventEndTime;
                String tempDiv = " ".repeat(this.dayOfWeekCollection.get(startingIndex + contentCount).length() +
                        Constants.CAL_ROW_SPACER - eventName.length() - 3 - eventTime.length());
                result.append(" ").append(eventName).append(": ").append(eventTime).append(tempDiv).append("|");
            }
        } else if (calendarMap.get(usedContentDates.get(contentCount)).size() - 1 < eventIndex) {
            String tempDiv = " ".repeat(this.dayOfWeekCollection.get(startingIndex + contentCount).length() +
                    Constants.CAL_ROW_SPACER);
            result.append(tempDiv).append("|");
        }
        contentCount += 1;
        return contentCount;
    }

    /**
     * Fill empty spaces before the starting day of month. Update startingIndex until it reaches to the first day
     * @param result StringBuilder object to be added on
     * @param startingIndex index that indicates the first day of week to have a date on
     * @return updated startingIndex
     */
    private int fillBeforeStartDate(StringBuilder result, int startingIndex) {
        String tempDiv = " ".repeat(this.dayOfWeekCollection.get(startingIndex).length() + Constants.CAL_ROW_SPACER);
        result.append(tempDiv).append("|");
        startingIndex += 1;
        return startingIndex;
    }

    /**
     * Fill empty spaces after every date has been placed on the calendar
     * @param result
     * StringBuilder object to be added on
     * @param count tracks the day of week along with index
     * @param index tracks the day of week along with count
     */
    private void fillRestRow(StringBuilder result, int count, int index) {
        while (index + count < this.dayOfWeekCollection.size()) {
            String tempDiv = " ".repeat(this.dayOfWeekCollection.get(index + count).length() +
                    Constants.CAL_ROW_SPACER);
            result.append(tempDiv).append("|");
            count += 1;
        }
    }
}
