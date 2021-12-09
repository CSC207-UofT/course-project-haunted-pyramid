package usecases.events;

import entities.Event;
import entities.recursions.RecursiveEvent;
import helpers.EventHelper;
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
    private EventHelper eventHelper = new EventHelper();


    /**
     *
     * Initialize an empty RepeatedEventManager.
     */

    public RepeatedEventManager(){
        this.recursiveIdToDateToEventsMap = new HashMap<>();
        this.recursiveEventMap = new HashMap<>();
    }

    /**
     * Initialize a RepeatedEventManager.
     *
     * @param recursiveIdToRecursiveEvent a map with values recursive events and key the uuid of each recursive event.
     */
    public RepeatedEventManager(Map<UUID, RecursiveEvent> recursiveIdToRecursiveEvent){
        this.recursiveIdToDateToEventsMap = new HashMap<>();
        this.recursiveEventMap = recursiveIdToRecursiveEvent;
        for (UUID uuid : recursiveIdToRecursiveEvent.keySet()) {
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
        return recursiveIdToDateToEventsMap;}
    public Map<UUID, RecursiveEvent> getRecursiveEventMap() {return recursiveEventMap;}

    /**
     * Adds a recursive event to the two maps of the repeated event manager in the appropriate way.
     *
     * @param recursiveEvent The recursive event to add to the repeated event manager.
     */
    public void addRecursion(RecursiveEvent recursiveEvent){
        this.recursiveEventMap.put(recursiveEvent.getId(), recursiveEvent);
        List<Event> events = recursiveEvent.listOfEventsInCycles(recursiveEvent.getEventsInOneCycle());
        this.recursiveIdToDateToEventsMap.put(recursiveEvent.getId(), eventListToMap(events,
                recursiveEvent.getCycleLength()));
    }

    /**
     * This method search all events in the repeated event manager and returns the event who matches the ID uuid (if
     * there is one).
     * @param uuid the ID of the event of interest
     * @return the events with ID uuid, or null if there is no such event in any recursion.
     */
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
     * @param id The id of a Recursive event.
     * @return Given the id of a recursive event object, this methods access the events in one cycle of this repetition
     * and returns a map with the id of the original event in the cycle as keys, and the list of events in the period
     * of repetition as values.
     */

    public List<Event> getEventsFromRecursion(UUID id){
        List<Event> eventsInOneCycle = this.recursiveEventMap.get(id).getEventsInOneCycle();
        return this.recursiveEventMap.get(id).listOfEventsInCycles(eventsInOneCycle);
    }

    /**
     * @param events all the events in a recursion
     * @param cycleLength the length of one cycle in this recursion.
     *
     * @return a map whose values are the event in some individual cycle of the recursion, and keys the date of the
     * first event of this particular cycle.
     */
    public Map<LocalDateTime, List<Event>> eventListToMap(List<Event> events, int cycleLength){
        Map<LocalDateTime, List<Event>> datesAndEvents = new HashMap<>();
        try{
        int endLoop = events.size();
        int i = 1;
        while(cycleLength*i < endLoop){
            datesAndEvents.put(eventHelper.startTimeGetter(events.get(cycleLength*(i-1))),
                    events.subList(cycleLength*(i-1), cycleLength*i));
            i++;
        }
        datesAndEvents.put(eventHelper.startTimeGetter(events.get(cycleLength*(i-1))), events.subList(cycleLength*(i-1),
                endLoop));
        return datesAndEvents;
        }
        catch (IndexOutOfBoundsException e){
            return datesAndEvents;
        }
    }

    /**
     * Gets all the events in the recursiveEvent and use the eventListToMap to create a map with dates as keys and
     * list of events as values, then add this map to the recursiveIdToDateToEventsMap map with the recursiveEvent
     * uuid as its key.
     *
     * @param recursiveEvent a particular recursive event.
     */
    private void addEventsFromRecursiveEvent(RecursiveEvent recursiveEvent){
        int cycleLength = recursiveEvent.getCycleLength();
        List<Event> allEventsInCycles = recursiveEvent.listOfEventsInCycles(recursiveEvent.getEventsInOneCycle());
        Map<LocalDateTime, List<Event>> datesAndEvents = eventListToMap(allEventsInCycles, cycleLength);
        UUID myID = recursiveEvent.getId();
        this.recursiveIdToDateToEventsMap.put(myID, datesAndEvents);
    }

    /**
     * Constructs a recursiveEvent with interval dates as a recursive pattern.
     *
     * @param eventsInCycle the list of events in the base cycle.
     * @param periodOfRepetition the two date between which the events in eventsInCycle repeat.
     * @return The constructed recursiveEvent.
     */
    private RecursiveEvent recursiveEventConstructor(List<Event> eventsInCycle, LocalDateTime[] periodOfRepetition){
        RecursiveEvent recursiveEvent = new RecursiveEvent(UUID.randomUUID());
        for (Event event : eventsInCycle){
            event.setRecursiveId(recursiveEvent.getId());
        }
        recursiveEvent.setIntervalDateDateGetter(periodOfRepetition);
        recursiveEvent.setEventsInOneCycle(eventsInCycle);
        return recursiveEvent;
    }

    /**
     * Constructs a recursiveEvent with events as a base cycle but with no recursion pattern.
     *
     * @param events the list of events in the base cycle.
     * @return the uuid of the newly constructed recursive event.
     */
    public UUID recursiveEventConstructor1(List<Event> events){
        UUID uuid = UUID.randomUUID();
        RecursiveEvent recursiveEvent = new RecursiveEvent(uuid, events);
        this.recursiveEventMap.put(uuid, recursiveEvent);
        return uuid;
    }

    /**
     * Constructs a recursiveEvent with interval dates as a recursive pattern then add it to the two maps of the
     * repeated event manager in the appropriate way.
     *
     * @param eventsInCycle the list of events in the base cycle.
     * @param periodOfRepetition the two date between which the events in eventsInCycle repeat.
     * @return the uuid of the newly constructed recursive event.
     */
    private UUID addEventsFromRecursiveEvent(List<Event> eventsInCycle, LocalDateTime[] periodOfRepetition){
        RecursiveEvent recursiveEvent = recursiveEventConstructor(eventsInCycle, periodOfRepetition);
        this.recursiveEventMap.put(recursiveEvent.getId(), recursiveEvent);
        this.addEventsFromRecursiveEvent(recursiveEvent);
        return recursiveEvent.getId();
    }

    /**
     *
     * @param id the id of the recursive event of interest.
     * @return the list of all events in the recursion of interest
     */
    public List<Event> getAllEventsFromRecursiveEvent(UUID id){
        List<Event> result = new ArrayList<>();
        if (!this.recursiveIdToDateToEventsMap.isEmpty()) {
            Map<LocalDateTime, List<Event>> dateEventMap = this.recursiveIdToDateToEventsMap.get(id);
            for (List<Event> events : dateEventMap.values()) {
                result.addAll(events);
            }
            return eventHelper.timeOrder(result);
        }
        else {
            return null;
        }
    }

    /**
     * Creates a new recursive event from an old one after it has being updated.
     *
     * @param allEvents all the events in the old cycle.
     * @param newCycle the new base cycle after the change has been implemented.
     * @return the uuid of the new recursive event created.
     */
    private UUID newRecursiveEventForUpdate(List<Event> allEvents, List<Event> newCycle) {
        Event lastEvent = allEvents.get(allEvents.size()-1);
        LocalDateTime lastEventEndTime = eventHelper.startTimeGetter(lastEvent);
        LocalDateTime[] intervalDates = new LocalDateTime[2];
        intervalDates[0] = eventHelper.startTimeGetter(newCycle.get(0));
        intervalDates[1] = lastEventEndTime.plus(Duration.ofDays(1));
        return addEventsFromRecursiveEvent(newCycle, intervalDates);
    }

    /**
     * This method is used to handle deletion of an event from a recursion with a single event in each cycle.
     *
     * @param changed the event to remove from the recursion with a single event.
     * @return The cycle to which the event "changed" belongs to.
     */
    private List<Event> newOneEventCycle(Event changed){
        UUID id = changed.getRecursiveId();
        Map<LocalDateTime, List<Event>> thisRecursionDates = this.recursiveIdToDateToEventsMap.get(id);
        List<LocalDateTime> results = new ArrayList<>();
        for(LocalDateTime localDateTime : thisRecursionDates.keySet()){
            if(eventHelper.startTimeGetter(changed).isAfter(localDateTime) ||
                    eventHelper.startTimeGetter(changed).isEqual(localDateTime)){
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

    /**
     * This method creates a new recursion from an old one, after implementing the change described by the string
     * addRemoveChange.
     *
     * @param addRemoveChange a String describing whether to delete/add or change an event in a particular recursion.
     * @param changed the event to delete/add or change in a particular recursion.
     */
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
            inputForNewRecursion = this.recursiveEventMap.get(id).cycleAfterAdditionChange(changed, "add",
                    allEvents);
            newCycles = inputForNewRecursion.get(1);
            uuid = newRecursiveEventForUpdate(inputForNewRecursion.get(0), newCycles);
        }
        else if (addRemoveChange.equalsIgnoreCase("change")){
            inputForNewRecursion = this.recursiveEventMap.get(id).cycleAfterAdditionChange(changed, "Change",
                    allEvents);
            newCycles = inputForNewRecursion.get(1);
            uuid = newRecursiveEventForUpdate(inputForNewRecursion.get(0), newCycles);
        }
        else{
            newCycles = newOneEventCycle(changed);
            uuid = id;
        }
        this.recursiveIdToDateToEventsMap.get(uuid).put(eventHelper.startTimeGetter(newCycles.get(0)),
                newCycles.subList(0,newCycles.size() - 1));
        recursionModifier(changed, id, newCycles);
    }

    /**
     * This method modify the original recursion which has now being updated to only include events before the update
     * has taken place.
     *
     * @param changed the event to modify or add.
     * @param id the id of the recursion this modification will take place in.
     * @param newCycles the base cycle of the recursion after the change has taken place.
     */
    private void recursionModifier(Event changed, UUID id, List<Event> newCycles) {
        LocalDateTime firstTime = eventHelper.startTimeGetter(newCycles.get(0));
        Set<LocalDateTime> keySet = new HashSet<>(this.recursiveIdToDateToEventsMap.get(id).keySet());
        this.recursiveIdToDateToEventsMap.get(id).remove(eventHelper.startTimeGetter(changed));
        this.recursiveIdToDateToEventsMap.get(id).remove(eventHelper.startTimeGetter(newCycles.get(0)));
        for(LocalDateTime localDateTime : keySet){
            if(localDateTime.isAfter(firstTime) || localDateTime.isEqual(firstTime)){
                this.recursiveIdToDateToEventsMap.get(id).remove(localDateTime);
            }
        }
        LocalDateTime[] intervalDates = new LocalDateTime[2];
        intervalDates[0] = eventHelper.startTimeGetter(this.recursiveEventMap.get(id).getEventsInOneCycle().get(0));
        intervalDates[1] = firstTime;
        this.recursiveEventMap.get(id).setIntervalDateDateGetter(intervalDates);
        if (newCycles.size()>1 && firstTime.isAfter(eventHelper.startTimeGetter(this.recursiveEventMap.
                get(id).getLastEvent()))){
            this.addRecursion(this.recursiveEventMap.get(id));
        }
    }
}