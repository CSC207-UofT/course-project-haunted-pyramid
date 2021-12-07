package interfaces;

import entities.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventInfoGetter {

    UUID getID(Event event);

    List<Event> getAllEvents();

    String getName(UUID eventID);

    LocalDateTime getStart(UUID event);

    LocalDateTime getEnd(UUID event);

    String getDescription(UUID eventID);
}
