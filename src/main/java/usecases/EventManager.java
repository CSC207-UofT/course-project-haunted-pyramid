package usecases;

import java.util.*;
import java.time.LocalDate;

import entities.Event;
import entities.Test;

public class EventManager{
    private final Map<Integer, Event> eventMap;
    // STYLE ERROR BECAUSE THE CLASSES IMPLEMENTING THE INTERFACES ARE NOT IMPLEMENTED YET
    /**
     * constructor for event manager
     * @param events a list of the current users events
     */
    public EventManager(List<Event> events){
        this.eventMap = new HashMap<>();

        for (Event event: events) {
            this.eventMap.put(event.getID(), event);
        }
    }

    /**
     * empty EventManager
     *
     */
    public EventManager(){
        this.eventMap = new HashMap<>();
    }

    /**
     * getDay returns a map of the events in a day
     * @param day the day that is being searched for
     * @return all events in this day
     */
    public Map<String, Event> getDay(LocalDate day){
        Map<String, Event> dayMap = new HashMap<>();
        for (Event event: eventMap.values()){
            if (event.getDay().isEqual(day)) {
                dayMap.put(event.getName(), event);
            }
        }
        return dayMap;
    }

    /**
     *
     * @param ID the id of an existing event
     * @return the event of this name
     */
    public <T extends Event> T getEvent(int ID){
        return (T) eventMap.get(ID);
    }

    public ArrayList<Event> getAll(){
        return new ArrayList<Event>(this.eventMap.values());
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

    /**
     * removes an event from the list by ID
     * @param ID the id to be removed
     * @return the event just removed
     */
    public Event removeEvent(int ID){
        return eventMap.remove(ID);
    }
    public <T extends Event> void addEvent(T event){
        eventMap.put(event.getID(), event);
    }

    public String getName(Event event){return event.getName();}
    public String getStart(Event event) {return event.getStartString();}
    public String getEnd(Event event) {return event.getEndString();}

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
    public ArrayList<Event> timeOrder(ArrayList<Event> events){
        ArrayList<Event> sorted = new ArrayList<>();
        while (!events.isEmpty()){
            sorted.add(earliest(events));
            events.remove(earliest(events));
        }
        return sorted;
    }

    public void newTest(Integer id, String title, Integer year, Integer month, Integer day,
                        Integer startHour, Integer endHour, Integer startMin, Integer endMin) {
        Test test = new Test(id, title, year, month, day, startHour, endHour, startMin, endMin);
        this.eventMap.put(test.getID(), test);
    }

    public void newAssignment(Integer id, String title, String course, Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4) {
    }

    public void newLecture(Integer id, String title, String course, Integer integer, Integer integer1, Integer integer2, Integer integer3, Integer integer4, Integer integer5, Integer integer6) {
    }

    //TODO replace entities.Event with subclass of entities.Event for free slots - implements repeatable -
    // list of events under one name
    //TODO finish implementation for day instead of frame
//    public ArrayList<Event> freeSlots(LocalDate day){
//        ArrayList<Event> events = new ArrayList<Event>((this.getDay(day).values()));
//        events = this.timeOrder(events);
//        ArrayList<Event> freeSlots = new ArrayList<Event>();
//        if (LocalTime.of(0, 0).isBefore(LocalTime.of(events.get(0).getStartTime().getHour(),
//                events.get(0).getStartTime().getMinute()))){
//            freeSlots.add(new Event(1, "before " + events.get(0).getName(),
//                    LocalDateTime.of(day, LocalTime.of(0, 0)),
//                    events.get(0).getStartTime()));
//        }
//        for (int i = 0; i < events.size()-1; i++){
//            if (events.get(i).getEndTime().isBefore(events.get(i+1).getStartTime())){
//                freeSlots.add(new Event(1, "before " + events.get(i+1).getName(), events.get(i).getEndTime(),
//                        events.get(i+1).getStartTime()));
//            }
//        }
//        return freeSlots;
//    }


}
