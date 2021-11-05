package usecases.EventCollections;

import entities.Event;
import usecases.EventCollections.Strategies.Scheduler;
import interfaces.EventListObserver;

import java.util.Map;

public abstract class EventCollection implements EventListObserver {
    protected Integer[][] except;
    protected Integer[][] also;
    protected Scheduler scheduler;
    protected Map<Integer, Event> collection;
    protected Integer[] initial;
    protected String name;
    protected Integer ID;
    protected abstract String getName();

    protected abstract void addExcept(Integer[] newRules);
    protected abstract void addAlso(Integer[] newAlso);
    protected abstract Event[] reschedule(Scheduler newScheduler);
    protected abstract void addToCollection(Event event);
    public abstract Integer getID();
}
