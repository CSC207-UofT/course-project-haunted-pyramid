package usecases.events;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.time.LocalDate;

import entities.Event;
import entities.recursions.RecursiveEvent;

import helpers.EventHelper;
import interfaces.EventListObserver;

/**
 * stores and Manages events
 * edits Events by dictionary key (unique ID)
 * updates EventListObservers when Event is added to its eventMap, removed from its eventMap, or
 * the start or end time of an <code>Event</code> within its <code>eventMap</code> is modified
 *
 * @author Sebin Im
 * @author Taite Cullen
 * @author Malik Lahlou
 * @author Seo Won Yi
 */

public class EventManager {
    private final Map<UUID, Event> eventMap;
    private final RepeatedEventManager repeatedEventManager;
    private EventListObserver[] toUpdate;
    public EventHelper eventHelper = new EventHelper();

    private Map<UUID, List<Event>> uuidEventsMap;
    private Map<UUID, Map<UUID, RecursiveEvent>> uuidRecursiveEventsMap;

    /**
     * constructs event manager. stores list of events by key: ID, value: event in <code>this.eventMap</code>
     * sets <code>this.toUpdate</code> to empty list of <code>EventListObservers</code>
     *
     * @param events a list of events to be stored in <code>this.eventMap</code>
     */
    public EventManager(List<Event> events) {
        if (events.isEmpty()) {
            this.eventMap = new HashMap<>();
        } else {
            this.eventMap = new HashMap<>();
            for (Event event : events) {
                this.eventMap.put(event.getID(), event);
            }
        }
        this.toUpdate = new EventListObserver[]{};
        this.repeatedEventManager = new RepeatedEventManager();
    }

    /**
     * constructs event manager. stores list of events by key: ID, value: event in <code>this.eventMap</code>
     * sets <code>this.toUpdate</code> to empty list of <code>EventListObservers</code>
     *
     * @param events a list of events to be stored in <code>this.eventMap</code>
     */
    public EventManager(List<Event> events, Map<UUID, RecursiveEvent> recursiveEventMap) {
        if (events.isEmpty()) {
            this.eventMap = new HashMap<>();
        } else {
            this.eventMap = new HashMap<>();
            for (Event event : events) {
                this.eventMap.put(event.getID(), event);
            }
        }
        this.toUpdate = new EventListObserver[]{};
        this.repeatedEventManager = new RepeatedEventManager(recursiveEventMap);
    }

    /**
     * Get this Events map
     *
     * @return A map of UUID of users as keys and list of events of that user as values
     */
    public Map<UUID, List<Event>> getUuidEventsMap() {
        return this.uuidEventsMap;
    }

    /**
     * Get this RecursiveEvents map
     *
     * @return A map of UUID of users as keys and list of events of that user as values
     */
    public Map<UUID, Map<UUID, RecursiveEvent>> getUuidRecursiveEventsMap() {
        return uuidRecursiveEventsMap;
    }

    /**
     * Set this Events map to the parameter
     *
     * @param map A map of UUID of users as keys and list of events of that user as values
     */
    public void setUuidEventsMap(Map<UUID, List<Event>> map) {
        this.uuidEventsMap = map;
    }

    /**
     * Set this RecursiveEvents map to the parameter
     *
     * @param uuidRecursiveEventsMap A map of UUID of maps of UUID as keys and list of events of that user as values
     */
    public void setUuidRecursiveEventsMap(Map<UUID, Map<UUID, RecursiveEvent>> uuidRecursiveEventsMap) {
        this.uuidRecursiveEventsMap = uuidRecursiveEventsMap;
    }

    /**
     * returns an event in <code>this.eventMap</code> with the input ID if it is there, otherwise returns null
     *
     * @param eventID the ID of an event
     * @return the event with this ID, or null
     */
    public Event get(UUID eventID) {
        if (this.eventMap.containsKey(eventID)) {
            return eventMap.get(eventID);
        } else {
            for (Event event : getDefaultEventInfoGetter().getAllEvents()) {
                if (!event.getWorkSessions().isEmpty()) {
                    for (Event session : event.getWorkSessions()) {
                        if (session.getID().equals(eventID)) {
                            return session;
                        }
                    }
                }
            }
        }
        return this.repeatedEventManager.getThisEventFromRecursion(eventID);
    }

    public List<Event> getEvents(List<UUID> eventIDList) {
        List<Event> result = new ArrayList<>();
        for (UUID uuid : eventIDList) {
            result.add(eventMap.get(uuid));
        }
        return result;
    }

    /**
     * removes the event of this ID from <code>this.eventMap</code> if it is there, returns the removed event or null
     *
     * @param ID the name to be removed
     * @return the event just removed, or null
     */
    public Event remove(UUID ID) {
        this.update("remove", this.get(ID));
        return eventMap.remove(ID);
    }

    /**
     * removes the event of this ID from <code>this.eventMap</code> if it is there
     *
     * @param id the name to be removed
     */
    public void removeWithoutUpdate(UUID id) {
        eventMap.remove(id);
    }

    /**
     * creates an event with given name and end time and adds to <code>this.eventMap</code>
     *
     * @param title   String title of the Event
     * @param endTime LocalDateTime end time of the event
     * @return the event that was created with given title, endTime, and unique ID
     */
    public UUID addEvent(String title, LocalDateTime endTime) {
        Event event = new Event(UUID.randomUUID(), title, endTime);
        return this.addEvent(event);
    }

    /**
     * creates an event with given name and end time.
     *
     * @param title   String title of the Event
     * @param endTime LocalDateTime end time of the event
     * @return the event that was created with given title, endTime, and unique ID
     */
    public Event getEvent(String title, LocalDateTime endTime) {
        return new Event(UUID.randomUUID(), title, endTime);
    }

    /**
     * adds an already existing event to <code>this.eventMap</code>. will overwrite event of same ID
     *
     * @param event event to be added
     */
    public UUID addEvent(Event event) {
        this.eventMap.put(event.getID(), event);
        this.update("add", event);
        return this.getDefaultEventInfoGetter().getID(event);
    }

    /**
     * takes a list of events that may contain work sessions and returns the same list of events in addition to
     * the work sessions they contain
     *
     * @param events List<Event> a list of events that may contain work sessions
     * @return a list of events plus their work sessions
     */
    public List<Event> flattenWorkSessions(List<Event> events) {
        List<Event> flat = new ArrayList<>();
        if (events.isEmpty()) {
            return flat;
        }
        for (Event event : events) {
            flat.add(event);
            if (!event.getWorkSessions().isEmpty()) {
                flat.addAll(event.getWorkSessions());
            }
        }
        return flat;
    }

    /**
     * returns an Event as a list of events, splitting them at 24:00 each day it spans. For an event that spans
     * multiple days, will return an event with startTime = event.startTime, endTime the same day as the startTime
     * but with time = 24:00, and each subsequent event will have start and end 0:00-24:00 for each day the event spans
     * fully. The final event will have start time 0:00 on the date of the endTime, with endTime same as the original
     * events endTime
     *
     * @param event the event to be split, may or may not have start time or span multiple days
     * @return the list of events as split by day
     */
    public List<Event> splitByDay(Event event) {
        if (event.hasStart()) {
            List<Event> splitByDay = new ArrayList<>();
            if (event.getStartTime().toLocalDate().isBefore(event.getEndTime().toLocalDate())) {
                splitByDay.add(new Event(event.getID(), event.getName(), event.getStartTime(),
                        LocalDateTime.of(event.getStartTime().toLocalDate(), LocalTime.of(23, 59))));
                for (LocalDate nextDay = event.getStartTime().plusDays(1L).toLocalDate(); event.getEndTime().
                        toLocalDate().isAfter(nextDay); nextDay = nextDay.plusDays(1)) {
                    splitByDay.add(new Event(event.getID(), event.getName(), LocalDateTime.of(nextDay, LocalTime.of(0, 0)),
                            LocalDateTime.of(nextDay, LocalTime.of(23, 59))));
                }
                splitByDay.add(new Event(event.getID(), event.getName(), LocalDateTime.of(event.getEndTime().toLocalDate(),
                        LocalTime.of(0, 0)), event.getEndTime()));
                return splitByDay;
            }
        }
        return new ArrayList<>(List.of(new Event[]{event}));
    }

    public RepeatedEventManager getRepeatedEventManager() {
        return repeatedEventManager;
    }

    /**
     * @param recursiveEvent The RecursiveEvent from which the repeated events should be extracted.
     * @return Given a RecursiveEvent, this method returns all the events in the period of repetition specified in the
     * RecursiveEvent object.
     */

    public List<Event> recursiveEventList(RecursiveEvent recursiveEvent) {
        List<Event> result = new ArrayList<>();
        for (List<Event> events : repeatedEventManager.getRecursiveIdToDateToEventsMap().get(recursiveEvent.getId()).values()) {
            result.addAll(events);
        }
        return result;
    }

    /**
     * returns ArrayList of all events in <code>this.eventMap</code>, including work sessions within events and
     * repeated events, split at day boundaries
     *
     * @return list of events, including work sessions within events (flattened)
     */
    public List<Event> getAllEventsFlatSplit() {
        List<Event> events = new ArrayList<>();
        for (Event event : this.flattenWorkSessions(new ArrayList<>(this.eventMap.values()))) {
            events.addAll(this.splitByDay(event));
        }
        for (RecursiveEvent recursiveEvent : repeatedEventManager.getRecursiveEventMap().values()) {
            List<Event> repeatedEvents = recursiveEventList(recursiveEvent);
            for (Event event : repeatedEvents) {
                events.addAll(this.splitByDay(event));
            }
        }
        return events;
    }

    /**
     * updates all eventList observers in <code>this.toUpdate</code> with given parameter. Runs when events are added,
     * removed, or times change in <code>this.eventMap</code>
     *
     * @param addRemoveChange string "add" or "remove" or "change" to specify the nature of the update
     * @param changed         list of the events that are modified
     */
    public void update(String addRemoveChange, Event changed) {
        for (EventListObserver obs : this.toUpdate) {
            obs.update(addRemoveChange, changed, this);
        }
    }

    /**
     * adds an <code>EventListObserver</code> to <code>this.toUpdate</code>
     *
     * @param obs the observer to be added
     */
    public void addObserver(EventListObserver obs) {
        List<EventListObserver> inter = new ArrayList<>(List.of(this.toUpdate));
        inter.add(obs);
        this.toUpdate = inter.toArray(new EventListObserver[0]);
    }

    /**
     * Removes an EventListObserver from list that is updated in update method
     *
     * @param obs EventListObserver to be removed
     */
    public void removeObserver(EventListObserver obs) {
        List<EventListObserver> inter = new ArrayList<>(List.of(this.toUpdate));
        inter.remove(obs);
        this.toUpdate = inter.toArray(new EventListObserver[0]);
    }

    /**
     * calculates the total hours in a list of events using event.getLength and summing
     *
     * @param events list of events with lengths to be added
     * @return the total length of time in hours spent on these events
     */
    public float totalHours(List<Event> events) {
        float hours = 0;
        for (Event event : events) {
            hours += event.getLength();
        }
        return hours;
    }

    /**
     * returns String representation of event
     *
     * @param event UUID of event
     * @return Event.toString()
     * @see Event#toString()
     */
    public String displayEvent(Event event) {
        return event.toString();
    }

    /**
     * checks if an Event of this ID is contained in <code>this.eventMap</code>
     *
     * @param eventID any UUID
     * @return true if an event with this integer ID is in <code>this.eventMap</code>, false otherwise
     */
    public boolean containsID(UUID eventID) {
        return !(this.get(eventID) == null);
    }


    /**
     * Return the start time information of the chosen event in string
     *
     * @param eventID ID of an event to investigate
     * @return the string of the start time
     */
    public String getStartTimeString(UUID eventID) {
        Event event = this.get(eventID);
        if (event.hasStart()) {
            String[] date = event.getStartTime().toString().split("-");
            return date[2].substring(3, 8);
        } else {
            return null;
        }
    }

    /**
     * Adds a list of events to eventMap
     *
     * @param events List of Events
     */
    public void addAll(List<Event> events) {
        for (Event event : events) {
            this.addEvent(event);
        }
    }

    /**
     * @return eventMap - Map with key: UUID, value: Event
     */
    public Map<UUID, Event> getEventMap() {
        return this.eventMap;
    }

    /**
     * constructs DefaultEventInfoGetter with this EventManager
     *
     * @return a new DefaultEventInfoGetter
     * @see DefaultEventInfoGetter
     * @see interfaces.EventInfoGetter
     */
    public DefaultEventInfoGetter getDefaultEventInfoGetter() {
        return new DefaultEventInfoGetter(this);
    }

    /**
     * Long startWorking for Event with UUID event
     *
     * @param event UUID of Event
     * @return Long startWorking
     */
    public LocalDate getStartWorking(UUID event) {
        return getDefaultEventInfoGetter().getEndDate(event).minusDays(get(event).getStartWorking());
    }

    /**
     * Orders a list of event IDs chronologically earliest to latest
     *
     * @param eventIDList the list of event ID to be modified
     * @return the input list, time ordered
     */
    public List<UUID> timeOrderID(List<UUID> eventIDList) {
        List<Event> eventList = new ArrayList<>();
        for (UUID eventID : eventIDList) {
            eventList.add(get(eventID));
        }
        eventList = eventHelper.timeOrder(eventList);
        List<UUID> sortedEventID = new ArrayList<>();
        for (Event event : eventList) {
            sortedEventID.add(getDefaultEventInfoGetter().getID(event));
        }
        return sortedEventID;
    }
}