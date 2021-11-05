package usecases.EventCollections;

import entities.Event;
import usecases.EventCollections.Strategies.Scheduler;

import java.util.Map;

public class Deadline extends EventCollection {
    public static int nextID;

    @Override
    public void update(String addRemoveChange, Map<Integer, Event> changed) {

    }

    @Override
    protected String getName() {
        return null;
    }

    @Override
    protected void addExcept(Integer[] newRules) {

    }

    @Override
    protected void addAlso(Integer[] newAlso) {

    }

    @Override
    protected Event[] reschedule(Scheduler newScheduler) {
        return new Event[0];
    }

    @Override
    protected void addToCollection(Event event) {

    }

    @Override
    public Integer getID() {
        return null;
    }
    //TODO define rules list for deadline collections. Implement 'update' and other methods

}
