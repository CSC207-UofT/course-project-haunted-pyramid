package usecases;

import entities.Event;
import entities.RecursiveEvent;
import interfaces.DateGetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepeatedEventManager {

    private Map<Integer, RecursiveEvent> recursiveEventMap;

    /**
     *
     * Initialize an RepeatedEventManager given a list of recursive events.
     */

    public RepeatedEventManager(List<RecursiveEvent> recursiveEvents){
        this.recursiveEventMap = new HashMap<>();
        for (RecursiveEvent recursiveEvent: recursiveEvents){
            this.recursiveEventMap.put(recursiveEvent.getId(), recursiveEvent);
        }
    }

    /**
     *
     * Initialize an empty RepeatedEventManager.
     */

    public RepeatedEventManager(){
        this.recursiveEventMap = new HashMap<>();
    }

    /**
     *
     * Getter and Setter methods.
     */

    public Map<Integer, RecursiveEvent> getRecursiveEventMap() {return recursiveEventMap;}
    public void setRecursiveEventMap(Map<Integer, RecursiveEvent> recursiveEventMap) {
        this.recursiveEventMap = recursiveEventMap;}

    /**
     *
     * Given a recursive event id, a list of events in one cycle and a method of repetition, add a recursive event
     * object to the RepeatedEventManager map.
     */

    public void addRecursiveEvent(Integer ID, ArrayList<Event> eventsInCycle, DateGetter methodToGetDate){
        RecursiveEvent recursiveEvent = new RecursiveEvent(ID, eventsInCycle, methodToGetDate);
        this.recursiveEventMap.put(recursiveEvent.getId(), recursiveEvent);
    }

    public void addRecursiveEvent(RecursiveEvent recursiveEvent){
        this.recursiveEventMap.put(recursiveEvent.getId(), recursiveEvent);
    }

    /**
     *
     * Returns the recursive event with the id.
     */

    public RecursiveEvent getRecursiveEvent(Integer id){
        return this.recursiveEventMap.get(id);
    }


    /**
     *
     * Given the id of a recursive event object, this methods access the events in one cycle of this repetition
     * and returns a map with the names of the events in the cycle as keys, and the list of events in the period
     * of repetition as values.
     */

    public HashMap<String, ArrayList<Event>> getEventsFromRecursion(Integer id){
        HashMap<String, ArrayList<Event>> result = new HashMap<>();
        int i = 0;
        for(Event event : this.recursiveEventMap.get(id).getEventsInOneCycle()){
            result.put(event.getName(), this.getRecursiveEvent(id).createEventInCycles(event));
            i ++;
        }
        return result;
    }


}

