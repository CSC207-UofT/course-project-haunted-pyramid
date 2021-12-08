package entities.recursions;

import entities.Event;
import entities.User;
import helpers.EventHelper;
import interfaces.DateGetter;
import usecases.events.EventManager;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Malik Lahlou
 */

public class RecursiveEvent implements Serializable {


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
    private EventHelper eventHelper = new EventHelper();

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
        List<Event> toReturn = methodToGetDate.listOfDatesInCycles(events);
        for(Event event : toReturn){
            event.setRecursiveId(this.id);
        }
        return toReturn;
    }

    public Event getLastEvent(){
        return this.eventsInOneCycle.get(eventsInOneCycle.size()-1);
    }


    /**
     * If user were to Remove/add/change an event from/to a recursion, these methods return cycle in which they will be.
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


    private List<Event> addChangeCycle(String addChange, List<Event> events, int index, Event event){
        if (addChange.equals("add")){
            events.add(index, event);
        }
        else{
            events.set(index, event);
        }
        return eventHelper.timeOrder(events);
    }

    private List<Event> listOfEventsInNewCycles(List<Event> allEvents, List<Event> newCycle) {
        Event lastEvent = allEvents.get(allEvents.size()-1);
        LocalDateTime lastEventEndTime = eventHelper.startTimeGetter(lastEvent);
        LocalDateTime[] intervalDates = new LocalDateTime[2];
        intervalDates[0] = eventHelper.startTimeGetter(newCycle.get(0));
        intervalDates[1] = lastEventEndTime.plus(Duration.ofDays(1));
        setIntervalDateDateGetter(intervalDates);
        setEventsInOneCycle(newCycle);
        return listOfEventsInCycles(newCycle);
    }


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
        // List<Event> result = listOfEventsInNewCycles(allEvents, newCycle);
        // newCycle.addAll(result);
        return result;
    }


    public List<List<Event>> cycleAfterAdditionChange(Event eventToAdd, String addChange, List<Event> allEvents){
        List<Event> newCycle = getSpecificCycle(allEvents, eventToAdd);
        int indexOfNewEvent = getNewEventIndex(newCycle, eventToAdd, addChange);
        newCycle = addChangeCycle(addChange, newCycle, indexOfNewEvent, eventToAdd);
        List<List<Event>> result = new ArrayList<>();
        result.add(allEvents);
        result.add(newCycle);
        // List<Event> result = listOfEventsInNewCycles(allEvents, newCycle);
        // newCycle.addAll(result);
        return result;
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



    public static void main(String[] args) throws IOException, ClassNotFoundException {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        LocalDateTime l =  LocalDateTime.of(2021, 11, 26, 11, 0);
        Event e1 = new Event(id1, "e1", l);
        Event e2 = new Event(id2, "e2", 2021, 11, 20, 10, 11, 0, 0);
        Event e3 = new Event(id3, "e3", 2021, 11, 22, 10, 11, 0, 0);
        Event e4 = new Event(id3, "e4", 2021, 11, 25, 10, 11, 0, 0);
        ArrayList<Event> events = new ArrayList<>();
        events.add(e2);
        events.add(e3);
        events.add(e4);
        events.add(e1);
        NumberOfRepetitionInput x = new NumberOfRepetitionInput(4);
        RecursiveEvent recursiveEvent = new RecursiveEvent(UUID.randomUUID(), events, x);
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, "malik", "lahlou", "pass");

        Map<UUID, Map<UUID, RecursiveEvent>> mine = new HashMap<>();
        Map<UUID, RecursiveEvent> mine2 = new HashMap<>();
        mine2.put(recursiveEvent.getId(),recursiveEvent);
        mine.put(user.getId(), mine2);

        List<Object> truc = new ArrayList<>();
        truc.add("elnfiuerf");
        truc.add(2);
        truc.add(e1);
        System.out.println(truc);

    }
}
