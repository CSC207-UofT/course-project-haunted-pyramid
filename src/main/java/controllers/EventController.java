package controllers;

import entities.UserPreferences;
import entities.recursions.RecursiveEvent;
import gateways.IOSerializable;
import helpers.ControllerHelper;
import helpers.EventIDConverter;
import presenters.MenuStrategies.DisplayMenu;
import presenters.MenuStrategies.EventEditMenuContent;
import presenters.MenuStrategies.RecursionEditMenuContent;
import usecases.events.EventManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Controller for creating then editing, or editing selected individual events - change name, description
 * start time, end time, delete
 * passes to RecursiveController and WorkSessionController to edit recursion or prep of an individual event
 *
 * @author Sebin Im
 * @author Taite Cullen
 * @author Malik Lahlou
 * @see entities.Event
 * @see EventManager
 * @see RecursionController
 * @see WorkSessionController
 */
public class EventController {

    private final EventManager eventManager;
    private final RecursionController recursionController;
    private final WorkSessionController workSessionController;
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
    public EventController(boolean hasSavedData, IOSerializable ioSerializable, UserController userController,
                           WorkSessionController workSessionController) {
        this.workSessionController = workSessionController;
        if (hasSavedData) {
            this.eventManager =
                    new EventManager(ioSerializable.eventsReadFromSerializable().getOrDefault(userController.getCurrentUser(), new ArrayList<>()),
                            ioSerializable.recursiveEventsReadFromSerializable().getOrDefault(userController.getCurrentUser(), new HashMap <>()));
        } else {
            this.eventManager = new EventManager(new ArrayList<>());
        }
        this.eventManager.setUuidEventsMap(ioSerializable.eventsReadFromSerializable());
        this.eventManager.setUuidRecursiveEventsMap(ioSerializable.recursiveEventsReadFromSerializable());
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
    public EventController(boolean hasSavedData, IOSerializable ioSerializable, UserController userController) {
        if (hasSavedData) {
            this.eventManager =
                    new EventManager(ioSerializable.eventsReadFromSerializable().getOrDefault(userController.getCurrentUser(), new ArrayList<>()),
                            ioSerializable.recursiveEventsReadFromSerializable().getOrDefault(userController.getCurrentUser(), new HashMap <>()));
        } else {
            this.eventManager = new EventManager(new ArrayList<>());
        }
        this.eventManager.setUuidEventsMap(ioSerializable.eventsReadFromSerializable());
        this.eventManager.setUuidRecursiveEventsMap(ioSerializable.recursiveEventsReadFromSerializable());
        this.recursionController = new RecursionController();
        this.ioController = new IOController();
        this.workSessionController = new WorkSessionController(userController.getUserManager().getPreferences(
                userController.getCurrentUser()));
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
        this.edit(this.eventManager.getID(this.eventManager.addEvent(title, dateTime)));
    }

    /**
     * Allows the user to edit an event from a list of actions
     *
     * @param ID the ID of the event to be edited by the user
     * @see EventController#getAction
     */
    public void edit(UUID ID) {
        boolean save = false;
        if (this.eventManager.containsID(ID)) {
            while (!save) {
                DisplayMenu dm = new DisplayMenu();
                EventEditMenuContent content = new EventEditMenuContent(this.eventManager.get(ID));
                System.out.println(dm.displayMenu(content));
                String next = ioController.getAnswer("Enter the Number of the Action You would like to Perform");
                save = this.getAction(next, ID);
                this.workSessionController.refresh(eventManager);
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
    private boolean getAction(String command, UUID ID) {
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
                this.prep(ID);
                break;
            case "8":
                this.addToRecursion(ID);
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


    private UUID getRecursiveID(){
        EventIDConverter converter = new EventIDConverter(this.getEventManager());
        Map<Integer, UUID> eventIDMap = converter.getEventIDMap();
        String eventID = ioController.getAnswer("Enter the ID of an event in the recursion in which you " +
                "want to add your event to");
        ControllerHelper helper = new ControllerHelper();
        while (!(helper.isInteger(eventID) && eventIDMap.containsKey(Integer.valueOf(eventID)))) {
            eventID = ioController.getAnswer("Please type a valid ID");
        }
        UUID uuid = converter.getUUIDFromInt(Integer.parseInt(eventID));
        return uuid;
    }

    private void addToRecursion(UUID id) {
        this.eventManager.get(id).setRecursiveId(getRecursiveID());
        this.eventManager.addObserver(this.eventManager.getRepeatedEventManager());
        this.eventManager.getRepeatedEventManager().update("add", this.eventManager.get(id), this.eventManager);
        this.eventManager.removeWithoutUpdate(id);
        this.eventManager.removeObserver(this.eventManager.getRepeatedEventManager());

    }


    private boolean carryToRecursion(UUID ID){
        if(eventManager.get(ID).getRecursiveId() != null){
            String recurse = ioController.getAnswer("Do you want to carry this change to the recursion");
            return recurse.equalsIgnoreCase("y");
        }
        return false;
    }

    /**
     * prompts a user to confirm that they wish to delete an event, then removes event from eventManager
     *
     * @param ID the ID of the event to be deleted
     * @return true if the event was deleted
     */
    private boolean delete(UUID ID) {
        System.out.println("Are you sure you want to delete this event?");
        String confirm = ioController.getAnswer("Please Enter y/n");
        if (confirm.equalsIgnoreCase("y")) {
            if(carryToRecursion(ID)){
                this.eventManager.addObserver(this.eventManager.getRepeatedEventManager());
                this.eventManager.remove(ID);
                this.eventManager.removeObserver(this.eventManager.getRepeatedEventManager());
            }
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
    private void changeStartDate(UUID ID) {
        LocalDate newStart = ioController.getDate("Please Enter a New Start Date");
        if (this.eventManager.getStartTime(ID) == null) {
            if(carryToRecursion(ID)){
                this.eventManager.addObserver(this.eventManager.getRepeatedEventManager());
                this.eventManager.setStart(ID, LocalDateTime.of(newStart, LocalTime.of(0, 0)));
                this.eventManager.removeObserver(this.eventManager.getRepeatedEventManager());
            }
            else {
                this.eventManager.setStart(ID, LocalDateTime.of(newStart, LocalTime.of(0, 0)));
            }
        } else {
            if(carryToRecursion(ID)){
                this.eventManager.addObserver(this.eventManager.getRepeatedEventManager());
                this.eventManager.setStart(ID, LocalDateTime.of(newStart, this.eventManager.getStartTime(ID)));
                this.eventManager.removeObserver(this.eventManager.getRepeatedEventManager());
            }
            else {
                this.eventManager.setStart(ID, LocalDateTime.of(newStart, this.eventManager.getStartTime(ID)));
            }
        }
    }

    /**
     * prompts a user to enter a new end date and changes the event end date
     *
     * @param ID the id of the event to be changed
     */
    private void changeEndDate(UUID ID) {
        LocalDate newEnd = ioController.getDate("Please Enter a New End Date");
        if(carryToRecursion(ID)){
            this.eventManager.addObserver(this.eventManager.getRepeatedEventManager());
            this.eventManager.setEnd(ID, LocalDateTime.of(newEnd, this.eventManager.getEndTime(ID)));
            this.eventManager.removeObserver(this.eventManager.getRepeatedEventManager());
        }
        else {
            this.eventManager.setEnd(ID, LocalDateTime.of(newEnd, this.eventManager.getEndTime(ID)));
        }
    }

    /**
     * prompts a user to enter a new end time and changes the event end time
     *
     * @param ID the id of the event to be changed
     */
    private void changeEndTime(UUID ID) {
        LocalTime newEnd = ioController.getTime("Please Enter a New End Time");
        if(carryToRecursion(ID)){
            this.eventManager.addObserver(this.eventManager.getRepeatedEventManager());
            this.eventManager.setEnd(ID, LocalDateTime.of(this.eventManager.getEndDate(ID), newEnd));
            this.eventManager.removeObserver(this.eventManager.getRepeatedEventManager());
        }
        else{
            this.eventManager.setEnd(ID, LocalDateTime.of(this.eventManager.getEndDate(ID), newEnd));
        }
    }

    /**
     * change the start time of the event, set the date to the same as end date if it was previously null
     *
     * @param ID the id of the event to be changed
     */
    private void changeStartTime(UUID ID) {
        LocalTime newStart = ioController.getTime("Please Enter a New Start Time");
        if (this.eventManager.get(ID).getStartTime() == null) {
            if(carryToRecursion(ID)){
                this.eventManager.addObserver(this.eventManager.getRepeatedEventManager());
                this.eventManager.setStart(ID, LocalDateTime.of(this.eventManager.getEndDate(ID), newStart));
                this.eventManager.removeObserver(this.eventManager.getRepeatedEventManager());
            }
            else{
            this.eventManager.setStart(ID, LocalDateTime.of(this.eventManager.getEndDate(ID), newStart));
            }
        }
        else {
            if(carryToRecursion(ID)){
                this.eventManager.addObserver(this.eventManager.getRepeatedEventManager());
                this.eventManager.setStart(ID, LocalDateTime.of(this.eventManager.getStartDate(ID), newStart));
                this.eventManager.removeObserver(this.eventManager.getRepeatedEventManager());
            }
            else {
                this.eventManager.setStart(ID, LocalDateTime.of(this.eventManager.getStartDate(ID), newStart));
            }
        }
    }

    /**
     * prompts the user to enter a new description for the event. changes the description
     *
     * @param ID the id of the event to modify
     */
    private void changeDescription(UUID ID) {
        String description = ioController.getAnswer("Please Enter a Description for This Event");
        this.eventManager.setDescription(this.eventManager.get(ID), description);
    }

    /**
     * prompts the user to enter a new event name and changes the name of the event
     *
     * @param ID the id of the event to be changed
     */
    private void changeName(UUID ID) {
        String name = ioController.getAnswer("please Enter a New Name");
        this.eventManager.setName(this.eventManager.get(ID), name);
    }

    /**
     * runs <code>workSessionController.edit(ID, this.eventManager)</code>
     *
     * @param ID the id of the event to be edited
     * @see WorkSessionController#edit
     */
    private void prep(UUID ID) {
        this.workSessionController.edit(ID, eventManager);
    }

    public void update(UserPreferences userPreferences){
        this.workSessionController.refresh(userPreferences, eventManager);
    }
}
