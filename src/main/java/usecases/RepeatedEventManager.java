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

    public RepeatedEventManager(List<RecursiveEvent> recursiveEvents){
        this.recursiveEventMap = new HashMap<>();
        for (RecursiveEvent recursiveEvent: recursiveEvents){
            this.recursiveEventMap.put(recursiveEvent.getId(), recursiveEvent);
        }
    }

    public RepeatedEventManager(){
        this.recursiveEventMap = new HashMap<>();
    }

    public Map<Integer, RecursiveEvent> getRecursiveEventMap() {return recursiveEventMap;}
    public void setRecursiveEventMap(Map<Integer, RecursiveEvent> recursiveEventMap) {
        this.recursiveEventMap = recursiveEventMap;}

    public void addRecursiveEvent(Integer ID, ArrayList<Event> eventsInCycle, DateGetter methodToGetDate){
        RecursiveEvent recursiveEvent = new RecursiveEvent(ID, eventsInCycle, methodToGetDate);
        this.recursiveEventMap.put(recursiveEvent.getId(), recursiveEvent);
    }

    public RecursiveEvent getRecursiveEvent(Integer id){
        return this.recursiveEventMap.get(id);
    }

//    public HashMap<String, ArrayList<Event>> getEventsFromRecursion(Integer id){
//        HashMap<String, ArrayList<Event>> result = new HashMap<>();
//        for(Event event : this.recursiveEventMap.get(id).getEventsInOneCycle()){
//            result.put(event.getName(), this.getRecursiveEvent(id).createEventInCycles(event));
//        }
//        return result;
//    }


    public HashMap<String, ArrayList<Event>> getEventsFromRecursion(Integer id){
        HashMap<String, ArrayList<Event>> result = new HashMap<>();
        for(Event event : this.recursiveEventMap.get(id).getEventsInOneCycle()){
            result.put(event.getName(), this.getRecursiveEvent(id).createEventInCycles(event));
        }
        return result;
    }


}

