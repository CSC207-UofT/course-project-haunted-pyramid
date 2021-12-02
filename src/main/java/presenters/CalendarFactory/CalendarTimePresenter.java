package presenters.CalendarFactory;

import usecases.events.EventManager;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Generate time strings to appropriately display on the calendar
 * @author Seo Won Yi
 */

public class CalendarTimePresenter {
    private EventManager eventManager;

    /**
     * Initialize by setting up EventManager object
     * @param eventManager EventManager object that contains all the event information
     */
    public CalendarTimePresenter(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * Get start time of the event considered from the given time
     * i.e. if the event started a day before, return the start time as "00:00" (the event started as the day started
     * because it is continuing from the day before)
     * @param eventID ID of an event
     * @param year year to consider from
     * @param month month to consider from
     * @param date date to consider from
     * @return converted start time appropriate for the given date
     */
    public String getStartTime(UUID eventID, int year, int month, int date) {
        LocalDate givenStartDate = LocalDate.of(year, month, date);
        if (eventManager.getStartTime(eventID) == null) {
            return null;
        }
        else if (eventManager.getStartDate(eventID).isEqual(givenStartDate)) {
            return eventManager.getStartTimeString(eventID);
        }
        else if (eventManager.getStartDate(eventID).isBefore(givenStartDate)) {
            return "00:00";
        }
        return null;
    }

    /**
     * Get end time of the event considered from the given time
     * i.e. if the event ends a day after, return the end time as "24:00" (the event continues all day for the given
     * date)
     * @param eventID ID of an event
     * @param year year to consider from
     * @param month month to consider from
     * @param date date to consider from
     * @return converted end time appropriate for the given date
     */
    public String getEndTime(UUID eventID, int year, int month, int date) {
        LocalDate givenEndDate = LocalDate.of(year, month, date);
        if (eventManager.getEndDate(eventID).isEqual(givenEndDate)) {
            return eventManager.getEndTimeString(eventID);
        }
        else if (eventManager.getEndDate(eventID).isAfter(givenEndDate)) {
            return "24:00";
        }
        return null;
    }
}
