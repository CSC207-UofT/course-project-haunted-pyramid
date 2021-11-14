package controllers;

import entities.ConstantID;
import entities.Event;
import gateways.IOSerializable;
import presenters.DisplayMenu;
import presenters.MenuStrategies.EventEditMenuContent;
import usecases.events.EventManager;
import usecases.WorkSessionScheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

/**
 * Controller for creating then editing, or editing selected individual events - change name, description
 * start time, end time, delete
 * passes to RecursiveController and WorkSessionController to edit recursion or prep of an individual event
 *
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
     * with pre-made WorkSessionController
     *
     * @param hasSavedData          boolean
     * @param ioSerializable        serialized
     * @param workSessionController workSessionController
     */
    public EventController(boolean hasSavedData, IOSerializable ioSerializable, WorkSessionController workSessionController) {
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
     * constructor for EventController from serialized Events
     * creates <code>this.eventManager</code> of current User's events, sets ID counter to maximum ID in eventManager,
     * creates new workSessionController
     *
     * @param hasSavedData   boolean
     * @param ioSerializable serialized
     */
    public EventController(boolean hasSavedData, IOSerializable ioSerializable) {
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
     * gets this.EventManager
     *
     * @return this.EventManager
     */
    public EventManager getEventManager() {
        return this.eventManager;
    }

    /**
     * allows the user to create a default event through terminal - asks for title, start date, start time,
     * otherwise default values - passes to <code>EventController.edit(the new event)</code>
     */
    public void createDefaultEvent() {
        String title = ioController.getName();
        LocalDateTime dateTime = ioController.getDateTime("Enter the End Time of the Event",
                "Enter the end date of the event");
        Event event = this.eventManager.addEvent(title, dateTime);
        this.edit(this.eventManager.getID(event));
    }

    /**
     * Allows the user to edit an event from a list of actions
     *
     * @param ID the ID of the event to be edited by the user
     * @see EventController#getAction
     */
    public void edit(Integer ID) {
        boolean save = false;
        if (this.eventManager.containsID(ID)) {
            while (!save) {
                DisplayMenu dm = new DisplayMenu();
                EventEditMenuContent content = new EventEditMenuContent(this.eventManager.get(ID));
                System.out.println(dm.displayMenu(content));
                String next = ioController.getAnswer("Enter the Number of the Action You would like to Perform");
                save = this.getAction(next, ID);
            }
        }
    }

    /**
     * allows determines by a list of actions which method to complete next
     *
     * @param command number from 1-10 completes corresponding command from list
     * @param ID      the ID of the event being passed to the next method
     * @return true if event was saved (or deleted), false otherwise
     */
    private boolean getAction(String command, Integer ID) {
        switch (command) {
            case "1":
                this.changeStartDate(ID);
                break;
            case "2":
                this.changeStartTime(ID);
                break;
            case "3":
                this.changeEndDate(ID);
                break;
            case "4":
                this.changeEndTime(ID);
                break;
            case "5":
                this.changeDescription(ID);
                break;
            case "6":
                this.changeName(ID);
                break;
            case "7":
                this.recurse(ID);
                break;
            case "8":
                this.prep(ID);
                break;
            case "9":
                if (this.delete(ID)) {
                    return true;
                }
                break;
            case "10":
                return true;
        }
        return false;
    }

    /**
     * prompts a user to confirm that they wish to delete an event, then removes event from eventManager
     *
     * @param ID the Id of the event to be deleted
     * @return true if the event was deleted
     */
    private boolean delete(Integer ID) {
        System.out.println("Are you sure you want to delete this event?");
        String confirm = ioController.getAnswer("Please Enter y/n");
        if (confirm.equalsIgnoreCase("y")) {
            this.eventManager.remove(ID);
            return true;
        } else if (!confirm.equalsIgnoreCase("n")) {
            return this.delete(ID);
        }
        return false;
    }

    /**
     * prompts a user to enter a new start date and changes the event start date, setting the start time to 00:00
     * if it was previously null
     *
     * @param ID the id of the event to be changed
     */
    private void changeStartDate(Integer ID) {
        LocalDate newStart = ioController.getDate("Please Enter a New Start Date");
        if (this.eventManager.getStartTime(ID) == null) {
            this.eventManager.setStart(ID, LocalDateTime.of(newStart, LocalTime.of(0, 0)));
        } else {
            this.eventManager.setStart(ID, LocalDateTime.of(newStart, this.eventManager.getStartTime(ID)));
        }
    }

    /**
     * prompts a user to enter a new end date and changes the event end date
     *
     * @param ID the id of the event to be changed
     */
    private void changeEndDate(Integer ID) {
        LocalDate newEnd = ioController.getDate("Please Enter a New End Date");
        this.eventManager.setEnd(ID, LocalDateTime.of(newEnd, this.eventManager.getEndTime(ID)));
    }

    /**
     * promps a user to enter a new end time and changes the event end time
     *
     * @param ID the id of the event to be changed
     */
    private void changeEndTime(Integer ID) {
        LocalTime newEnd = ioController.getTime("Please Enter a New End Time");
        this.eventManager.setEnd(ID, LocalDateTime.of(this.eventManager.getEndDate(ID), newEnd));
    }

    /**
     * change the start time of the event, set the date to the same a the end date if it was previously null
     *
     * @param ID the id of the event to be changed
     */
    private void changeStartTime(Integer ID) {
        LocalTime newStart = ioController.getTime("Please Enter a New Start Time");
        if (this.eventManager.get(ID).getStartTime() == null) {
            this.eventManager.setStart(ID, LocalDateTime.of(this.eventManager.getEndDate(ID), newStart));
        } else {
            this.eventManager.setStart(ID, LocalDateTime.of(this.eventManager.getStartDate(ID), newStart));
        }
    }

    /**
     * prompts the user to enter a new description for the event. changes the description
     *
     * @param ID the id of the event to modify
     */
    private void changeDescription(Integer ID) {
        String description = ioController.getAnswer("Please Enter a Description for This Event");
        this.eventManager.setDescription(this.eventManager.get(ID), description);
    }

    /**
     * prompts the user to enter a new event name and changes the name of the event
     *
     * @param ID the id of the event to be changed
     */
    private void changeName(Integer ID) {
        String name = ioController.getAnswer("please Enter a New Name");
        this.eventManager.setName(this.eventManager.get(ID), name);
    }

    /**
     * runs <code>workSessionController.edit(ID, this.eventManager)</code>
     *
     * @param ID the id of the event to be edited
     * @see WorkSessionController#edit
     */
    private void prep(Integer ID){
        this.workSessionController.edit(ID, this.eventManager);
    }

    /**
     * prompts the user to choose an action for modifying the recursion of the event, then runs
     * <code>recursiveController.edit()</code>
     *
     * @param ID the id of the event to be modified
     * @see RecursionController#createNewRecursion(Event, EventManager, EventController)
     */
    private void recurse(Integer ID) {
        String nextStep = ioController.getAnswer("Enter 'Create' to create new recursion" +
                "'edit' to modify an existing one and 'delete' to remove all repetitions of this event");
        if (nextStep.equalsIgnoreCase("Create")) {
            this.recursionController.createNewRecursion(this.eventManager.get(ID), this.eventManager, this);
        }
        //TODO: add more options to delete or modify a recursion (now can only add).
    }
}
