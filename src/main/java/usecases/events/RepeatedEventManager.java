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


    /**
     *
     * Given a recursive event id, a list of events in one cycle and a method of repetition, add a recursive event
     * object to the RepeatedEventManager map.
     */

    public void addRecursiveEvent(List<Event> eventsInCycle, DateGetter methodToGetDate){
        RecursiveEvent recursiveEvent = new RecursiveEvent(ConstantID.get(), eventsInCycle, methodToGetDate);
        this.recursiveEventMap.put(recursiveEvent.getId(), recursiveEvent);
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

    public Map<Integer, List<Event>> getEventMapFromRecursion(Integer id){
        Map<Integer, List<Event>> result = new HashMap<>();
        List<Event> eventsInOneCycle = this.recursiveEventMap.get(id).getEventsInOneCycle();
        int realSize = eventsInOneCycle.size() - 1;
        for(int eventIndex = 0; eventIndex < realSize; eventIndex++){
            Event event = eventsInOneCycle.get(eventIndex);
            result.put(event.getID(), this.getRecursiveEvent(id).createEventInCycles(event));
        }
        return result;
    }


    /**
     *
     * @param id The id of a Recursive event.
     * @return Given the id of a recursive event object, this methods access the events in one cycle of this repetition
     * and returns a map with the id of the original event in the cycle as keys, and the list of events in the period
     * of repetition as values.
     */

    public List<Event> getEventsFromRecursion(Integer id){
        List<Event> eventsInOneCycle = this.recursiveEventMap.get(id).getEventsInOneCycle();
        return this.recursiveEventMap.get(id).listOfEventsInCycles(eventsInOneCycle);
    }


    @Override
    public void update(String addRemoveChange, Event changed, EventManager eventManager) {
        // TODO (for phase 2): implement this method which will update a recursion if one of its events is modified
        //  in the EventManager.
    }
}



