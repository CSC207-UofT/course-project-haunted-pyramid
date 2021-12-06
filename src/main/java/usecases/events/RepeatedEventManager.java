package usecases.events;

import entities.Event;
import entities.recursions.RecursiveEvent;
import interfaces.EventListObserver;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Malik Lahlou
 */

public class RepeatedEventManager implements EventListObserver {
    /**
     *
     * Integer: id of RecursiveEvent object
     * LocalDateTime: the date of the beginning of the list of events List<Event>.
     */


    private Map<UUID, RecursiveEvent> recursiveEventMap;
    private Map<UUID, Map<LocalDateTime, List<Event>>> recursiveIdToDateToEventsMap;


    /**
     *
     * Initialize an empty RepeatedEventManager.
     */

    public RepeatedEventManager(){
        this.recursiveIdToDateToEventsMap = new HashMap<>();
        this.recursiveEventMap = new HashMap<>();
    }

    public RepeatedEventManager(Map<UUID, RecursiveEvent> recursiveIdToRecursiveEvent){
        this.recursiveIdToDateToEventsMap = new HashMap<>();
        this.recursiveEventMap = recursiveIdToRecursiveEvent;
        for(UUID uuid : recursiveIdToRecursiveEvent.keySet()){
            RecursiveEvent recursiveEvent = recursiveIdToRecursiveEvent.get(uuid);
            int cycleLength = recursiveEvent.getCycleLength();
            List<Event> allEvents = recursiveEvent.listOfEventsInCycles(recursiveEvent.getEventsInOneCycle());
            recursiveIdToDateToEventsMap.put(uuid, eventListToMap(allEvents, cycleLength));
        }

    }

    /**
     *
     * Getter and Setter methods.
     */

    public Map<UUID, Map<LocalDateTime, List<Event>>> getRecursiveIdToDateToEventsMap() {
        return recursiveIdToDateToEventsMap;
    }
    public Map<UUID, RecursiveEvent> getRecursiveEventMap() {return recursiveEventMap;}

    public void addRecursion(RecursiveEvent recursiveEvent){
        this.recursiveEventMap.put(recursiveEvent.getId(), recursiveEvent);
        List<Event> events = recursiveEvent.listOfEventsInCycles(recursiveEvent.getEventsInOneCycle());
        this.recursiveIdToDateToEventsMap.put(recursiveEvent.getId(), eventListToMap(events, recursiveEvent.getCycleLength()));
    }

    public Event getThisEventFromRecursion(UUID uuid){
        for(UUID uuid1 : this.recursiveIdToDateToEventsMap.keySet()){
            for(List<Event> events : this.recursiveIdToDateToEventsMap.get(uuid1).values()){
                for(Event event : events){
                    if(event.getID() == uuid){
                        return event;
                    }
                }
            }
        }
        return null;
    }






        /**
     *
     * Given a recursive event id, a list of events in one cycle and a method of repetition, add a recursive event
     * object to the RepeatedEventManager map.
     */

    public void addRecursiveEvent(RecursiveEvent recursiveEvent){
        this.recursiveEventMap.put(recursiveEvent.getId(), recursiveEvent);
    }


    /**
     * @param id The id of the Recursive event.
     */
    public RecursiveEvent getRecursiveEvent(UUID id){
        return this.recursiveEventMap.get(id);
    }


    /**
     *
     * @param id The id of a Recursive event.
     * @return Given the id of a recursive event object, this methods access the events in one cycle of this repetition
     * and returns a map with the id of the original event in the cycle as keys, and the list of events in the period
     * of repetition as values.
     */

    public Map<UUID, List<Event>> getEventMapFromRecursion(UUID id){
        Map<UUID, List<Event>> result = new HashMap<>();
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

    public List<Event> getEventsFromRecursion(UUID id){
        List<Event> eventsInOneCycle = this.recursiveEventMap.get(id).getEventsInOneCycle();
        return this.recursiveEventMap.get(id).listOfEventsInCycles(eventsInOneCycle);
    }


    private LocalDateTime startTimeGetter(Event event){
        if(event.getStartTime() == null){
            return event.getEndTime();
        }
        else{
            return event.getStartTime();
        }
    }

    public Map<LocalDateTime, List<Event>> eventListToMap(List<Event> events, int cycleLength){
        Map<LocalDateTime, List<Event>> datesAndEvents = new HashMap<>();
        try{
        int endLoop = events.size();
        int i = 1;
        while(cycleLength*i < endLoop){
            datesAndEvents.put(startTimeGetter(events.get(cycleLength*(i-1))),
                    events.subList(cycleLength*(i-1), cycleLength*i));
            i++;
        }
        datesAndEvents.put(startTimeGetter(events.get(cycleLength*(i-1))), events.subList(cycleLength*(i-1), endLoop));
        return datesAndEvents;
        }
        catch (IndexOutOfBoundsException e){
            return datesAndEvents;
        }
    }


    public void addEventsFromRecursiveEvent(RecursiveEvent recursiveEvent){
        int cycleLength = recursiveEvent.getCycleLength();
        List<Event> allEventsInCycles = recursiveEvent.listOfEventsInCycles(recursiveEvent.getEventsInOneCycle());
        Map<LocalDateTime, List<Event>> datesAndEvents = eventListToMap(allEventsInCycles, cycleLength);
        UUID myID = recursiveEvent.getId();
        this.recursiveIdToDateToEventsMap.put(myID, datesAndEvents);
    }

    //TODO: maybe there is a pattern that does this and avoid the repetitions in code.

    private RecursiveEvent recursiveEventConstructor(List<Event> eventsInCycle, int numberOfRepetition){
        RecursiveEvent recursiveEvent = new RecursiveEvent(UUID.randomUUID());
        for (Event event : eventsInCycle){
            event.setRecursiveId(recursiveEvent.getId());
        }
        recursiveEvent.setEventsInOneCycle(eventsInCycle);
        recursiveEvent.setNumberOfRepetitionDateGetter(numberOfRepetition);
        return recursiveEvent;
    }

    private RecursiveEvent recursiveEventConstructor(List<Event> eventsInCycle, LocalDateTime[] periodOfRepetition){
        RecursiveEvent recursiveEvent = new RecursiveEvent(UUID.randomUUID());
        for (Event event : eventsInCycle){
            event.setRecursiveId(recursiveEvent.getId());
        }
        recursiveEvent.setIntervalDateDateGetter(periodOfRepetition);
        recursiveEvent.setEventsInOneCycle(eventsInCycle);
        return recursiveEvent;
    }


    public UUID recursiveEventConstructor1(List<Event> events){
        UUID uuid = UUID.randomUUID();
        RecursiveEvent recursiveEvent = new RecursiveEvent(uuid, events);
        this.recursiveEventMap.put(uuid, recursiveEvent);
        return uuid;
    }

    public void addEventsFromRecursiveEvent(List<Event> eventsInCycle, int numberOfRepetition){
        RecursiveEvent recursiveEvent = recursiveEventConstructor(eventsInCycle, numberOfRepetition);
        this.recursiveEventMap.put(recursiveEvent.getId(), recursiveEvent);
        this.addEventsFromRecursiveEvent(recursiveEvent);
    }

    public UUID addEventsFromRecursiveEvent(List<Event> eventsInCycle, LocalDateTime[] periodOfRepetition){
        RecursiveEvent recursiveEvent = recursiveEventConstructor(eventsInCycle, periodOfRepetition);
        this.recursiveEventMap.put(recursiveEvent.getId(), recursiveEvent);
        this.addEventsFromRecursiveEvent(recursiveEvent);
        return recursiveEvent.getId();
    }

    public List<Event> getAllEventsFromRecursiveEvent(UUID id){
        List<Event> result = new ArrayList<>();
        Map<LocalDateTime, List<Event>> dateEventMap = this.recursiveIdToDateToEventsMap.get(id);
        for(List<Event> events : dateEventMap.values()){
            result.addAll(events);
        }
        EventManager em = new EventManager(new ArrayList<>());
        return em.timeOrder(result);
    }

    private UUID newRecursiveEventForUpdate(List<Event> allEvents, List<Event> newCycle) {
        Event lastEvent = allEvents.get(allEvents.size()-1);
        LocalDateTime lastEventEndTime = startTimeGetter(lastEvent);
        LocalDateTime[] intervalDates = new LocalDateTime[2];
        intervalDates[0] = startTimeGetter(newCycle.get(0));
        intervalDates[1] = lastEventEndTime.plus(Duration.ofDays(1));
        return addEventsFromRecursiveEvent(newCycle, intervalDates);
    }

    private List<Event> newOneEventCycle(Event changed){
        UUID id = changed.getRecursiveId();
        Map<LocalDateTime, List<Event>> thisRecursionDates = this.recursiveIdToDateToEventsMap.get(id);
        List<LocalDateTime> results = new ArrayList<>();
        for(LocalDateTime localDateTime : thisRecursionDates.keySet()){
            if(startTimeGetter(changed).isAfter(localDateTime) || startTimeGetter(changed).isEqual(localDateTime)){
                results.add(localDateTime);
            }
        }
        LocalDateTime result = results.get(0);
        for(LocalDateTime localDateTime : results){
            if(localDateTime.isAfter(result)){
                result = localDateTime;
            }
        }
        return thisRecursionDates.get(result);
    }

    @Override
    public void update(String addRemoveChange, Event changed, EventManager eventManager) {
        UUID id = changed.getRecursiveId();
        List<Event> allEvents = new ArrayList<>(getAllEventsFromRecursiveEvent(id));
        List<Event> newCycles;
        List<List<Event>> inputForNewRecursion;
        UUID uuid;
        if (addRemoveChange.equalsIgnoreCase("Remove") &&
                this.recursiveEventMap.get(id).getCycleLength() > 1) {
            inputForNewRecursion = this.recursiveEventMap.get(id).cycleAfterRemoval(changed, allEvents);
            newCycles = inputForNewRecursion.get(1);
            uuid = newRecursiveEventForUpdate(inputForNewRecursion.get(0), newCycles);
        }
        else if (addRemoveChange.equalsIgnoreCase("add")) {
            inputForNewRecursion = this.recursiveEventMap.get(id).cycleAfterAdditionChange(changed, "add", allEvents);
            newCycles = inputForNewRecursion.get(1);
            uuid = newRecursiveEventForUpdate(inputForNewRecursion.get(0), newCycles);
        }
        else if (addRemoveChange.equalsIgnoreCase("change")){
            inputForNewRecursion = this.recursiveEventMap.get(id).cycleAfterAdditionChange(changed, "Change", allEvents);
            newCycles = inputForNewRecursion.get(1);
            uuid = newRecursiveEventForUpdate(inputForNewRecursion.get(0), newCycles);
        }
        else{
            newCycles = newOneEventCycle(changed);
            uuid = id;
        }
        this.recursiveIdToDateToEventsMap.get(uuid).put(startTimeGetter(newCycles.get(0)), newCycles.subList(0,newCycles.size() - 1));
        newRecursionCreator(changed, id, newCycles);
    }



    private void newRecursionCreator(Event changed, UUID id, List<Event> newCycles) {
        LocalDateTime firstTime = startTimeGetter(newCycles.get(0));
        Set<LocalDateTime> keySet = new HashSet<>(this.recursiveIdToDateToEventsMap.get(id).keySet());
        this.recursiveIdToDateToEventsMap.get(id).remove(startTimeGetter(changed));
        this.recursiveIdToDateToEventsMap.get(id).remove(startTimeGetter(newCycles.get(0)));
        for(LocalDateTime localDateTime : keySet){
            if(localDateTime.isAfter(firstTime) || localDateTime.isEqual(firstTime)){
                this.recursiveIdToDateToEventsMap.get(id).remove(localDateTime);
            }
        }
        // LocalDateTime lastEventEndTime = startTimeGetter(changed);
        LocalDateTime[] intervalDates = new LocalDateTime[2];
        intervalDates[0] = startTimeGetter(this.recursiveEventMap.get(id).getEventsInOneCycle().get(0));
        intervalDates[1] = firstTime;
        this.recursiveEventMap.get(id).setIntervalDateDateGetter(intervalDates);
        if (newCycles.size()>1 && firstTime.isAfter(startTimeGetter(this.recursiveEventMap.get(id).getLastEvent()))){
            this.addRecursion(this.recursiveEventMap.get(id));
        }
    }






}












