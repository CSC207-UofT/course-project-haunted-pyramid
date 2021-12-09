package controllers;


import helpers.ControllerHelper;
import usecases.events.EventManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Malik Lahlou
 */

public class RecursionController {
    private final IOController ioController = new IOController();
    private final ControllerHelper helper = new ControllerHelper();


    /**
     * This method prompt the user to enter the date the first event of the cycle repeats for the second time. It catches
     * if the input is incorrect.
     *
     * @param firstEventTime the time of the first event in the cycle.
     * @param lastEventInCycleDate the last day in the cycle, the input sate must be after this one.
     * @return the date the first event of the cycle repeats for the second time
     */
    private LocalDateTime secondFirstEventDateTime(LocalTime firstEventTime, LocalDateTime lastEventInCycleDate){
        System.out.println("Please enter the Second Occurrence date of the first event");
        LocalDate secondFirstEventDate = ioController.getDate("Enter the Date of the Occurrence");
        LocalDateTime secondFirstEventDateTime = LocalDateTime.of(secondFirstEventDate, firstEventTime);
        while (secondFirstEventDateTime.isBefore(lastEventInCycleDate)){
            System.out.println("Please enter a date after: " + lastEventInCycleDate);
            secondFirstEventDate = ioController.getDate("Enter the Date of the Occurrence");
            secondFirstEventDateTime = LocalDateTime.of(secondFirstEventDate, firstEventTime);
        }
        return secondFirstEventDateTime;
    }

    /**
     * @return The two dates between which the cycle of the recursion should repeat.
     */
    private LocalDateTime[] intervalDateGetterDates() {
        LocalDateTime[] result = new LocalDateTime[2];
        LocalDate firstDate = ioController.getDate("Enter the date when this cycle should begin");
        LocalDate lastDate = ioController.getDate("Enter the date when this cycle should end");
        LocalTime time = LocalTime.of(0,0);
        result[0] = LocalDateTime.of(firstDate, time);
        result[1] = LocalDateTime.of(lastDate, time);
        return result;
    }

    /**
     * Set the recursive pattern for the recursive event with ID uuid.
     *
     * @param eventManager  the event manager containing all events and recursive events.
     * @param uuid  the uuid of the recursive event for which this recursive pattern will be added.
     */
    private void setDateGetter(EventManager eventManager, UUID uuid){
        String repetitionMethod = ioController.getAnswer("Enter either: 'num' if there is a " +
                "number of times you want to repeat the events you selected, or 'dates' if there are two dates " +
                "in between which the the events you selected should repeats");
        while (!repetitionMethod.equalsIgnoreCase("num") &&
                !repetitionMethod.equalsIgnoreCase("dates")){
            repetitionMethod = ioController.getAnswer("Please check your input!! Enter either: 'num' for the " +
                    "number of times you want to repeat the events you selected, or 'dates' if there are two dates in " +
                    "between which the the events you selected should repeats");
        }
        if (repetitionMethod.equalsIgnoreCase("num")){
            String numOfRepetitions = ioController.getAnswer("Enter the number of times the cycle repeats");
            while (!helper.isInteger(numOfRepetitions)){
                numOfRepetitions = ioController.getAnswer("The input must be a whole number.");
            }
            eventManager.getRepeatedEventManager().getRecursiveEventMap().get(uuid).
                    setNumberOfRepetitionDateGetter(Integer.parseInt(numOfRepetitions));
        }
        else {
            eventManager.getRepeatedEventManager().getRecursiveEventMap().get(uuid).
                    setIntervalDateDateGetter(intervalDateGetterDates());
        }
    }

    /**
     * This method creates a recursive event and returns its uuid.
     *
     * @param eventIDList the list of event IDs which will be part of the first cycle of this recursion
     * @param eventManager  the event manager containing all events and recursive events,
     * @return the uuid of the newly created recursive event.
     */
    private UUID addEventsToRecursiveObject(List<UUID> eventIDList, EventManager eventManager){
        UUID uuid = eventManager.getRepeatedEventManager().recursiveEventConstructor1(
                eventManager.eventHelper.timeOrder(eventManager.getEvents(eventIDList)));
        LocalTime firstEventEndTime = eventManager.getRepeatedEventManager().getRecursiveEventMap().get(uuid).
                getEventsInOneCycle().get(0).getEndTime().toLocalTime();
        LocalDateTime lastEventEndDate = eventManager.getRepeatedEventManager().getRecursiveEventMap().get(uuid).
                getEventsInOneCycle().get(eventIDList.size() - 1).getEndTime();
        LocalDateTime secondFirstEventDateTime = secondFirstEventDateTime(firstEventEndTime, lastEventEndDate);
        String eventName = eventManager.getDefaultEventInfoGetter().getName(eventManager.getRepeatedEventManager().getRecursiveEventMap().get(uuid).
                getEventsInOneCycle().get(0));
        eventManager.getRepeatedEventManager().getRecursiveEventMap().get(uuid).addEventToCycle(
                eventManager.getEvent(eventName + "-2", secondFirstEventDateTime));
        return uuid;
    }

    /**
     * This method creates a recursive event and returns its uuid.
     *
     * @param eventIDList the list of event IDs which will be part of the first cycle of this recursion
     * @param eventManager  the event manager containing all events and recursive events.
     * @param secondFirstEventDateTime the second instance of the first event in the base cycle.
     * @return the uuid of the newly created recursive event.
     */
    private UUID addEventsToRecursiveObject(List<UUID> eventIDList, EventManager eventManager,
                                            LocalDateTime secondFirstEventDateTime){
        UUID uuid = eventManager.getRepeatedEventManager().recursiveEventConstructor1(
                eventManager.eventHelper.timeOrder(eventManager.getEvents(eventIDList)));
        String eventName = eventManager.getDefaultEventInfoGetter().getName(eventManager.getRepeatedEventManager().
                getRecursiveEventMap().get(uuid).getEventsInOneCycle().get(0));
        eventManager.getRepeatedEventManager().getRecursiveEventMap().get(uuid).addEventToCycle(
                eventManager.getEvent(eventName + "-2", secondFirstEventDateTime));
        return uuid;
    }

    /**
     *
     * @param eventIDList the ids of the events to repeat.
     * @param eventManager  the event manager containing all events and recursive events.
     * This method prompt the user to create repetitions of events with ids in eventIDList.
     */
    public void createNewRecursion(List<UUID> eventIDList, EventManager eventManager){
        UUID uuid = addEventsToRecursiveObject(eventIDList, eventManager);
        setDateGetter(eventManager, uuid);
        eventManager.getRepeatedEventManager().getRecursiveIdToDateToEventsMap().put(uuid,
                eventManager.getRepeatedEventManager().eventListToMap(eventManager.
                        getRepeatedEventManager().getEventsFromRecursion(uuid), eventIDList.size()));
    }

    /**
     * Creates a recursive event for Recursive menu.
     * @param eventIDList the ids of the events to repeat.
     * @param eventManager the event manager containing all events and recursive events.
     * @param dateTime1 the date this recursion should start.
     * @param dateTime2 the date this recursion should end.
     * @param secondFirstEventDateTime the second instance of the first event in the base cycle.
     * This method create repetitions of events with ids in eventIDList.
     */
    public void createNewRecursion(List<UUID> eventIDList, EventManager eventManager, LocalDateTime dateTime1,
                                   LocalDateTime dateTime2, LocalDateTime secondFirstEventDateTime){
        UUID uuid = addEventsToRecursiveObject(eventIDList, eventManager, secondFirstEventDateTime);
        LocalDateTime[] input = new LocalDateTime[2];
        input[0] = dateTime1;
        input[1] = dateTime2;
        eventManager.getRepeatedEventManager().getRecursiveEventMap().get(uuid).
                setIntervalDateDateGetter(input);
        eventManager.getRepeatedEventManager().getRecursiveIdToDateToEventsMap().put(uuid,
                eventManager.getRepeatedEventManager().eventListToMap(eventManager.
                        getRepeatedEventManager().getEventsFromRecursion(uuid), eventIDList.size()));
    }

    /**
     * Creates a recursive event for Recursive menu.
     * @param eventIDList the ids of the events to repeat.
     * @param eventManager  the event manager containing all events and recursive events.
     * @param numRepetition the number of times this cycle should repeat.
     * @param secondFirstEventDateTime the second instance of the first event in the base cycle.
     * This method create repetitions of events with ids in eventIDList.
     */
    public void createNewRecursion(List<UUID> eventIDList, EventManager eventManager, int numRepetition,
                                   LocalDateTime secondFirstEventDateTime){
        UUID uuid = addEventsToRecursiveObject(eventIDList, eventManager, secondFirstEventDateTime);
        eventManager.getRepeatedEventManager().getRecursiveEventMap().get(uuid).
                setNumberOfRepetitionDateGetter(numRepetition);
        eventManager.getRepeatedEventManager().getRecursiveIdToDateToEventsMap().put(uuid,
                eventManager.getRepeatedEventManager().eventListToMap(eventManager.
                        getRepeatedEventManager().getEventsFromRecursion(uuid), eventIDList.size()));
    }
}