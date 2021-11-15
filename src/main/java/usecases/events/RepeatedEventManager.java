package usecases.events;

import helpers.ConstantID;
import entities.Event;
import entities.recursions.RecursiveEvent;
import interfaces.DateGetter;
import interfaces.EventListObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Malik Lahlou
 */

public class RepeatedEventManager implements EventListObserver {

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

    public RecursiveEvent addRecursiveEvent(ArrayList<Event> eventsInCycle, DateGetter methodToGetDate){
        RecursiveEvent recursiveEvent = new RecursiveEvent(ConstantID.get(), eventsInCycle, methodToGetDate);
        this.recursiveEventMap.put(recursiveEvent.getId(), recursiveEvent);
        return recursiveEvent;
    }

    public void addRecursiveEvent(RecursiveEvent recursiveEvent){
        this.recursiveEventMap.put(recursiveEvent.getId(), recursiveEvent);
    }


    /**
     * @param id The id of the Recursive event.
     */
    public RecursiveEvent getRecursiveEvent(Integer id){
        return this.recursiveEventMap.get(id);
    }

    /**
     *
     * @param id The id of a Recursive event.
     * @return Given the id of a recursive event object, this methods access the events in one cycle of this repetition
     * and returns a map with the id of the original event in the cycle as keys, and the list of events in the period
     * of repetition as values.
     */

    public HashMap<Integer, ArrayList<Event>> getEventsFromRecursion(Integer id){
        HashMap<Integer, ArrayList<Event>> result = new HashMap<>();
        ArrayList<Event> eventsInOneCycle = this.recursiveEventMap.get(id).getEventsInOneCycle();
        int realSize = eventsInOneCycle.size() - 1;
        for(int eventIndex = 0; eventIndex < realSize; eventIndex++){
            Event event = eventsInOneCycle.get(eventIndex);
            result.put(event.getID(), this.getRecursiveEvent(id).createEventInCycles(event));
        }
        return result;
    }


    @Override
    public void update(String addRemoveChange, ArrayList<Event> changed, EventManager eventManager) {
        // TODO (for phase 2): implement this method which will update a recursion if one of its events is modified
        //  in the EventManager.
    }
}



