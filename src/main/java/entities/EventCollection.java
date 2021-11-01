package entities;

import helpers.Scheduler;
import interfaces.EventListObserver;

import java.util.Map;

public abstract class EventCollection implements EventListObserver {
    protected Map<String, String> rules;
    protected Integer[] collection;
    protected Scheduler scheduler;
    /**
     * changes the rules of the collection
     * @param newRules Strings in the form "aspect=rule", from list of rules corresponding to this collection
     */
    protected abstract void changeRules(String[] newRules);

    /**
     * based on the rules of the collection, what should the start time of this event be
     * @param count out of the ordered collection, which number event
     * @return start time
     */
    protected abstract Integer[] getStart(Integer count);

    /**
     * based on the rules of the collection, what should the end time of this event be
     * @param count out of the ordered collection, which number event
     * @return end time
     */
    protected abstract Integer[] getEnd(Integer count);

    /**
     *
     * @return the number of events that should be in this collection based on rules
     */
    protected abstract Integer getSize();

    /**
     *
     * @return all the id's of the events in this collection
     */
    protected abstract Integer[] getCollection();

    protected abstract void addToCollection(Integer ID);
}
