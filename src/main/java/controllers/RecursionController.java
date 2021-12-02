package controllers;

import helpers.ConstantID;
import helpers.Constants;
import helpers.ControllerHelper;
import interfaces.DateGetter;
import presenters.MenuStrategies.DisplayMenu;
import presenters.MenuStrategies.RecursionEditMenuContent;
import usecases.events.EventManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Malik Lahlou
 */

public class RecursionController {
    private final IOController ioController = new IOController();
    private final ControllerHelper helper = new ControllerHelper();




    private LocalDateTime secondFirstEventDateTime(LocalTime firstEventTime, LocalDateTime lastEventInCycleDate){
        System.out.println("Please enter the Second Occurrence date of the first event");
        LocalDate secondFirstEventDate = ioController.getDate("Enter the Date of the Occurrence");
        LocalDateTime secondFirstEventDateTime = LocalDateTime.of(secondFirstEventDate, firstEventTime);
        while (secondFirstEventDateTime.isBefore(lastEventInCycleDate)){
            System.out.println("Please enter a date after: " + lastEventInCycleDate.toString());
            secondFirstEventDate = ioController.getDate("Enter the Date of the Occurrence");
            secondFirstEventDateTime = LocalDateTime.of(secondFirstEventDate, firstEventTime);
        }
        return secondFirstEventDateTime;
    }

    private LocalDateTime[] intervalDateGetterDates() {
        LocalDateTime[] result = new LocalDateTime[2];
        LocalDate firstDate = ioController.getDate("Enter the date when this cycle should begin");
        LocalDate lastDate = ioController.getDate("Enter the date when this cycle should end");
        LocalTime time = LocalTime.of(0,0);
        result[0] = LocalDateTime.of(firstDate, time);
        result[1] = LocalDateTime.of(lastDate, time);
        return result;
    }

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

    private UUID addEventsToRecursiveObject(List<UUID> eventIDList, EventManager eventManager){
        UUID uuid = eventManager.getRepeatedEventManager().recursiveEventConstructor1(
                eventManager.timeOrder(eventManager.getEvents(eventIDList)));
        LocalTime firstEventEndTime = eventManager.getRepeatedEventManager().getRecursiveEventMap().get(uuid).
                getEventsInOneCycle().get(0).getEndTime().toLocalTime();
        LocalDateTime lastEventEndDate = eventManager.getRepeatedEventManager().getRecursiveEventMap().get(uuid).
                getEventsInOneCycle().get(eventIDList.size() - 1).getEndTime();
        LocalDateTime secondFirstEventDateTime = secondFirstEventDateTime(firstEventEndTime, lastEventEndDate);
        String eventName = eventManager.getName(eventManager.getRepeatedEventManager().getRecursiveEventMap().get(uuid).
                getEventsInOneCycle().get(0));
        eventManager.getRepeatedEventManager().getRecursiveEventMap().get(uuid).addEventToCycle(
                eventManager.getEvent(eventName + "-2", secondFirstEventDateTime));
        return uuid;
    }

    /**
     *
     * @param eventIDList the ids of the events to repeat.
     * @param eventManager the event manager containing all the events of this user
     * This methods prompt the user to create repetitions of events with ids in eventIDList.
     */


    public void createNewRecursion(List<UUID> eventIDList, EventManager eventManager){
        UUID uuid = addEventsToRecursiveObject(eventIDList, eventManager);
        setDateGetter(eventManager, uuid);
        eventManager.getRepeatedEventManager().getRecursiveIdToDateToEventsMap().put(uuid,
                eventManager.getRepeatedEventManager().eventListToMap(eventManager.
                        getRepeatedEventManager().getEventsFromRecursion(uuid), eventIDList.size()));
    }




    /**
     *
     * @return A helper method that return a DateGetter given a user input.
     */


}