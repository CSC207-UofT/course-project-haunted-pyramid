package controllers;
import entities.Event;
import gateways.IOSerializable;
import usecases.CalendarManager;
import usecases.EventManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import presenters.DisplayCalendarFactory; // JUST FOR THE DEMONSTRATION

public class EventController {

    private final EventManager eventManager;
    private final CalendarManager calendarManager;
    private final Scanner scanner = new Scanner(System.in);
    private final DisplayCalendarFactory displayCalendarFactory;
    private Integer nextID = 0;

    public EventController(boolean hasSavedData, IOSerializable ioSerializable, CalendarManager calendarManager){
        if (hasSavedData) {
            this.eventManager = new EventManager(ioSerializable.eventsReadFromSerializable());
        } else {
            this.eventManager = new EventManager(new ArrayList<>());
        }
        this.calendarManager = calendarManager;
        this.displayCalendarFactory = new DisplayCalendarFactory(this.calendarManager);

    }
    public void schedule(){
        String type = IOController.getEventType();
        Integer ID = nextID;
        nextID += 1;
        Set<Event> changes;
        String name = IOController.getName();
        String course = IOController.getCourse();
        List<Integer> date = IOController.getDate("Enter the date of the event");
        List<Integer> start = IOController.getTime("Enter the start time");
        List<Integer> end = IOController.getTime("enter the end time");
        this.eventManager.addEvent(ID, name, type, date.get(0), date.get(1), date.get(2), end.get(0), end.get(1),
                "", "");
        this.calendarManager.addToCalendar(eventManager.get(ID));


            /*// THIS JUST FOR THE TESTING. WILL BE SEPARATED IN THE FUTURE
            System.out.println(this.calendarPresenter.showMonthCalendar(Integer.parseInt(dateParts[0]),
                    Integer.parseInt(dateParts[1])));*/
    }

    public EventManager getEventManager() { return this.eventManager; }
}
