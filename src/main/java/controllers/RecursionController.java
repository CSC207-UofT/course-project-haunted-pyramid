package controllers;

import entities.Event;
import entities.recursions.IntervalDateInput;
import entities.recursions.NumberOfRepetitionInput;
import helpers.ControllerHelper;
import interfaces.DateGetter;
import usecases.events.EventManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Malik Lahlou
 */

public class RecursionController {
    private final IOController ioController = new IOController();
    private final ControllerHelper helper = new ControllerHelper();

    public void createNewRecursion(List<Integer> eventIDList, EventManager eventManager){
        ArrayList<Event> cycle = new ArrayList<>();
        for (int id : eventIDList){
            cycle.add(eventManager.get(id));
        }
        eventManager.timeOrder(cycle);
        DateGetter methodToGetDates;
        System.out.println("Please enter the Second Occurrence date of the Event");
        LocalDateTime secondFirstEventDate = ioController.getDateTime("Enter the time of Occurrence",
                "Enter the Date of the Occurrence");
        while (secondFirstEventDate.isBefore(cycle.get(cycle.size() - 1).getEndTime())){
            System.out.println("Please enter a date after: " + cycle.get(cycle.size() - 1).getEndTime().toString());
            secondFirstEventDate = ioController.getDateTime("Enter the Time", "Enter the Date");
        }

        Event newEvent = eventManager.addEvent(cycle.get(0).getName() + "-2", secondFirstEventDate);
        cycle.add(newEvent);

        String repetitionMethod = ioController.getAnswer("Enter either: 'num' if there is a " +
                "number of times you want to repeat the events you selected, or 'dates' if there are two dates " +
                "in between which the the events you selected should repeats");
        while (!repetitionMethod.equalsIgnoreCase("num") &&
                !repetitionMethod.equalsIgnoreCase("dates")){
            repetitionMethod = ioController.getAnswer("Please check your input!! Enter either: the number " +
                    "of times you want to repeat the events you selected, or 'dates' if there are two dates in " +
                    "between which the the events you selected should repeats");
        }
        if (repetitionMethod.equalsIgnoreCase("num")){
            String numOfRepetitions = ioController.getAnswer("Enter the number of times the cycle repeats");
            while (!helper.isInteger(numOfRepetitions)){
                numOfRepetitions = ioController.getAnswer("The input must be a whole number.");
            }
            methodToGetDates = new NumberOfRepetitionInput(Integer.parseInt(numOfRepetitions));
        }
        else {
            methodToGetDates = getDateGetter();
        }
        eventManager.getRepeatedEventManager().addRecursiveEvent(cycle, methodToGetDates);
    }

    private DateGetter getDateGetter() {
        DateGetter methodToGetDates;
        LocalDate firstDate = ioController.getDate("Enter the date when this cycle should begin");
        LocalDate lastDate = ioController.getDate("Enter the date when this cycle should end");
        LocalTime time = LocalTime.of(0,0);
        LocalDateTime realFirstDate = LocalDateTime.of(firstDate, time);
        LocalDateTime realLastDate = LocalDateTime.of(lastDate, time);
        methodToGetDates = new IntervalDateInput(realFirstDate, realLastDate);
        return methodToGetDates;
    }
}