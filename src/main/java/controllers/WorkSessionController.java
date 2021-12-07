package controllers;

import entities.Event;
import entities.UserPreferences;
import helpers.Constants;
import helpers.ControllerHelper;
import presenters.MenuStrategies.DisplayMenu;
import presenters.MenuStrategies.WorkSessionMenuContent;
import usecases.events.EventManager;
import usecases.events.worksessions.WorkSessionManager;
import usecases.events.worksessions.WorkSessionScheduler;
import usecases.events.worksessions.WorkSessionSchedulerBuilder;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Controller for setting up the work session
 * @author Seo Won Yi
 * @author Taite Cullen
 * @see EventController
 * @see WorkSessionScheduler
 */
public class WorkSessionController {
    private WorkSessionScheduler workSessionScheduler;
    private final IOController ioController;
    private final ControllerHelper helper;


    /**
     * Instantiate the workSessionController
     * @param userPreferences the user preferences on which to base auto scheduling
     */
    public WorkSessionController(UserPreferences userPreferences){
        WorkSessionSchedulerBuilder workSessionSchedulerBuilder = new WorkSessionSchedulerBuilder();
        this.workSessionScheduler = workSessionSchedulerBuilder.getWorkSessionScheduler(userPreferences);
        this.ioController = new IOController();
        this.helper = new ControllerHelper();
    }

    public void refresh(UserPreferences userPreferences, EventManager eventManager){
        WorkSessionSchedulerBuilder workSessionSchedulerBuilder = new WorkSessionSchedulerBuilder();
        this.workSessionScheduler = workSessionSchedulerBuilder.getWorkSessionScheduler(userPreferences);
        for (Event event: eventManager.getAllEvents()){
            this.workSessionScheduler.autoSchedule(eventManager.getID(event), eventManager);
        }
    }
    public void refresh(EventManager eventManager){
        for (Event event: eventManager.getAllEvents()){
            this.workSessionScheduler.autoSchedule(eventManager.getID(event), eventManager);
        }
    }

    /**
     * Confirm and perform necessary action from the user regarding modification of work session
     * @param eventID ID of the event to set/modify work session
     * @param eventManager EventManager object to bring necessary methods
     */
    public void edit(UUID eventID, EventManager eventManager) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        boolean done = false;
        while (!done) {
            DisplayMenu displayMenu = new DisplayMenu();
            if (workSessionManager.getTotalWorkSession(eventID).size() == 0) {
                System.out.println("There is no Work Session assigned for this Event");
                System.out.println("Please Set up the Work Session");
            } else {
                System.out.println("The following Work Sessions are assigned for this Event");
                if (workSessionManager.getPastWorkSession(eventID).size() == 0) {
                    System.out.println("There is no Past Work Sessions");
                }
                else {
                    System.out.println("Past: " + workSessionManager.getPastSessionsString(eventID));
                }
                if (workSessionManager.getFutureWorkSession(eventID).size() == 0) {
                    System.out.println("All Work Sessions were assigned for the Past.");
                    System.out.println("Please mark them to update or change Total Work Session Hours");
                }
                else {
                    System.out.println("Current: " + workSessionManager.getFutureSessionsString(eventID));
                }
                System.out.println("Current Session Length: " + workSessionManager.getEventSessionLength(eventID));
                System.out.println("Total Work Session Hours: " + workSessionManager.getTotalHoursNeeded(eventID));
            }
            System.out.println("Please choose your next action");
            done = finalChoice(eventID, eventManager, displayMenu);
        }
    }

    /**
     * Ask and confirm what the user wants to do with the work session related tasks
     * @param eventID ID of the event
     * @param eventManager eventManager that has event information
     * @param displayMenu DisplayMenu class to show the appropriate menu
     * @return perform the task, unless chosen to, do not return to the main menu
     */
    private boolean finalChoice(UUID eventID, EventManager eventManager, DisplayMenu displayMenu) {
        boolean done = false;
        WorkSessionMenuContent menu = new WorkSessionMenuContent();
        String choice = ioController.getAnswer(displayMenu.displayMenu(menu));
        choice = helper.invalidCheck(displayMenu, choice, menu.numberOfOptions(), menu);
        switch (choice) {
            case "1":
                markCompletion(eventID, eventManager);
                break;
            case "2":
                changeSessionLength(eventID, eventManager);
                break;
            case "3":
                changeTotalHour(eventID, eventManager);
                break;
            case "4":
                changeStartWorking(eventID, eventManager);
                break;
            case "5":
                done = true;
                break;
        }
        return done;
    }

    private void changeStartWorking(UUID eventID, EventManager eventManager) {
        LocalDate startWorking = ioController.getDate("please enter a date to start working on " +
                "this project");
        changeStartWorking(eventID, eventManager, startWorking);
    }

    public void changeStartWorking(UUID Id, EventManager eventManager, LocalDate startWorking){
        workSessionScheduler.changeStartWorking(Id, startWorking, eventManager);
    }

    public void changeStartWorking(UUID Id, EventManager eventManager, Long startWorking){
        workSessionScheduler.changeStartWorking(Id, startWorking, eventManager);
    }

    /**
     * Change total hour of the work session
     * @param eventID ID of the event to change from
     * @param eventManager eventManager object with the necessary function
     */
    private void changeTotalHour(UUID eventID, EventManager eventManager) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        System.out.println("Original Total Work Session Hour: " + workSessionManager.getTotalHoursNeeded(eventID));
        String chosenHour = ioController.getAnswer("Please type the new Total Hour (Max: 50)");
        chosenHour = helper.invalidCheckNoMenu(chosenHour, Constants.MAXIMUM_WORK_SESSION_HOUR,
                "Please type the valid Total Work Session Hour (Max: 50)");
        changeTotalHour(eventID, eventManager, Long.valueOf(chosenHour));
        System.out.println("The change has been applied");
    }

    public void changeTotalHour(UUID eventID, EventManager eventManager, Long chosenHour){
        this.workSessionScheduler.setHoursNeeded(eventID, chosenHour, eventManager);
    }

    /**
     * Change the individual session length
     * @param eventID ID of the event to change from
     * @param eventManager eventManager object with the necessary function
     */
    private void changeSessionLength(UUID eventID, EventManager eventManager) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        System.out.println("Original Length: " + workSessionManager.getEventSessionLength(eventID));
        String chosenLength = ioController.getAnswer("Please type new Session Length (Max: 10)");
        chosenLength = helper.invalidCheckNoMenu(chosenLength, Constants.MAXIMUM_SESSION_LENGTH,
                "Please type the valid Session Length (Max: 10");
        changeSessionLength(eventID, eventManager, Long.valueOf(chosenLength));
        System.out.println("The change has been applied");
    }

    public void changeSessionLength(UUID eventID, EventManager eventManager, Long chosenLength){
        this.workSessionScheduler.setSessionLength(eventID, chosenLength, eventManager);
    }

    /**
     * Mark the past session complete or incomplete
     * @param eventID ID of the event to change from
     * @param eventManager eventManager object with the necessary function
     */
    private void markCompletion(UUID eventID, EventManager eventManager) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        if (workSessionManager.getTotalWorkSession(eventID).size() == 0) {
            return;
        }
        String sessionNumber = ioController.getAnswer("Please type the session #");
        sessionNumber = helper.invalidCheckNoMenu(sessionNumber, workSessionManager.getTotalWorkSession(eventID).size(),
                "Please Choose the Valid Session # from the Past Sessions");
        System.out.println("Please type Complete to indicate the completion of the session");
        String marking = ioController.getAnswer("Otherwise, please type Incomplete to perform rescheduling");
        while (!marking.equalsIgnoreCase("complete") && !marking.equalsIgnoreCase("incomplete")){
            marking = ioController.getAnswer("Please type Complete or Incomplete to indicate the progress");
        }
        if (marking.equalsIgnoreCase("complete")){
            this.workSessionScheduler.markComplete(eventID, sessionNumber, eventManager);
            System.out.println("The session was marked Complete");
        }
        else if (marking.equalsIgnoreCase("incomplete")){
            this.workSessionScheduler.markInComplete(eventID, sessionNumber, eventManager);
            System.out.println("The session was marked Incomplete");
        }
    }

    public void markComplete(UUID event, UUID session, EventManager eventManager){
        workSessionScheduler.markComplete(event, session, eventManager);
    }

    public void markInComplete(UUID event, UUID session, EventManager eventManager){
        workSessionScheduler.markInComplete(event, session, eventManager);
    }

    public WorkSessionManager getWorkSessionManager(EventManager eventManager) {
        return new WorkSessionManager(eventManager);
    }

}
