package interfaces;

import entities.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventInfoGetter {
    /**
     * get ID of an event
     * @param event event to get ID from
     * @return ID of an event
     */
    UUID getID(Event event);

    /**
     * get list of all events
     * @return list of events
     */
    List<Event> getAllEvents();

    /**
     * get name of the event
     * @param eventID ID of an event to get name from
     * @return name of the event
     */
    String getName(UUID eventID);

    /**
     * Get start information from the event
     * @param event ID of an event to get start information from
     * @return start information of an event
     */
    LocalDateTime getStart(UUID event);

    /**
     * Get end information from the event
     * @param event ID of an event to get end information from
     * @return end information of an event
     */
    LocalDateTime getEnd(UUID event);

    /**
     * get description of the event
     * @param eventID ID of an event to get description from
     * @return description of the event
     */
    String getDescription(UUID eventID);
}
