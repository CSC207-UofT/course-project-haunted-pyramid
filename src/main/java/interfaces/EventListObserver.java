package interfaces;

import entities.Event;
import usecases.events.EventManager;

public interface EventListObserver {
    /**
     * @param addRemoveChange what to do with the events in the map, 'add', 'remove' or 'change'
     *      * where 'change' indicates a change of date
     * @param changed map of days to events, where days are the
     *      * Integer days they were originally mapped to before modification
     * @param eventManager the eventManager that was updated
     */
    void update(String addRemoveChange, Event changed, EventManager eventManager);
}
