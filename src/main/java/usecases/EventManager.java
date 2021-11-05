package usecases;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.time.LocalDate;

import entities.ConstantID;
import entities.Event;
import interfaces.EventListObserver;


public class EventManager{
    private final Map<Integer, Event> eventMap;
    private EventListObserver[] toUpdate;
    //TODO temporary
    private Integer nextID;

    /**
     * constructor for event manager
     * @param events a list of the current users events
     */
    public EventManager(Event[] events){
        if (events.length == 0) {
            this.eventMap = new HashMap<>();
        } else {
            this.eventMap = new HashMap<>();
            for (Event event: events){
                this.eventMap.put(event.getID(), event);
            }
        }
        this.nextID = 0;
    }

    private Integer nextID(){
        nextID = nextID + 1;
        return this.nextID;
    }

    /**
     * empty EventManager
     */
    public EventManager(){
        this.eventMap = new HashMap<>();
    }

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
        return event;
    }
    public Event addEvent(String name, Integer[] datetime){
        Event event = new Event(ConstantID.get(), name, datetime[0], datetime[1], datetime[2], datetime[3], datetime[4],
                datetime[8], datetime[9]);
        this.eventMap.put(event.getID(), event);
        return event;
    }

    public void addToCollection(Event event, Integer ID){
        event.addToCollection(ID);
    }

    public ArrayList<Event> getAllEvents() { return new ArrayList<>(this.eventMap.values()); }

    public String getName(Event event){
        return event.getName();
    }
    public String getStart(Event event) {return event.getStartString();}

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
        for (Integer i = 0; i < 5; i ++){
            concat[i] = this.getStartInt(event)[i];
        }
        for (Integer i = 0; i < 5; i ++){
            concat[i+5] = this.getEndInt(event)[i];
        }
        return concat;
    }
    public String getEndTime(Event event){
        String[] date = event.getEndString().split("-");
        return date[2].substring(3, 8);
    }

    public String getEnd(Event event) {return event.getEndString();}

    public void update(String addRemoveChange, Map<Integer, Event> changed){
        for (EventListObserver obs: this.toUpdate){
            obs.update(addRemoveChange, changed);
        }
    }
    public void addObserver(EventListObserver obs){
        ArrayList<EventListObserver> inter = new ArrayList<>(List.of(this.toUpdate));
        inter.add(obs);
        this.toUpdate = inter.toArray(new EventListObserver[0]);
    }

    public String getAllNames(){
        StringBuilder list = new StringBuilder();
        for (Event event: eventMap.values()){
            list.append(event.getName());
        }
        return list.toString();
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
        while (!events.isEmpty()){
            sorted.add(earliest(events));
            events.remove(earliest(events));
        }
        return sorted;
    }

    public void setStartEnd(Event event, Integer[] instanceSchedule) {
        event.setStartTime(LocalDateTime.of(instanceSchedule[0], instanceSchedule[1], instanceSchedule[2],
                instanceSchedule[3], instanceSchedule[4]));
        event.setEndTime(LocalDateTime.of(instanceSchedule[5], instanceSchedule[6], instanceSchedule[7],
                instanceSchedule[8], instanceSchedule[9]));

    }
}
