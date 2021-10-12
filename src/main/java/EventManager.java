import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventManager {
    private Map<String, Event> eventMap;
    private Map<String, ArrayList<AutoSchedule>> fluidSessions;
    private Map<String, ArrayList<Repeated>> occurenceLists;
    /**
     * constructor for event manager
     * @param events a list of the current users events
     */
    public EventManager(List<Event> events){
        this.eventMap = new HashMap<String, Event>();
        this.occurenceLists = new HashMap<String, ArrayList<Repeated>>();
        this.fluidSessions = new HashMap<String, ArrayList<AutoSchedule>>();

        for (Event event: events){
            this.eventMap.put(event.getName(), event);
            if (event instanceof Repeated){
                this.occurenceLists.put(event.getName(), ((Repeated) event).occurrences());
            }
            if (event instanceof Fluid){
                this.fluidSessions.put(event.getName(), ((Fluid) event).getFluidSessions());
            }
            if (event instanceof AutoSchedule){
                ArrayList<AutoSchedule> event1 = new ArrayList<AutoSchedule>();
                event1.add((AutoSchedule) event);
                this.fluidSessions.put(event.getName(), event1);
            }

        }
    }

    /**
     * getDay returns a map of the events in a day
     * @param day the day that is being searched for
     * @return all events in this day
     */
    public Map<String, Event> getDay(LocalDate day){
        Map<String, Event> dayMap = new HashMap<String, Event>();
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

    public Event removeEvent(String name){
        return eventMap.remove(name);
    }

    public void addEvent(Event event){
        eventMap.put(event.getName(),event);
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
    public ArrayList<Event> timeOrder(ArrayList<Event> events){
        ArrayList<Event> sorted = new ArrayList<Event>();
        while (!events.isEmpty()){
            sorted.add(earliest(events));
            events.remove(earliest(events));
        }
        return sorted;
    }

    //TODO replace Event with subclass of Event for free slots - implements repeatable - list of events under one name
    public ArrayList<Event> freeSlots(LocalDateTime start, LocalDateTime end, ArrayList<Event> events){
        events = timeOrder(events);
        ArrayList<Event> freeSlots = new ArrayList<Event>();
        if (start.isBefore(events.get(0).getStartTime())){
            freeSlots.add(new Event(1, "before " + events.get(0).getName(), start,
                    events.get(0).getStartTime()));
        }
        for (int i = 0; i < events.size()-1; i++){
            if (events.get(i).getEndTime().isBefore(events.get(i+1).getStartTime())){
                freeSlots.add(new Event(1, "before " + events.get(i+1).getName(), events.get(i).getEndTime(),
                        events.get(i+1).getStartTime()));
            }
        }
        return freeSlots;
    }


}
