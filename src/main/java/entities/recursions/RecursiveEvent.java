package entities.recursions;

import entities.Event;
import helpers.EventHelper;
import interfaces.DateGetter;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Malik Lahlou
 */

public class RecursiveEvent implements Serializable {

    private final UUID id;
    private List<Event> eventsInOneCycle;
    private DateGetter methodToGetDate;
    private EventHelper eventHelper = new EventHelper();

    /**
     * Constructor for recursive event.
     *
     * @param id uuid of the recursive event.
     * @param events the events in one cycle.
     * @param methodToGetDate the repetition pattern.
     */
    public RecursiveEvent(UUID id, List<Event> events, DateGetter methodToGetDate){
        this.id = id;
        this.eventsInOneCycle = new ArrayList<>();
        for (Event event : events){
            this.eventsInOneCycle.add(event);
            event.setRecursiveId(id);
        }
        this.methodToGetDate = methodToGetDate;
    }

    /**
     * Constructor for recursive event.
     *
     * @param id uuid of the recursive event.
     */
    public RecursiveEvent(UUID id){
        this.id = id;
        this.eventsInOneCycle = new ArrayList<>();
    }

    /**
     * Constructor for recursive event.
     *
     * @param uuid uuid of the recursive event.
     * @param events the events in one cycle.
     *
     */
    public RecursiveEvent(UUID uuid, List<Event> events){
        this.id = uuid;
        for (Event event : events){
            event.setRecursiveId(id);
        }
        this.eventsInOneCycle = events;
    }

    /**
     * Getter methods.
     */

    public UUID getId() {return id;}
    public List<Event> getEventsInOneCycle() {return eventsInOneCycle;}
    public int getCycleLength(){
        return eventsInOneCycle.size() -1;
    }

    /**
     * Setter methods.
     */
    public void setEventsInOneCycle(List<Event> eventsInOneCycle) {this.eventsInOneCycle = eventsInOneCycle;}
    public void addEventToCycle(Event event){this.eventsInOneCycle.add(event);}
    public void setMethodToGetDate(DateGetter methodToGetDate) {this.methodToGetDate = methodToGetDate;}
    public void setNumberOfRepetitionDateGetter(int numberOfRepetition){
        this.methodToGetDate = new NumberOfRepetitionInput(numberOfRepetition);
    }
    public void setIntervalDateDateGetter(LocalDateTime[] periodOfRepetition){
        this.methodToGetDate = new IntervalDateInput(periodOfRepetition[0], periodOfRepetition[1]);
    }



    /**
     *
     * Uses the classes that implement the date getter interface to return the dates of all the events in the
     * period of repetition.
     * @param events The events in one repetition cycle.
     * @return list of repetitions of event in events
     */

    public List<Event> listOfEventsInCycles(List<Event> events){
        List<Event> toReturn = methodToGetDate.listOfEventsInTheCycles(events);
        for(Event event : toReturn){
            event.setRecursiveId(this.id);
        }
        return toReturn;
    }

    /**
     *
     * Helper method to return the last event in the cycle of the recursion.
     * @return list of repetitions of event in events
     */
    public Event getLastEvent(){
        return this.eventsInOneCycle.get(eventsInOneCycle.size()-1);
    }


    /**
     * Returns the cycle which contain or should contain the event to modify or add based on its start/end date.
     *
     * @param allEvents all the events in this recursion.
     * @param event the event to add or modify
     * @return the list of events in the cycle of interest.
     */
    private List<Event> getSpecificCycle(List<Event> allEvents, Event event){
        int cycleLength = this.getCycleLength();
        int cycleNumber = 1;
        try{
            while(eventHelper.startTimeGetter(event).isAfter(eventHelper.startTimeGetter(allEvents.get(cycleNumber*cycleLength))) ||
                    eventHelper.startTimeGetter(event).isEqual(eventHelper.startTimeGetter(allEvents.get(cycleNumber*cycleLength)))){
                cycleNumber++;
            }
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException){
            return new ArrayList<>(allEvents.subList(cycleNumber*cycleLength, allEvents.size()));
        }
        return new ArrayList<>(allEvents.subList((cycleNumber-1)*cycleLength, cycleNumber*cycleLength + 1));
    }


    /**
     * Returns the index of the event to add or modify in the specific cycle returned above.
     *
     * @param events all the events in this recursion.
     * @param eventToAdd the event to add or modify
     * @param addChangeRemove String describing whether to add or the eventToAdd.
     * @return the index of interest.
     */
    private int getNewEventIndex(List<Event> events, Event eventToAdd, String addChangeRemove){
        int indexOfNewEvent = 0;
        if(addChangeRemove.equals("add")){
            while(eventHelper.startTimeGetter(eventToAdd).isAfter(eventHelper.startTimeGetter(events.get(indexOfNewEvent)))){
                indexOfNewEvent++;
            }
        }
        else{
            while(eventToAdd.getID() != events.get(indexOfNewEvent).getID()){
                indexOfNewEvent++;
            }
        }
        return indexOfNewEvent;
    }

    /**
     * Returns the new cycle after adding or modifying the event of interest.
     *
     * @param addChange String describing whether to add or the eventToAdd.
     * @param events   all the events in this recursion.
     * @param event     the event to add or modify
     * @param index the index of the event to add or modify in the specific cycle returned above.
     * @return the list of events in the cycle of interest.
     */
    private List<Event> addChangeCycle(String addChange, List<Event> events, int index, Event event){
        if (addChange.equals("add")){
            events.add(index, event);
        }
        else{
            events.set(index, event);
        }
        return eventHelper.timeOrder(events);
    }


    /**
     * Returns allEvents and the new cycle after the removal which will be the base of future recursions.
     *
     * @param eventToRemove the event to remove from the recursion.
     * @param allEvents all the events in this recursion.
     * @return a list consisting of a list of all the events in the recursion and the events in the new cycle after
     * the removal of eventToRemove.
     */
    public List<List<Event>> cycleAfterRemoval(Event eventToRemove, List<Event> allEvents){
        List<Event> newCycle = getSpecificCycle(allEvents, eventToRemove);
        if(eventToRemove.equals(newCycle.get(0))){
            int newCycleLength = newCycle.size() - 1;
            newCycle.remove(eventToRemove);
            newCycle.remove(newCycleLength - 1);
            newCycle.add(allEvents.get(allEvents.indexOf(eventToRemove) + newCycleLength + 1));
        }
        else{
            newCycle.remove(eventToRemove);
        }
        List<List<Event>> result = new ArrayList<>();
        result.add(allEvents);
        result.add(newCycle);
        return result;
    }

    /**
     * Returns allEvents and the new cycle, which will be the base of future recursions, after the addition or change.
     *
     * @param eventToAdd the event to add or modify
     * @param addChange      String describing whether to add or the eventToAdd.
     * @param allEvents all the events in this recursion.
     * @return a list consisting of a list of all the events in the recursion and the events in the new cycle after
     * the addition or change of eventToAdd.
     */
    public List<List<Event>> cycleAfterAdditionChange(Event eventToAdd, String addChange, List<Event> allEvents){
        List<Event> newCycle = getSpecificCycle(allEvents, eventToAdd);
        int indexOfNewEvent = getNewEventIndex(newCycle, eventToAdd, addChange);
        newCycle = addChangeCycle(addChange, newCycle, indexOfNewEvent, eventToAdd);
        List<List<Event>> result = new ArrayList<>();
        result.add(allEvents);
        result.add(newCycle);
        return result;
    }
}
