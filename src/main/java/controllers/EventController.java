package controllers;
import entities.ConstantID;
import entities.Event;
import gateways.IOSerializable;
import presenters.DisplayMenu;
import presenters.MenuStrategies.EventEditMenuContent;
import usecases.EventManager;
import usecases.WorkSessionScheduler;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for creating then editing, or editing selected individual events - change name, description
 * start time, end time, delete
 * passes to RecursiveController and WorkSessionController to edit recursion or prep of an individual event
 * @author Taite Cullen
 * @see Event
 * @see EventManager
 * @see RecursionController
 * @see WorkSessionController
 */
public class EventController {

    private final EventManager eventManager;
    private final WorkSessionController workSessionController;
    private final RecursionController recursionController;

    /**
     * constructor for EventController from serialized Events
     * creates <code>this.eventManager</code> of current User's events, sets ID counter to maximum ID in eventManager,
     * creates new workSessionController
     * @param hasSavedData
     * @param ioSerializable
     * @param workSessionController
     */
    public EventController(boolean hasSavedData, IOSerializable ioSerializable, WorkSessionController workSessionController){
        this.workSessionController = workSessionController;
        if (hasSavedData) {
            this.eventManager = new EventManager(ioSerializable.eventsReadFromSerializable());
            ConstantID.set(this.eventManager.getMaxID());
        } else {
            this.eventManager = new EventManager();
            this.eventManager.addObserver(this.workSessionController.getWorkSessionScheduler());
        }
        this.recursionController = new RecursionController();
    }

    /**
     *
     * @param hasSavedData
     * @param ioSerializable
     */
    public EventController(boolean hasSavedData, IOSerializable ioSerializable){
        this.workSessionController = new WorkSessionController(new WorkSessionScheduler(new HashMap<LocalTime, LocalTime>(),
                true, false));
        if (hasSavedData) {
            this.eventManager = new EventManager(ioSerializable.eventsReadFromSerializable());
            ConstantID.set(this.eventManager.getMaxID());
        } else {
            this.eventManager = new EventManager();
            this.eventManager.addObserver(this.workSessionController.getWorkSessionScheduler());
        }
        this.recursionController = new RecursionController();
    }

    /**
     *
     */
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

    /**
     *
     * @param ID
     */
    public void edit(Integer ID){
        boolean save = false;
        if (this.eventManager.containsID(ID)){
            while(!save){
                DisplayMenu dm = new DisplayMenu();
                EventEditMenuContent content = new EventEditMenuContent(this.eventManager.get(ID));
                System.out.println(dm.displayMenu(content));
                String next = IOController.getAnswer("");
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

    /**
     *
     * @param command
     * @param ID
     */
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

    /**
     *
     * @param ID
     */
    public void delete(Integer ID){
        this.eventManager.remove(ID);
    }

    /**
     *
     * @param ID
     * @param newStart
     */
    public void changeStart(Integer ID, String newStart){
        try {
            this.eventManager.setStart(this.eventManager.get(ID), newStart);
        } catch(IllegalArgumentException illegalArgumentException){
            System.out.println("please enter a valid date of the form \n YYYY-MM-DDTHH:MM");
        }
    }

    /**
     *
     * @param ID
     * @param newEnd
     */
    public void changeEnd(Integer ID, String newEnd){
        try {
            this.eventManager.setEnd(this.eventManager.get(ID), newEnd);
        } catch(IllegalArgumentException illegalArgumentException){
            System.out.println("please enter a valid date of the form \n YYYY-MM-DDTHH:MM");
        }
    }

    /**
     *
     * @param ID
     * @param description
     */
    public void changeDescription(Integer ID, String description){
        this.eventManager.setDescription(this.eventManager.get(ID), description);
    }

    /**
     *
     * @param ID
     * @param name
     */
    public void changeName(Integer ID, String name){
        this.eventManager.setName(this.eventManager.get(ID), name);
    }

    /**
     *
     * @param ID
     */
    public void prep(Integer ID){
        this.workSessionController.edit(this.eventManager.get(ID), this.eventManager);
    }

    /**
     *
     * @param ID
     */
    public void recurse(Integer ID){
        this.recursionController.edit(this.eventManager.get(ID), this.eventManager);
    }

    /**
     *
     * @return
     */
    public EventManager getEventManager() { return this.eventManager; }

}
