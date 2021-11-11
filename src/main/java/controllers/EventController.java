package controllers;
import entities.Event;
import gateways.IOSerializable;
import usecases.CalendarManager;
import usecases.EventManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import presenters.DisplayCalendarFactory; // JUST FOR THE DEMONSTRATION


public class EventController {

    private final EventManager eventManager;
    private final WorkSessionController workSessionController;
    private final RecursionController recursionController;

    public EventController(boolean hasSavedData, IOSerializable ioSerializable){
        this.workSessionController = new WorkSessionController();
        if (hasSavedData) {
            this.eventManager = new EventManager(ioSerializable.eventsReadFromSerializable());
        } else {
            this.eventManager = new EventManager();
            this.eventManager.addObserver(this.workSessionController.getWorkSessionScheduler());
        }
        this.recursionController = new RecursionController();
    }
    public void schedule(){
        Set<Event> changes;
        String title = IOController.getName();
        String course = IOController.getCourse();
        Integer[] date = IOController.getDate("Enter the date of the event");
        List<Integer> end = IOController.getTime("enter the end time");
        Event event = this.eventManager.addEvent(title, LocalDateTime.of(date[0], date[1], date[2], end.get(0), end.get(1)));
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
                }else if (nextArgs[0].equalsIgnoreCase(("recurse"))){
                    this.recursionController.edit(this.eventManager.get(ID), this.eventManager);
                }
            }

        }
    }

    //for testing purposes
    public void displayEvents(){
        for (Event event: this.eventManager.timeOrder(this.eventManager.getAllEventsFlatSplit())){
            System.out.println(this.eventManager.displayEvent(this.eventManager.getID(event)));
        }
    }

    public EventManager getEventManager() { return this.eventManager; }

}
