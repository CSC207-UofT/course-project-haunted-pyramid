package usecases;

import entities.Event;
import usecases.calendar.CalendarManager;
import usecases.events.EventManager;

import java.time.LocalDate;
import java.util.UUID;

public class EventCalendarCollaborator {
    private final EventManager eventManager;
    private final CalendarManager calendarManager;

    /**
     * initialize the class.
     * @param em EventManager object that has all the information of event
     * @param cm CalendarManager object that has all the calendar information
     */
    public EventCalendarCollaborator(EventManager em, CalendarManager cm) {
        this.eventManager = em;
        this.calendarManager = cm;
    }

    /**
     * add all the events to the calendar
     */
    public void addAllEvents() {
        for (Event event : this.eventManager.getAllEventsFlatSplit()) {
            UUID eventID = this.eventManager.getID(event);
            LocalDate eventDate = event.getEndTime().toLocalDate();
            this.calendarManager.addToCalendar(eventID, eventDate.getYear(), eventDate.getMonthValue(),
                    eventDate.getDayOfMonth());
        }
    }

    public CalendarManager getCalendarManager() {
        return this.calendarManager;
    }
}
