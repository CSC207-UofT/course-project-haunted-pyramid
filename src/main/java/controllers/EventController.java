package controllers;
import entities.Event;
import gateways.IOSerializable;
import usecases.CalendarManager;
import usecases.EventManager;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

import presenters.DisplayCalendarFactory; // JUST FOR THE DEMONSTRATION

public class EventController {

    private final EventManager eventManager;
    private final CalendarManager calendarManager;
    private final DisplayCalendarFactory displayCalendarFactory;

    public EventController(boolean hasSavedData, IOSerializable ioSerializable, CalendarManager calendarManager){
        /*if (hasSavedData) {
            this.eventManager = new EventManager(ioSerializable.eventsReadFromSerializable());
        }*/
        this.eventManager = new EventManager();
        this.calendarManager = calendarManager;
        this.displayCalendarFactory = new DisplayCalendarFactory(this.calendarManager);

    }
    public void schedule(){
        String type = IOController.getEventType();
        Set<Event> changes;
        String title = IOController.getName();
        String course = IOController.getCourse();
        List<Integer> date = IOController.getDate("Enter the date of the event");
        List<Integer> start = IOController.getTime("Enter the start time");
        List<Integer> end = IOController.getTime("enter the end time");
        this.calendarManager.addToCalendar(this.eventManager.addEvent(title, date.get(0), date.get(1), date.get(2),
                start.get(0), start.get(1), end.get(0), end.get(1)));
        this.displayEvents();
    }

    //for testing purposes
    public void displayEvents(){
        for (Event event: this.eventManager.getAllEvents()){
            System.out.println(eventManager.getID(event) + " " + eventManager.getName(event) + "-" +
                    eventManager.getStart(event) + "/" + eventManager.getEnd(event));
        }
    }

    public EventManager getEventManager() { return this.eventManager; }

}
