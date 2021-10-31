package usecases;

import java.time.LocalTime;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import entities.Event;
import interfaces.AutoSchedule;
import interfaces.Repeated;
import interfaces.Fluid;

public class EventManager{
    private final Map<String, Event> eventMap;
    /**
     * constructor for event manager
     * @param events a list of the current users events
     */
    public EventManager(List<Event> events){
        this.eventMap = new HashMap<>();
        this.occurrenceLists = new HashMap<>();
        this.fluidSessions = new HashMap<>();

        for (Event event: events){
            this.eventMap.put(event.getName(), event);
            if (event instanceof Repeated){ // NOT IMPLEMENTED YET
                this.occurrenceLists.put(event.getName(), ((Repeated) event).occurrences());
            }
            if (event instanceof Fluid){ // NOT IMPLEMENTED YET
                this.fluidSessions.put(event.getName(), ((Fluid) event).getFluidSessions());
            }
            if (event instanceof AutoSchedule){ // NOT IMPLEMENTED YET
                ArrayList<AutoSchedule> event1 = new ArrayList<>();
                event1.add((AutoSchedule) event);
                this.fluidSessions.put(event.getName(), event1);
            }

        }
    }

    /**
     * empty EventManager
     */
    public EventManager(){
        this.eventMap = new HashMap<>();
        this.occurrenceLists = new HashMap<>();
        this.fluidSessions = new HashMap<>();
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
     * @param name the name of an existing event
     * @return the event of this name
     */
    public Event getEvent(String name){
        return eventMap.get(name);
    }

    /**
     * removes an event from the set
     * @param name the name to be removed
     * @return the event just removed
     */
    public Event removeEvent(String name){
        return eventMap.remove(name);
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
    public void addEvent(String type, String name, int year, int month, int day, int startHour, int startMin, int endHour,
                         int endMin){
        //TODO add different types of Events (assignment, test, etc)
        //TODO make ID to be more flexible
        Event event = new Event(1, name, year, month, day, startHour, endHour, startMin, endMin);
        this.eventMap.put(event.getName(), event);
        /*
        if (event instanceof Repeated){
            this.occurenceLists.put(event.getName(), ((Repeated) event).occurrences());
        }
        if (event instanceof Fluid){
            this.fluidSessions.put(event.getName(), ((Fluid) event).getFluidSessions());
        }
        if (event instanceof AutoSchedule){
            ArrayList<AutoSchedule> event1 = new ArrayList<>();
            event1.add((AutoSchedule) event);
            this.fluidSessions.put(event.getName(), event1);
        }
        */
    }


    public String getName(Event event){
        return event.getName();
    }
    public String getStart(Event event) {return event.getStartString();}
    public String getStartTime(Event event){
        String[] date = event.getStartString().split("-");
        return date[2].substring(3, 8);
    }
    public String getEndTime(Event event){
        String[] date = event.getEndString().split("-");
        return date[2].substring(3, 8);
    }
    public String getEnd(Event event) {return event.getEndString();}
    public String getAllNames(){
        StringBuilder list = new StringBuilder();
        for (String name: eventMap.keySet()){
            list.append(name);
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
