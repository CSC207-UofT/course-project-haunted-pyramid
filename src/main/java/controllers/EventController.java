package controllers;
import entities.Event;
import gateways.IOSerializable;
import usecases.CalendarManager;
import usecases.EventManager;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

import presenters.DisplayCalendarFactory; // JUST FOR THE DEMONSTRATION
import usecases.WorkSessionScheduler;

public class EventController {

    private final EventManager eventManager;
    private final CalendarManager calendarManager;
    private final WorkSessionController workSessionController;
    private final DisplayCalendarFactory displayCalendarFactory;

    public EventController(boolean hasSavedData, IOSerializable ioSerializable, CalendarManager calendarManager){
        /*if (hasSavedData) {
            this.eventManager = new EventManager(ioSerializable.eventsReadFromSerializable());
            this.workSessionController = new workSessionController(ioSerializable.
        }*/
        this.eventManager = new EventManager();
        this.calendarManager = calendarManager;
        this.displayCalendarFactory = new DisplayCalendarFactory(this.calendarManager);
        this.workSessionController = new WorkSessionController();
    }
    public void schedule(){
        Set<Event> changes;
        String title = IOController.getName();
        String course = IOController.getCourse();
        Integer[] date = IOController.getDate("Enter the date of the event");
        List<Integer> start = IOController.getTime("Enter the start time");
        List<Integer> end = IOController.getTime("enter the end time");
        Event event = this.eventManager.addEvent(title, date[0], date[1], date[2],
                start.get(0), start.get(1), end.get(0), end.get(1));
        this.calendarManager.addToCalendar(event);
        this.edit(this.eventManager.getID(event));
    }

    public void edit(Integer ID){
        boolean save = false;

        if (this.eventManager.containsID(ID)){
            while(!save){
                System.out.println(this.eventManager.displayEvent(ID));
                String next = IOController.getAnswer("select a field to edit followed by its new value " +
                        "[i.e. start: 0000-00-00-00-00], or \nprep \nrecurse \nsave \ndelete");
                String[] nextArgs = next.split(": ");

                if (nextArgs[0].equalsIgnoreCase("save")){
                    save = true;
                }else if (nextArgs[0].equalsIgnoreCase("delete")) {
                    this.eventManager.remove(ID);
                    save = true;
                }else if (nextArgs[0].equals("start")){
                    this.eventManager.setStart(this.eventManager.get(ID), nextArgs[1]);
                }else if (nextArgs[0].equalsIgnoreCase("end")){
                    this.eventManager.setEnd(this.eventManager.get(ID), nextArgs[1]);
                }else if (nextArgs[0].equalsIgnoreCase("description")){
                    this.eventManager.setDescription(this.eventManager.get(ID), nextArgs[1]);
                }else if (nextArgs[0].equalsIgnoreCase("name")){
                    this.eventManager.setName(this.eventManager.get(ID), nextArgs[1]);
                }else if (nextArgs[0].equalsIgnoreCase("prep")){
                    this.workSessionController.edit(this.eventManager.get(ID), this.eventManager);
                }else if (nextArgs[0].equalsIgnoreCase("recursion")){
                }
            }

        }
    }

    //for testing purposes
    public void displayEvents(){
        for (Event event: this.eventManager.timeOrder(this.eventManager.getAllEvents())){
            System.out.println(this.eventManager.displayEvent(this.eventManager.getID(event)));
        }
    }

    public EventManager getEventManager() { return this.eventManager; }

}
