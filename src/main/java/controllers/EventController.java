package controllers;
import entities.ConstantID;
import entities.Event;
import gateways.IOSerializable;
import presenters.DisplayMenu;
import presenters.MenuStrategies.EventEditMenuContent;
import usecases.events.EventManager;
import usecases.WorkSessionScheduler;

import java.time.LocalTime;
import java.util.HashMap;

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
    private final IOController ioController;

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
        this.ioController = new IOController();
    }


    /**
     *
     * @param hasSavedData
     * @param ioSerializable
     */
    public EventController(boolean hasSavedData, IOSerializable ioSerializable){
        this.workSessionController = new WorkSessionController(new WorkSessionScheduler(new HashMap<>(),
                true));
        if (hasSavedData) {
            this.eventManager = new EventManager(ioSerializable.eventsReadFromSerializable());
            ConstantID.set(this.eventManager.getMaxID());
        } else {
            this.eventManager = new EventManager();
            this.eventManager.addObserver(this.workSessionController.getWorkSessionScheduler());
        }
        this.recursionController = new RecursionController();
        this.ioController = new IOController();
    }

    /**
     *
     */
    public void createDefaultEvent(){
        String title = ioController.getName();
        String date = ioController.getAnswer("Enter the date of the event in the form YYYY-MM-DD");
        String time = ioController.getAnswer("Enter the time of the event in the form HH:MM");
        try {
            Event event = this.eventManager.addEvent(title, date + "T" + time);
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
                String next = ioController.getAnswer("");
                if (next.equalsIgnoreCase("save")){
                    save = true;
                }else if (next.equalsIgnoreCase("delete")) {
                    if (ioController.getAnswer("are you sure? (y/n)").equalsIgnoreCase("y")){
                        this.delete(ID);
                        save = true;
                    }
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
        try{
            String[] nextArgs = command.split(": ");
            if (nextArgs[0].equals("start date")) {
                this.changeStartDate(ID, nextArgs[1]);
            } else if (nextArgs[0].equalsIgnoreCase("end date")) {
                this.changeEndDate(ID, nextArgs[1]);
            } else if (nextArgs[0].equalsIgnoreCase("prep")) {
                this.prep(ID);
            } else if (nextArgs[0].equalsIgnoreCase(("recurse"))) {
                this.recurse(ID);
            } else if (nextArgs[0].equalsIgnoreCase("start time")) {
                this.changeStartTime(ID, nextArgs[1]);
            } else if (nextArgs[0].equalsIgnoreCase("end time")) {
                this.changeEndTime(ID, nextArgs[1]);
            } else if (nextArgs[0].equalsIgnoreCase("description")) {
                this.changeDescription(ID, nextArgs[1]);
            } else if (nextArgs[0].equalsIgnoreCase("name")) {
                this.changeName(ID, nextArgs[1]);
            }
        }
        catch (Exception exception){
            System.out.println("Please check your input");
            String next = ioController.getAnswer("");
            getAction(next, ID);
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
    public void changeStartDate(Integer ID, String newStart){
        try {
            if (this.eventManager.getStartTimeString(this.eventManager.get(ID)) == null){
                this.eventManager.setStart(this.eventManager.get(ID), newStart + "T00:00");
            } else {
                this.eventManager.setStart(this.eventManager.get(ID), newStart + "T" +
                        this.eventManager.getStartTimeString(this.eventManager.get(ID)));
            }
        } catch(IllegalArgumentException illegalArgumentException){
            System.out.println("please enter a valid date of the form \n YYYY-MM-DD");
        }
    }

    /**
     *
     * @param ID
     * @param newEnd
     */
    public void changeEndDate(Integer ID, String newEnd){
        try {
            this.eventManager.setEnd(this.eventManager.get(ID), newEnd + "T" +
                    this.eventManager.getEndTimeString(this.eventManager.get(ID)));
        } catch(IllegalArgumentException illegalArgumentException){
            System.out.println("please enter a valid date of the form \n YYYY-MM-DD");
        }
    }

    public void changeEndTime(Integer ID, String newEnd){
        try {
            this.eventManager.setEnd(this.eventManager.get(ID), this.eventManager.getEndDateString(ID) + "T" +
                    newEnd);
        } catch(IllegalArgumentException illegalArgumentException){
            System.out.println("please enter a valid time of the form \n HH:MM");
        }
    }

    public void changeStartTime(Integer ID, String newStart){
        try {
            if (this.eventManager.getStartDateString(ID).isEmpty()){
                this.eventManager.setStart(this.eventManager.get(ID), this.eventManager.getEndDateString(ID) + "T" +
                        newStart);
            }else{
                this.eventManager.setStart(this.eventManager.get(ID), this.eventManager.getStartDateString(ID)
                        + "T" + newStart);
            }
        } catch(IllegalArgumentException illegalArgumentException){
            System.out.println("please enter a valid time of the form \n HH:MM");
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
        String nextStep = ioController.getAnswer("Enter 'Create' to create new recursion" +
                "'edit' to modify an existing one and 'delete' to remove all repetitions of this event");
        if (nextStep.equalsIgnoreCase("Create")){
            this.recursionController.createNewRecursion(this.eventManager.get(ID), this.eventManager, this);
        }
        //TODO: add more options to delete or modify a recursion (now can only add).
    }

    /**
     *
     * @return
     */
    public EventManager getEventManager() { return this.eventManager; }

}
