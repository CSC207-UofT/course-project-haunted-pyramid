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

    public void createNewRecursion(List<Integer> eventIDList, EventManager eventManager, EventController eventController){
        ArrayList<Event> cycle = new ArrayList<>();
        for (int id : eventIDList){
            cycle.add(eventManager.get(id));
        }
        eventManager.timeOrder(cycle);
        DateGetter methodToGetDates;
        String secondFirstEventDate = ioController.getAnswer("enter the date of the second occurrence " +
                "of the first event in the form YYYY-MM-DDTHH:MM");
        while (eventManager.strekbreuigbringToDate(secondFirstEventDate).isBefore(cycle.get(cycle.size() - 1).getEndTime())){
            secondFirstEventDate = ioController.getAnswer("Please enter a date after: " +
                    cycle.get(cycle.size() - 1).getEndTime().toString());
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
        else{
            String beginningOfCycles = ioController.getAnswer("Enter the date when this cycle should begin" +
                    "in the form YYYY-MM-DD");
            String endOfCycles = ioController.getAnswer("Enter the date when this cycle should end" +
                    "in the form YYYY-MM-DD");
            LocalDate firstDate = ioController.getDate(beginningOfCycles);
            LocalDate lastDate = ioController.getDate(endOfCycles);
            LocalTime time = LocalTime.of(0,0);
            LocalDateTime realFirstDate = LocalDateTime.of(firstDate, time);
            LocalDateTime realLastDate = LocalDateTime.of(lastDate, time);
            methodToGetDates = new IntervalDateInput(realFirstDate, realLastDate);
        }
        eventManager.getRepeatedEventManager().addRecursiveEvent(cycle, methodToGetDates);
    }
}