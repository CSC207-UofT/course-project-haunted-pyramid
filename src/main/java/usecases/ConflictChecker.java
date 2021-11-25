package usecases;

import entities.OurCalendar;
import helpers.IsOverlapped;
import usecases.calendar.CalendarManager;
import usecases.events.EventManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConflictChecker {
    private final EventManager eventManager;
    private final CalendarManager calendarManager;

    /**
     * initialize the conflict checker class
     * @param eventManager EventManager object that has all the event information
     * @param calendarManager CalendarManager object to explore
     */
    public ConflictChecker(EventManager eventManager, CalendarManager calendarManager) {
        this.eventManager = eventManager;
        this.calendarManager = calendarManager;
    }

    /**
     * Check if there exist any conflict for the given year, month, and date
     * return the list of conflicted events' IDs
     * @param year given year
     * @param month given month
     * @param date given date
     * @return a list of conflicted event IDs
     */
    public List<UUID> notifyConflict(int year, int month, int date) {
        List<UUID> conflictEventList = new ArrayList<>();
        int adjustedMonth = this.calendarManager.adjustMonth(year, month);
        int currentMonth = this.calendarManager.getCurrentMonth();
        if (adjustedMonth == currentMonth) {
            OurCalendar currentCalendar = this.calendarManager.getCurrentCalendar();
            updatedConflict(date, conflictEventList, currentCalendar);
        }
        else if (adjustedMonth > currentMonth && currentMonth + 4 > adjustedMonth) {
            OurCalendar futureCalendar = this.calendarManager.getFutureCalendar().get(adjustedMonth - currentMonth - 1);
            updatedConflict(date, conflictEventList, futureCalendar);
        }
        else if (adjustedMonth < currentMonth && currentMonth - 4 < adjustedMonth) {
            OurCalendar pastCalendar = this.calendarManager.getPastCalendar().get(currentMonth - adjustedMonth - 1);
            updatedConflict(date, conflictEventList, pastCalendar);
        }
        return conflictEventList;
    }

    /**
     * update conflictEventList by observing events in calendar for the specific date
     * @param date given date to look for conflict
     * @param conflictEventList List of conflicted events to be updated
     * @param calendar OurCalendar object to be explored
     */
    private void updatedConflict(int date, List<UUID> conflictEventList, OurCalendar calendar) {
        List<UUID> eventIDList = calendar.getCalendarMap().get(date);
        addConflictEventID(conflictEventList, eventIDList);
        setConflict(conflictEventList, calendar);
    }

    /**
     * If there is any conflicted events, update the calendar attributes
     * @param conflictEventList list of conflicted events to check from
     * @param calendar calendar to be updated
     */
    private void setConflict(List<UUID> conflictEventList, OurCalendar calendar) {
        if (conflictEventList.size() != 0) {
            calendar.setConflict(true);
            calendar.setConflictEvent(conflictEventList);
        }
    }

    /**
     * Using the helper class IsOverlapped, check for conflicted events
     * @param conflictEventList Conflicted events ID List to be updated
     * @param eventIDList List of event IDs to consider from
     */
    private void addConflictEventID(List<UUID> conflictEventList, List<UUID> eventIDList) {
        for (int i = 0; i < eventIDList.size() - 1; i++) {
            List<LocalDateTime> firstTimeList = getTimeList(eventIDList, i);
            for (int j = i + 1; j < eventIDList.size(); j++) {
                List<LocalDateTime> secondTimeList = getTimeList(eventIDList, j);
                IsOverlapped isOverlapped = new IsOverlapped(firstTimeList, secondTimeList);
                if (isOverlapped.getResult()) {
                    if (!conflictEventList.contains(eventIDList.get(i))) {
                        conflictEventList.add(eventIDList.get(i));
                    }
                    if (!conflictEventList.contains(eventIDList.get(j))) {
                        conflictEventList.add(eventIDList.get(j));
                    }
                }
            }
        }
    }

    /**
     * Helper method to evaluate time information
     * @param eventIDList event ID list to be considered from
     * @param index index of the event ID list that we would like to extract start and end time from
     * @return the list of local date time (two elements, start and end time)
     */
    private List<LocalDateTime> getTimeList(List<UUID> eventIDList, int index) {
        List<LocalDateTime> firstTimeList = new ArrayList<>();
        LocalDateTime startTime = eventManager.get(eventIDList.get(index)).getStartTime();
        LocalDateTime endTime = eventManager.get(eventIDList.get(index)).getEndTime();
        if (startTime == null) {
            startTime = endTime;
        }
        firstTimeList.add(startTime);
        firstTimeList.add(endTime);
        return firstTimeList;
    }
}
