package helpers;

import entities.Event;
import usecases.events.EventManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventHelper {
    /**
     * orders a list of events chronologically earliest to latest
     *
     * @param events the list to be modified
     * @return the input list, time ordered
     */
    public List<Event> timeOrder(List<Event> events) {
        List<Event> sorted = new ArrayList<>();
        events = new ArrayList<>(events);
        while (!(events.isEmpty())) {
            sorted.add(earliest(events));
            events.remove(earliest(events));
        }
        events = sorted;
        return events;
    }

    /**
     * computes the earliest startTime in a list of events (chronologically first)
     *
     * @param events the list of events over which to compare startTimes
     * @return the event with the earliest start time
     */
    public Event earliest(List<Event> events) {
        Event earliest = events.get(0);
        LocalDateTime earliestStartTime = earliest.getEndTime();
        if (earliest.hasStart()) {
            earliestStartTime = earliest.getStartTime();
        }
        for (Event event : events) {
            LocalDateTime eventStartTime;
            if (!event.hasStart()) {
                eventStartTime = event.getEndTime();
            } else {
                eventStartTime = event.getStartTime();
            }
            if (eventStartTime.isBefore(earliestStartTime)) {
                earliest = event;
                earliestStartTime = eventStartTime;
            }
        }
        return earliest;
    }

    public LocalDateTime startTimeGetter(Event event){
        if(event.getStartTime() == null){
            return event.getEndTime();
        }
        else{
            return event.getStartTime();
        }
    }

    /**
     * Orders a list of event IDs chronologically earliest to latest
     *
     * @param eventIDList the list of event ID to be modified
     * @param eventManager
     * @return the input list, time ordered
     */
    public List<UUID> timeOrderID(List<UUID> eventIDList, EventManager eventManager) {
        List<Event> eventList = new ArrayList<>();
        for (UUID eventID : eventIDList) {
            eventList.add(eventManager.get(eventID));
        }
        eventList = timeOrder(eventList);
        List<UUID> sortedEventID = new ArrayList<>();
        for (Event event : eventList) {
            sortedEventID.add(eventManager.getDefaultEventInfoGetter().getID(event));
        }
        return sortedEventID;
    }
}
