package controllers;
import entities.Event;
import gateways.IOSerializable;
import presenters.MenuStrategies.EventEditMenuContent;
import usecases.EventManager;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Set;


public class EventController {

    private final EventManager eventManager;
    private final WorkSessionController workSessionController;
    private final RecursionController recursionController;
    private final EventEditMenuContent eventEditMenuContent = new EventEditMenuContent();

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
        String title = IOController.getName();
        String course = IOController.getCourse();
        String date = IOController.getAnswer("Enter the date/time of the event in the form YYYY-MM-DDTHH:MM");
        try {
            Event event = this.eventManager.addEvent(title, date);
            this.edit(this.eventManager.getID(event));
        }catch(IllegalArgumentException illegalArgumentException){
            System.out.println("invalid date/time");
        }
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
                    this.delete(ID);
                    save = true;
                }else if (nextArgs[0].equals("start")){
                    this.changeStart(ID, nextArgs[1]);
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

    public void delete(Integer ID){
        this.eventManager.remove(ID);
    }
    public void changeStart(Integer ID, String newStart){
        try {
            this.eventManager.setStart(this.eventManager.get(ID), newStart);
        } catch(IllegalArgumentException illegalArgumentException){
            System.out.println("please enter a valid date of the form \n YYYY-MM-DDTHH:MM");
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
