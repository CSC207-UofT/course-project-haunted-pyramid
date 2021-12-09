package helpers;

import entities.Event;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventHelper implements Serializable {
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

    /**
     * Return the start time if the event has one, return the end time otherwise.
     *
     * @param event the event in question.
     */

    public LocalDateTime startTimeGetter(Event event){
        if(event.getStartTime() == null){
            return event.getEndTime();
        }
        else{
            return event.getStartTime();
        }
    }

}
