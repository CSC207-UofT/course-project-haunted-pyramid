package entities.recursions;

import entities.Event;
import interfaces.DateGetter;
import usecases.events.EventManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Malik Lahlou
 */

public class RecursiveEvent {


    /**
     * + eventsInOneCycle ;for example if a lecture is tuesdays and thurdays of each week, the cycle will consist
     *  of [(tuesday, date+time of lecture), (thursday, date+time of lecture), (tuesday, date+time of lecture after the
     *  thursday one)]. If an event repeats 3 times each 2 week, the list would have 3 elements with the days and
     *  times of the three occurrences of the event.
     *  methodToGetDate: indicates which method the user want to handle repetitions. For now, the user can either input
     *  the number of times a cycle repeats, or the two dates in between the cycle repeats.
     */


    private final UUID id;
    private List<Event> eventsInOneCycle;
    private DateGetter methodToGetDate;

    public RecursiveEvent(UUID id, List<Event> events, DateGetter methodToGetDate){
        this.id = id;
        this.eventsInOneCycle = new ArrayList<>();
        for (Event event : events){
            this.eventsInOneCycle.add(event);
            event.setRecursiveId(id);
        }
        this.methodToGetDate = methodToGetDate;
    }

    public RecursiveEvent(UUID id){
        this.id = id;
        this.eventsInOneCycle = new ArrayList<>();
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
        List<Event> toReturn = methodToGetDate.listOfDatesInCycles(events);
        for(Event event : toReturn){
            event.setRecursiveId(this.id);
        }
        return toReturn;
    }


    /**
     * If user were to Remove/add/change an event from/to a recursion, these methods return cycle in which they will be.
     */


    //TODO: this in helper class
    private LocalDateTime startTimeGetter(Event event){
        if(event.getStartTime() == null){
            return event.getEndTime();
        }
        else{
            return event.getStartTime();
        }
    }


    private List<Event> getSpecificCycle(List<Event> allEvents, Event event){
        int cycleLength = this.getCycleLength();
        int cycleNumber = 1;
        try{
            while(startTimeGetter(event).isAfter(startTimeGetter(allEvents.get(cycleNumber*cycleLength)))){
                cycleNumber++;
            }
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException){
            return allEvents.subList(cycleNumber*cycleLength, allEvents.size());
        }
        return allEvents.subList((cycleNumber-1)*cycleLength, cycleNumber*cycleLength + 1);
    }



    private int getNewEventIndex(List<Event> events, Event eventToAdd, String addChangeRemove){
        int indexOfNewEvent = 0;
        if(addChangeRemove.equals("add")){
            while(startTimeGetter(eventToAdd).isAfter(startTimeGetter(events.get(indexOfNewEvent)))){
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


    private List<Event> addChangeCycle(String addChange, List<Event> events, int index, Event event){
        if (addChange.equals("add")){
            events.add(index, event);
        }
        else{
            events.set(index, event);
        }
        EventManager eventManager = new EventManager(new ArrayList<>());
        return eventManager.timeOrder(events);
    }

    private List<Event> listOfEventsInNewCycles(List<Event> allEvents, List<Event> newCycle) {
        Event lastEvent = allEvents.get(allEvents.size()-1);
        LocalDateTime lastEventEndTime = startTimeGetter(lastEvent);
        LocalDateTime[] intervalDates = new LocalDateTime[2];
        intervalDates[0] = startTimeGetter(newCycle.get(0));
        intervalDates[1] = lastEventEndTime;
        setIntervalDateDateGetter(intervalDates);
        setEventsInOneCycle(newCycle);
        return listOfEventsInCycles(newCycle);
    }


    public List<Event> cycleAfterRemoval(Event eventToRemove){
        List<Event> allEvents = this.listOfEventsInCycles(this.eventsInOneCycle);
        List<Event> newCycle = getSpecificCycle(allEvents, eventToRemove);
        if(eventToRemove.equals(newCycle.get(0))){
            newCycle.remove(eventToRemove);
            newCycle.remove(newCycle.size() - 1);
            newCycle.add(allEvents.get(allEvents.indexOf(eventToRemove) + 1));
        }
        else{
            newCycle.remove(eventToRemove);
        }
        return listOfEventsInNewCycles(allEvents, newCycle);
    }


    public List<Event> cycleAfterAdditionChange(Event eventToAdd, String addChange){
        List<Event> allEvents = this.listOfEventsInCycles(this.eventsInOneCycle);
        List<Event> newCycle = getSpecificCycle(allEvents, eventToAdd);
        int indexOfNewEvent = getNewEventIndex(newCycle, eventToAdd, addChange);
        newCycle = addChangeCycle(addChange, newCycle, indexOfNewEvent, eventToAdd);
        return listOfEventsInNewCycles(allEvents, newCycle);
    }



    /**
     *
     * @param event The specific event.
     * @return Given a specific event in a cycle, this method returns an arrayList of all the events
     * (which are repetitions of this specific event) in the period of repetition (don't include original event
     * in this.eventsInOneCycle).
     */

    public List<Event> createEventInCycles(Event event){
        List<Event> result = new ArrayList<>();
        int indexOfEvent = this.eventsInOneCycle.indexOf(event);
        List<Event> listOfDatesInCycles = this.listOfEventsInCycles(this.eventsInOneCycle);
        int cyclesLength = listOfDatesInCycles.size();
        int i = 1;
        while (indexOfEvent + this.getCycleLength()*i < cyclesLength){
            listOfDatesInCycles.get(indexOfEvent + this.getCycleLength()*i).setRecursiveId(this.id);
            result.add(listOfDatesInCycles.get(indexOfEvent + this.getCycleLength()*i));
            i += 1;
        }
        return result;
    }


}
