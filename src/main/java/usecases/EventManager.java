package usecases;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDate;

import entities.ConstantID;
import entities.Event;
import entities.RecursiveEvent;
import interfaces.EventListObserver;

/**
 * @author Taite Cullen
 * @author Malik Lahlou
 * stores and Manages events
 * edits Events by dictionary key (unique ID)
 * updates EventListObservers when Event is added to its eventMap, removed from its eventMap, or
 * the start or end time of an <code>Event</code> within its <code>eventMap</code> is modified
 */
public class EventManager{
    private final Map<Integer, Event> eventMap;
    private RepeatedEventManager repeatedEventManager;
    private EventListObserver[] toUpdate;

    /**
     * constructs event manager. stores list of events by key: ID, value: event in <code>this.eventMap</code>
     * sets <code>this.toUpdate</code> to empty list of <code>EventListObservers</code>
     * @param events a list of events to be stored in <code>this.eventMap</code>
     */
    public EventManager(ArrayList<Event> events){
        if (events.isEmpty()) {
            this.eventMap = new HashMap<>();
        } else {
            this.eventMap = new HashMap<>();
            for (Event event: events){
                this.eventMap.put(event.getID(), event);
            }
        }
        this.toUpdate = new EventListObserver[]{};
    }


    /**
     * constructs empty EventManager (no Events or Observers)
     */
    public EventManager() {
        this.eventMap = new HashMap<>();
        this.toUpdate = new EventListObserver[]{};
    }

    /**
     * returns the ID of an Event (does not have to be in <code>this.eventMap</code>
     * @param event any Event
     * @return the ID of the Event (Event.getID())
     */
    public Integer getID(Event event) {
        return event.getID();

    }

    /**
     * getDay returns a map of the events in a day
     * @param day the day that is being searched for
     * @return all events in this day
     */
    public Map<Integer, Event> getDay(LocalDate day){
        Map<Integer, Event> dayMap = new HashMap<>();
        for (Event event: eventMap.values()){
            if (event.getDay().isEqual(day)) {
                dayMap.put(event.getID(), event);
            }
        }
        return dayMap;
    }

    /**
     *
     * @param ID the name of an existing event
     * @return the event of this name
     */
    public Event get(Integer ID){
        return eventMap.get(ID);
    }

    /**
     * removes an event from the set
     * @param ID the name to be removed
     * @return the event just removed
     */
    public Event remove(Integer ID){
        ArrayList<Event> truc = new ArrayList<>();
        truc.add(this.get(ID));
        this.update("remove", truc);
        return eventMap.remove(ID);
    }

    /**
     *
     * @param year year of event
     * @param month month of event
     * @param day date of event
     * @param startHour time event starts (form HHMM)
     * @param endHour time event ends (form HHMM)
     * @param startMin start minute
     * @param endMin end minute
     */
    public Event addEvent(String name, int year, int month, int day, int startHour, int startMin, int endHour,
                         int endMin){
        Event event = new Event(ConstantID.get(), name, year, month, day, startHour, endHour, startMin, endMin);
        this.eventMap.put(event.getID(), event);
        ArrayList<Event> truc = new ArrayList<>();
        truc.add(event);
        this.update("add", truc);
        return event;
    }

    public Event addEvent(String name, int year, int month, int day, int endHour,
                          int endMin){
        Event event = new Event(ConstantID.get(), name, year, month, day, endHour, endMin);
        this.eventMap.put(event.getID(), event);
        ArrayList<Event> truc = new ArrayList<>();
        truc.add(event);
        this.update("add", truc);
        return event;
    }

    public void addEvent(String name, LocalDateTime start, LocalDateTime end){
        Event event = new Event(ConstantID.get(), name, start, end);
        this.eventMap.put(event.getID(), event);
        ArrayList<Event> truc = new ArrayList<>();
        truc.add(event);
        this.update("add", truc);
    }

    private List<Event> flattenWorkSessions(List<Event> events){
        List<Event> flat = new ArrayList<>();
        for (Event event: events){
            flat.add(event);
            if (!event.getWorkSessions().isEmpty()){flat.addAll(event.getWorkSessions());}
        }
        return flat;
    }
    public ArrayList<Event> getAllEvents() {
        return new ArrayList<>(this.eventMap.values());
    }

    public String getName(Event event){
        return event.getName();
    }
    public String getStart(Event event) {return event.getStartString();}

    public void setStart(Event event, String startString){
        event.setStartTime(stringToDate(startString));
        ArrayList<Event> truc = new ArrayList<>();
        truc.add(event);
        this.update("change", truc);
    }

    public void setEnd(Event event, String endString){
        event.setEndTime(stringToDate(endString));
        ArrayList<Event> truc = new ArrayList<>();
        truc.add(event);
        this.update("change", truc);
    }

    private LocalDateTime stringToDate(String endString) {
        String[] full = endString.split(" ");
        String[] time = full[1].split(":");
        String[] date = full[0].split("-");
        return LocalDateTime.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]),
                Integer.parseInt(date[2]), Integer.parseInt(time[0]), Integer.parseInt(time[1]));
    }

    public String getStartTime(Event event){
        String[] date = event.getStartString().split("-");
        return date[2].substring(3, 8);
    }

    public Integer[] getStartInt(Event event){
        return new Integer[] {event.getStartTime().getYear(), event.getStartTime().getMonthValue(),
                event.getStartTime().getDayOfMonth(), event.getStartTime().getHour(), event.getStartTime().getMinute()};
    }

    public Integer[] getEndInt(Event event){
        return new Integer[] {event.getEndTime().getYear(), event.getEndTime().getMonthValue(),
                event.getEndTime().getDayOfMonth(), event.getEndTime().getHour(), event.getEndTime().getMinute()};
    }

    public Integer[] getStartEndInt(Event event){
        Integer[] concat = new Integer[10];
        for (int i = 0; i < 5; i ++){
            concat[i] = this.getStartInt(event)[i];
        }
        for (int i = 0; i < 5; i ++){
            concat[i+5] = this.getEndInt(event)[i];
        }
        return concat;
    }
    public String getEndTime(Event event){
        String[] date = event.getEndString().split("-");
        return date[2].substring(3, 8);
    }

    public String getEnd(Event event) {return event.getEndString();}

    public void update(String addRemoveChange, ArrayList<Event> changed){
        for (EventListObserver obs: this.toUpdate){
            obs.update(addRemoveChange, changed, this);
        }
    }

    public void addObserver(EventListObserver obs){
        ArrayList<EventListObserver> inter = new ArrayList<>(List.of(this.toUpdate));
        inter.add(obs);
        this.toUpdate = inter.toArray(new EventListObserver[0]);
    }

    public float totalHours(List<Event> events){
        float hours = 0;
        for(Event event: events) {hours += event.getLength();}
        return hours;
    }

    public Event earliest(List<Event> events){
        Event earliest = events.get(0);
        for (Event event: events){
            if (event.getStartTime().isBefore(earliest.getStartTime())){
                earliest = event;
            }
        }
        return earliest;
    }
    public List<Event> timeOrder(List<Event> events){
        List<Event> sorted = new ArrayList<>();
        events = new ArrayList<>(events);
        while (!(events.isEmpty())){
            sorted.add(earliest(events));
            events.remove(earliest(events));
        }
        events = sorted;
        return events;
    }

    public String getDescription(Event event){
        return event.getDescription();
    }

    public String displayEvent(Integer ID){
        Event event = this.get(ID);
        return this.getID(event) + "\nname: " + this.getName(event) + "\nstart: " +
                this.getStart(event) + "\nend: " + this.getEnd(event) + "\ndescription: " + this.getDescription(event);
    }

    public boolean containsID(Integer ID){
        return this.eventMap.containsKey(ID);
    }

    public Map<LocalDate, List<Event>> getRange(LocalDate from, LocalDate to){
        Map<LocalDate, List<Event>> range = new HashMap<>();
        while (from.isBefore(to)){
            range.put(from, this.flattenWorkSessions(List.of(this.getDay(from).values().toArray(new Event[0]))));
            from  = from.plusDays(1L);
        }
        return range;
    }

    public Map<LocalDateTime, Long> freeSlots(LocalDateTime start, List<Event> events, LocalDateTime end){
        List<Event> schedule = this.timeOrder(this.flattenWorkSessions(events));
        Map<LocalDateTime, Long> lengthStart = new HashMap<>();
        int taskNum = 0;
        for (Event event: schedule){
            if (event.getEndTime().isBefore(end) && event.getStartTime().isAfter(start)){
                if (lengthStart.isEmpty()){
                    lengthStart.put(start, Duration.between(start, event.getStartTime()).getSeconds());
                }else{
                    lengthStart.put(schedule.get(taskNum-1).getStartTime(), Duration.between(schedule.get(taskNum-1).getEndTime(),
                                    event.getStartTime()).getSeconds());
                }
            }if(schedule.get(taskNum + 1).getStartTime().isAfter(end)){
                lengthStart.put(event.getEndTime(), Duration.between(event.getEndTime(), end).getSeconds());
            }
            taskNum ++;
        }
        return lengthStart;
    }

    public void setName(Event event, String name) {
        event.setName(name);
    }

    public void setDescription(Event event, String descrip) {
        event.setDescription(descrip);
    }


    public void addEvent(Event event){this.eventMap.put(event.getID(), event);}

    public void setRepeatedEventManager(RepeatedEventManager repeatedEventManager) {
        this.repeatedEventManager = repeatedEventManager;
    }

    /**
     * TODO Malik
     * Given a RecursiveEvent, this method gets all the events in the period of repetition and adds them to the
     * event manager event list.
     */

    public void addEventsInRecursion(RecursiveEvent recursiveEvent){
        for(ArrayList<Event> events : repeatedEventManager.getEventsFromRecursion(recursiveEvent.getId()).values()){
            for(Event event : events){
                this.addEvent(event);
            }
        }
    }

    /**
     * TODO Malik
     */
    public void addEventsInRecursion() {
        for (RecursiveEvent recursiveEvent : this.repeatedEventManager.getRecursiveEventMap().values()) {
            this.addEventsInRecursion(recursiveEvent);
        }
    }


}
