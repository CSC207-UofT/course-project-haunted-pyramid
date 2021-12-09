package interfaces;

import java.util.List;
import java.util.UUID;

public interface WorkSessionInfoGetter {

    /**
     * Get total hours needed for the event (work session hours)
     * @param eventID ID of an event
     * @return the total hour needed for the event
     */
    Long getTotalHoursNeeded(UUID eventID);

    /**
     * Get session length assigned for the event (work session hours)
     * @param eventID ID of an event
     * @return the session length assigned for the event
     */
    Long getEventSessionLength(UUID eventID);

    /**
     * get the number of days before deadline to start working
     * @param eventID ID of Event
     * @return number of days
     */
    Long getStartWorking(UUID eventID);

    /**
     * get the list of future sessions by ID
     * @param ID ID of an event that has work session set up
     * @return the list of future sessions
     */
    List<UUID> getFutureWorkSessions(UUID ID);

    /**
     * get the list of past sessions by ID
     * @param ID ID of an event that has work session set up
     * @return the list of past sessions
     */
    List<UUID> getPastWorkSessions(UUID ID);
}
