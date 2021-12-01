package presenters.CalendarFactory;

import usecases.events.EventManager;

import java.time.LocalDate;
import java.util.UUID;

public class CalendarTimePresenter {
    private EventManager eventManager;

    public CalendarTimePresenter(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public String getStartTime(UUID eventID, int year, int month, int date) {
        LocalDate givenStartDate = LocalDate.of(year, month, date);
        if (eventManager.getStartDate(eventID).isEqual(givenStartDate)) {
            return eventManager.getStartTimeString(eventID);
        }
        else if (eventManager.getStartDate(eventID).isBefore(givenStartDate)) {
            return "00:00";
        }
        return null;
    }

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
