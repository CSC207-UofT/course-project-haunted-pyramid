package controllers;
import entities.ConstantID;
import entities.Event;
import gateways.IOSerializable;
import presenters.DisplayMenu;
import presenters.MenuStrategies.EventEditMenuContent;
import presenters.MenuStrategies.MenuContent;
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

    public EventController(boolean hasSavedData, IOSerializable ioSerializable){
        this.workSessionController = new WorkSessionController();
        if (hasSavedData) {
            this.eventManager = new EventManager(ioSerializable.eventsReadFromSerializable());
            ConstantID.set(this.eventManager.getMaxID());
        } else {
            this.eventManager = new EventManager();
            this.eventManager.addObserver(this.workSessionController.getWorkSessionScheduler());
        }
        this.recursionController = new RecursionController();
    }
    public void createDefaultEvent(){
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
                DisplayMenu dm = new DisplayMenu();
                EventEditMenuContent content = new EventEditMenuContent(this.eventManager.get(ID));
                System.out.println(dm.displayMenu(content));
                String next = IOController.getAnswer("select a field to edit followed by its new value " +
                        "[i.e. start: 0000-00-00-00-00], or \nprep \nrecurse \nsave \ndelete");
                if (next.equalsIgnoreCase("save")){
                    save = true;
                }else if (next.equalsIgnoreCase("delete")) {
                    this.delete(ID);
                    save = true;
                }else {
                    this.getAction(next, ID);
                }
            }
        }
    }

    private void getAction(String command, Integer ID){
        String[] nextArgs = command.split(": ");
        if (nextArgs[0].equals("start")){
            this.changeStart(ID, nextArgs[1]);
        }else if (nextArgs[0].equalsIgnoreCase("end")){
            this.changeEnd(ID, nextArgs[1]);
        }else if (nextArgs[0].equalsIgnoreCase("description")){
            this.changeDescription(ID, nextArgs[1]);
        }else if (nextArgs[0].equalsIgnoreCase("name")){
            this.changeName(ID, nextArgs[1]);
        }else if (nextArgs[0].equalsIgnoreCase("prep")){
            this.prep(ID);
        }else if (nextArgs[0].equalsIgnoreCase(("recurse"))){
            this.recurse(ID);
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

    public void changeEnd(Integer ID, String newEnd){
        try {
            this.eventManager.setEnd(this.eventManager.get(ID), newEnd);
        } catch(IllegalArgumentException illegalArgumentException){
            System.out.println("please enter a valid date of the form \n YYYY-MM-DDTHH:MM");
        }
    }

    public void changeDescription(Integer ID, String description){
        this.eventManager.setDescription(this.eventManager.get(ID), description);
    }

    public void changeName(Integer ID, String name){
        this.eventManager.setName(this.eventManager.get(ID), name);
    }

    public void prep(Integer ID){
        this.workSessionController.edit(this.eventManager.get(ID), this.eventManager);
    }

    public void recurse(Integer ID){
        this.recursionController.edit(this.eventManager.get(ID), this.eventManager);
    }

    //for testing purposes
    public void displayEvents(){
        for (Event event: this.eventManager.timeOrder(this.eventManager.getAllEventsFlatSplit())){
            System.out.println(this.eventManager.displayEvent(this.eventManager.getID(event)));
        }
    }

    public EventManager getEventManager() { return this.eventManager; }

}
