package usecases.events;

import entities.Event;
import entities.recursions.RecursiveEvent;
import interfaces.EventInfoGetter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * EventInfoGetter - gets information about events with input UUID's stored in attribute eventManager
 *
 * @author Taite Cullen
 */
public class DefaultEventInfoGetter implements EventInfoGetter {
    public final EventManager eventManager;

    /**
     * constructs a default event info getter
     *
     * @param eventManager EventManager
     */
    public DefaultEventInfoGetter(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * returns the ID of an Event (does not have to be in <code>this.eventMap</code>
     *
     * @param event any Event
     * @return the ID of the Event (Event.getID())
     */
    @Override
    public UUID getID(Event event) {
        return event.getID();
    }

    /**
     * returns all the values in <code>this.eventMap</code>
     *
     * @return list of events (without work sessions, not split)
     */
    @Override
    public List<Event> getAllEvents() {
        List<Event> allEvents = new ArrayList<>(eventManager.getEventMap().values());
        for (RecursiveEvent recursiveEvent : eventManager.getRepeatedEventManager().getRecursiveEventMap().values()) {
            allEvents.addAll(eventManager.recursiveEventList(recursiveEvent));
        }
        return eventManager.timeOrder(allEvents);
    }

    /**
     * returns the name of any event with eventID [event.getName()]
     *
     * @param eventID ID of an event
     * @return the name of the event
     */
    @Override
    public String getName(UUID eventID) {
        return eventManager.get(eventID).getName();
    }

    /**
     * Sets the name of any event (does not have to be in <code>this.eventMap</code>
     *
     * @param event the event to set name
     * @param name  String of new name
     */
    public void setName(UUID event, String name) {
        eventManager.get(event).setName(name);
    }

    /**
     * returns the name of event
     *
     * @param event Event
     * @return String event.getName()
     */
    public String getName(Event event) {
        return event.getName();
    }

    /**
     * Sets the description of an event, does not have to be in <code>this.eventMap</code>
     *
     * @param event    the event with description to be set
     * @param describe String the new description
     */
    public void setDescription(UUID event, String describe) {
        eventManager.get(event).setDescription(describe);
    }

    /**
     * Get description of a specific event
     *
     * @param eventID ID of the event
     * @return get description of the event from eventID
     */
    @Override
    public String getDescription(UUID eventID) {
        if (eventManager.getEventMap().containsKey(eventID)) {
            if (eventManager.getEventMap().get(eventID).getDescription() != null) {
                return eventManager.getEventMap().get(eventID).getDescription();
            } else {
                return "No description provided";
            }
        } else {
            return null;
        }
    }

    /**
     * changes the start time of the Event with id in eventManager
     *
     * @param id    UUID of event
     * @param start LocalDateTime new start
     */
    public void setStart(UUID id, LocalDateTime start) {
        if (start == null) {
            eventManager.get(id).setStartTime(eventManager.get(id).getEndTime());
        } else {
            eventManager.get(id).setStartTime(start);
        }
        eventManager.update("change", eventManager.get(id));
    }

    /**
     * changes the end time of Event in eventManager with UUID id
     *
     * @param id  UUID of Event
     * @param end LocalDateTime new end time
     */
    public void setEnd(UUID id, LocalDateTime end) {
        eventManager.get(id).setEndTime(end);
        eventManager.update("change", eventManager.get(id));
    }

    /**
     * Return the end time information of the chosen event in string
     *
     * @param eventID ID of an event to investigate
     * @return the string of the end time
     */
    public String getEndTimeString(UUID eventID) {
        Event event = eventManager.get(eventID);
        String[] date = event.getEndTime().toString().split("-");
        return date[2].substring(3, 8);
    }

    /**
     * Return the End date of the event
     *
     * @param id ID of the event
     * @return the end date of the event
     */
    public LocalDate getEndDate(UUID id) {
        return eventManager.get(id).getEndTime().toLocalDate();
    }

    /**
     * Return the start date of the event
     *
     * @param id ID of the event
     * @return the start date of the event
     */
    public LocalDate getStartDate(UUID id) {
        if (eventManager.get(id).hasStart()) {
            return eventManager.get(id).getStartTime().toLocalDate();
        } else {
            return null;
        }
    }

    /**
     * Return the end time of the event
     *
     * @param id ID of the event
     * @return the end time of the event
     */
    public LocalTime getEndTime(UUID id) {
        return eventManager.get(id).getEndTime().toLocalTime();
    }

    /**
     * Return the start time of the event
     *
     * @param id ID of the event
     * @return the start time of the event
     */
    public LocalTime getStartTime(UUID id) {
        if (eventManager.get(id).hasStart()) {
            return eventManager.get(id).getStartTime().toLocalTime();
        } else {
            return null;
        }
    }

    /**
     * Return the end date time of the event
     *
     * @param event selected event
     * @return Return the end date time of the event
     */
    public LocalDateTime getEnd(Event event) {
        return event.getEndTime();
    }

    /**
     * gets the full LocalDateTime end of Event with UUID eventID
     *
     * @param eventID UUID of Event in EventManager
     * @return LocalDateTime event.getEndTime()
     */
    @Override
    public LocalDateTime getEnd(UUID eventID) {
        return eventManager.get(eventID).getEndTime();
    }

    /**
     * gets the full LocalDateTime start of Event event
     *
     * @param event Even
     * @return LocalDateTime event.getStartTime()
     */
    public LocalDateTime getStart(Event event) {
        return event.getStartTime();
    }

    /**
     * gets the full LocalDateTime start of Event with UUID eventID
     *
     * @param event UUID of Event in EventManager
     * @return LocalDateTime event.getStartTime()
     */
    @Override
    public LocalDateTime getStart(UUID event) {
        return eventManager.get(event).getStartTime();
    }

    /**
     * gets the numerical length in hours of event (difference between start and end time)
     *
     * @param event Event
     * @return double length of event from start to end
     */
    public double getLength(Event event) {
        return event.getLength();
    }
}
