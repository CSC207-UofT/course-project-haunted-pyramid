package usecases;

import entities.Event;
import usecases.EventCollections.Recursion;
import usecases.EventCollections.Strategies.SchedulerFactory;

import java.util.HashMap;
import java.util.Map;

public class RecursiveManager {
    public EventManager eventManager;

    public RecursiveManager(EventManager eventManager){
        this.eventManager = eventManager;
    }

    public Recursion createRecursion(Event event, Map<String, String> recurseSpecs, Integer[] end){
        SchedulerFactory schedulerFactory = new SchedulerFactory();
        Integer[] startInitial = eventManager.getStartInt(event);
        Integer[] endInitial = eventManager.getEndInt(event);
        Integer[] initial = new Integer[10];
        for (int i = 0; i < 10; i++){
            if (i<5){
                initial[i] = startInitial[i];
            } else{
                initial[i] = endInitial[i];
            }
        }
        Recursion recursion = new Recursion(this.eventManager.getName(event), initial, end,
                schedulerFactory.getScheduler(recurseSpecs));
        this.eventManager.addObserver(recursion);
        this.eventManager.addToCollection(event, recursion.getID());
        this.schedule(recursion, recurseSpecs, end);
        return recursion;
    }

    public void schedule(Recursion recursion, Map<String, String> recurseSpecs, Integer[] end){
        SchedulerFactory schedulerFactory = new SchedulerFactory();
        Event[] toRemove = recursion.reschedule(schedulerFactory.getScheduler(recurseSpecs));
        for (Integer[] datetime: recursion.toAdd()){
            this.eventManager.addEvent(recursion.getName(), datetime);
        }
    }
}
