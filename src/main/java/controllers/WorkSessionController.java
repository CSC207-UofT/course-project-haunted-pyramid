package controllers;

import helpers.Constants;
import helpers.ControllerHelper;
import presenters.DisplayMenu;
import presenters.MenuStrategies.WorkSessionMenuContent;
import usecases.events.EventManager;
import usecases.events.worksessions.WorkSessionScheduler;

/**
 * Controller for setting up the work session
 * @author Seo Won Yi
 * @author Taite Cullen
 * @see EventController
 * @see WorkSessionScheduler
 */
public class WorkSessionController {
    private final WorkSessionScheduler workSessionScheduler;
    private final IOController ioController;
    private final ControllerHelper helper;

    /**
     * Instantiate the workSessionController
     * @param workSessionScheduler workSessionScheduler class to be used as a base
     */
    public WorkSessionController(WorkSessionScheduler workSessionScheduler){
        this.workSessionScheduler = workSessionScheduler;
        this.ioController = new IOController();
        this.helper = new ControllerHelper();
    }

    public WorkSessionScheduler getWorkSessionScheduler(){
        return this.workSessionScheduler;
    }

    /**
     * Confirm and perform necessary action from the user regarding modification of work session
     * @param eventID ID of the event to set/modify work session
     * @param eventManager EventManager object to bring necessary methods
     */
    public void edit(Integer eventID, EventManager eventManager) {
        boolean done = false;
        while (!done) {
            DisplayMenu displayMenu = new DisplayMenu();
            if (eventManager.getTotalWorkSession(eventID).size() == 0) {
                System.out.println("There is no Work Session assigned for this Event");
                System.out.println("Please Set up the Work Session");
            } else {
                System.out.println("The following Work Sessions are assigned for this Event");
                if (eventManager.getPastWorkSession(eventID).size() == 0) {
                    System.out.println("There is no Past Work Sessions");
                }
                else {
                    System.out.println("Past: " + eventManager.getPastWorkSession(eventID));
                }
                if (eventManager.getFutureWorkSession(eventID).size() == 0) {
                    System.out.println("All Work Sessions were assigned for the Past.");
                    System.out.println("Please mark them to update or change Total Work Session Hours");
                }
                else {
                    System.out.println("Current: " + eventManager.getFutureWorkSession(eventID));
                }
                System.out.println("Current Session Length: " + eventManager.getEventSessionLength(eventID));
                System.out.println("Total Work Session Hours: " + eventManager.getTotalHoursNeeded(eventID));
            }
            System.out.println("Please choose your next action");
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
                    done = true;
                    break;
            }
        }
    }

    /**
     * Change total hour of the work session
     * @param eventID ID of the event to change from
     * @param eventManager eventManager object with the necessary function
     */
    private void changeTotalHour(Integer eventID, EventManager eventManager) {
        System.out.println("Original Total Work Session Hour: " + eventManager.getTotalHoursNeeded(eventID));
        String chosenHour = ioController.getAnswer("Please type the new Total Hour (Max: 50)");
        chosenHour = helper.invalidCheckNoMenu(chosenHour, Constants.MAXIMUM_WORK_SESSION_HOUR,
                "Please type the valid Total Work Session Hour (Max: 50)");
        this.workSessionScheduler.setHoursNeeded(eventManager.get(eventID), Long.valueOf(chosenHour), eventManager);
        System.out.println("The change has been applied");
    }

    /**
     * Change the individual session length
     * @param eventID ID of the event to change from
     * @param eventManager eventManager object with the necessary function
     */
    private void changeSessionLength(Integer eventID, EventManager eventManager) {
        System.out.println("Original Length: " + eventManager.getEventSessionLength(eventID));
        String chosenLength = ioController.getAnswer("Please type new Session Length (Max: 10)");
        chosenLength = helper.invalidCheckNoMenu(chosenLength, Constants.MAXIMUM_SESSION_LENGTH,
                "Please type the valid Session Length (Max: 10");
        this.workSessionScheduler.setSessionLength(eventManager.get(eventID), Long.valueOf(chosenLength), eventManager);
        System.out.println("The change has been applied");
    }

    /**
     * Mark the past session complete or incomplete
     * @param eventID ID of the event to change from
     * @param eventManager eventManager object with the necessary function
     */
    private void markCompletion(Integer eventID, EventManager eventManager) {
        if (eventManager.getPastWorkSession(eventID).size() == 0) {
            return;
        }
        String sessionNumber = ioController.getAnswer("Please type the session #");
        sessionNumber = helper.invalidCheckNoMenu(sessionNumber, eventManager.getPastWorkSession(eventID).size(),
                "Please Choose the Valid Session # from the Past Sessions");
        System.out.println("Please type Complete to indicate the completion of the session");
        String marking = ioController.getAnswer("Otherwise, please type Incomplete to perform rescheduling");
        while (!marking.equalsIgnoreCase("complete") && !marking.equalsIgnoreCase("incomplete")){
            marking = ioController.getAnswer("Please type Complete or Incomplete to indicate the progress");
        }
        if (marking.equalsIgnoreCase("complete")){
            this.workSessionScheduler.markComplete(eventManager.get(eventID), sessionNumber, eventManager);
            System.out.println("The session was marked Complete");
        }
        else if (marking.equalsIgnoreCase("incomplete")){
            this.workSessionScheduler.markInComplete(eventManager.get(eventID), sessionNumber, eventManager);
            System.out.println("The session was marked Incomplete");
        }
    }

}
