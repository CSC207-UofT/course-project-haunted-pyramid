package controllers;

import entities.Event;
import helpers.CalendarSelection;
import helpers.ControllerHelper;
import helpers.DateInfo;
import presenters.DisplayCalendar;
import presenters.DisplayCalendarFactory;
import presenters.DisplayMenu;
import presenters.MenuStrategies.CalNextActionMenuContent;
import presenters.MenuStrategies.CalendarTypeMenuContent;
import presenters.MenuStrategies.CalendarYearMonthMenuContent;
import presenters.MenuStrategies.MenuContent;
import usecases.calendar.CalendarManager;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Controller used for displaying calendar
 * @author Seo Won Yi
 */

public class CalendarController {
    private final Scanner scanner = new Scanner(System.in);
    private final ControllerHelper helper = new ControllerHelper();

    /**
     * the default current monthly calendar that will show every time the user returns to the main menu
     * @param eventController eventcontroller that stores the entire event information
     * @return the current monthly calendar image
     */
    public String showDefaultCalendar(EventController eventController){
        DisplayCalendarFactory calendarFactory = getDisplayCalendarFactory(eventController);
        return calendarFactory.displayCurrentCalendarByType("Monthly").displayCalendar();
    }

    /**
     * asks and confirms the inputs of necessary data such as type of calendar, year, month,
     * date to display the appropriate calendar
     * @param eventController event information needed to display on the calendar
     */
    public void showCalendar(EventController eventController) {
        System.out.println("You may type Return to return to the main menu at any time (except Date selection)");
        DisplayMenu displayMenu = new DisplayMenu();
        MenuContent calendarTypeMenu = new CalendarTypeMenuContent();
        DisplayCalendarFactory calendarFactory = getDisplayCalendarFactory(eventController);
        String typeCalendar = getTypeInput(displayMenu, calendarTypeMenu);
        if (typeCalendar.equalsIgnoreCase("Return")){
            return;
        }
        String dateInput = getCalendarDateInput(displayMenu, "view");
        if (dateInput.equalsIgnoreCase("Return")){
            return;
        }
        DateInfo dateInfo = new DateInfo();
        DisplayCalendar calendar = selectCalendar(calendarFactory, typeCalendar, dateInput, dateInfo);
        System.out.println(calendar.displayCalendar());
        finalAction(eventController, displayMenu);
    }

    /**
     * Confirms year/month/date information from the user and
     * show daily calendar for the selected date to direct the user to modification of an event by ID
     * @param eventController event information that needs to be displayed in the calendar
     */
    public void dailyCalendarForModification(EventController eventController){
        DisplayMenu displayMenu = new DisplayMenu();
        System.out.println("You may type Return to return to the main menu at any time (except Date selection)");
        String dateInput = getCalendarDateInput(displayMenu, "modify");
        CalendarSelection calendarSelection = new CalendarSelection(new DateInfo(), dateInput);
        int year = calendarSelection.getYear();
        int month = calendarSelection.getMonth();
        int numberOfDays = calendarSelection.getNumberOfDays();
        int date = getDateInput(numberOfDays, "event");
        DisplayCalendarFactory calendarFactory = getDisplayCalendarFactory(eventController);
        System.out.println(calendarFactory.displaySpecificCalendarByType("Daily",
                year, month, date).displayCalendar());
        String eventID = getEventID(eventController);
        if (eventID.equalsIgnoreCase("Return")) {
            return;
        }
        eventController.edit(Integer.parseInt(eventID));
    }

    /**
     * asks and get eventID after confirming the validity of it
     * @param eventController eventController object used to confirm the eventID's existence
     * @return the valid eventID that will be used for modification
     */
    private String getEventID(EventController eventController) {
        System.out.println("Please type the Event ID to access the Event or type Return to return to the main menu");
        String eventID = scanner.nextLine();
        if (eventID.equalsIgnoreCase("Return")){
            return "Return";
        }
        while (!(isInteger(eventID) && eventController.getEventManager().containsID(Integer.parseInt(eventID)))){
            System.out.println("Please type the valid ID");
            eventID = scanner.nextLine();
        }
        return eventID;
    }

    /**
     * Helper method that initiates DisplayCalendarFactory with the given event information
     * @param eventController the event information provided
     * @return displayCalendarFactory with the calendarManager that contains event information from eventController
     */
    private DisplayCalendarFactory getDisplayCalendarFactory(EventController eventController) {
        CalendarManager calendarManager = new CalendarManager();
        for (Event event : eventController.getEventManager().getAllEventsFlatSplit()) {
            calendarManager.addToCalendar(event);
        }
        return new DisplayCalendarFactory(calendarManager);
    }

    /**
     * choose the action at the end of the calendar display. shows appropriate menu and leads to the next phase
     * @param eventController eventController that can be used as a next phase
     * @param displayMenu display menu object to show the menu when needed
     */
    private void finalAction(EventController eventController, DisplayMenu displayMenu) {
        System.out.println("Please choose the next action");
        MenuContent calNextActionMenu = new CalNextActionMenuContent();
        System.out.println(displayMenu.displayMenu(calNextActionMenu));
        String finalAction = scanner.nextLine();
        if (finalAction.equalsIgnoreCase("return")){
            return;
        }
        helper.invalidCheck(displayMenu, finalAction, calNextActionMenu.numberOfOptions(), calNextActionMenu);
        switch (finalAction) {
            case "1":
                showCalendar(eventController);
                break;
            case "2":
                dailyCalendarForModification(eventController);
                break;
            case "3":
                eventController.createDefaultEvent();
                break;
            case "4":
                break;
        }
    }

    /**
     * asks and confirms the  date (if necessary) information and uses other necessary information such as the type
     * of the calendar or year, month from the parameter to create the appropriate calendar object.
     * @param calendarFactory DisplayCalendarFactory object used to create the image of the calendar
     * @param typeCalendar type of the calendar chosen
     * @param dateInput choice made by the user previous to determine the year/month of the calendar to show
     * @param dateInfo DateInfo object needed to run the helper method CalendarSelection which provides the year, month
     *                 information
     * @return the DisplayCalendar object which has a method to show the image of the calendar.
     */
    private DisplayCalendar selectCalendar(DisplayCalendarFactory calendarFactory, String typeCalendar,
                                           String dateInput, DateInfo dateInfo) {
        CalendarSelection calendarSelection = new CalendarSelection(dateInfo, dateInput);
        int year = calendarSelection.getYear();
        int month = calendarSelection.getMonth();
        int numberOfDays = calendarSelection.getNumberOfDays();
        int date = 1;
        DisplayCalendar calendar;
        switch (typeCalendar) {
            case "1":
                calendar = calendarFactory.displaySpecificCalendarByType("monthly", year, month, date);
                break;
            case "2":
                date = getDateInput(numberOfDays, "calendar");
                calendar = calendarFactory.displaySpecificCalendarByType("weekly", year, month, date);
                break;
            case "3":
                date = getDateInput(numberOfDays, "calendar");
                calendar = calendarFactory.displaySpecificCalendarByType("daily", year, month, date);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + typeCalendar);
        }
        return calendar;
    }

    /**
     * Asks and confirms the year and month input from the user
     * @param displayMenu display menu object used to show the menu
     * @param question decide an appropriate question message
     * @return the year and month choice made by the user
     */
    private String getCalendarDateInput(DisplayMenu displayMenu, String question) {
        if (question.equalsIgnoreCase("View")) {
            System.out.println("Please select the Year and Month that you would like to view the calendar from");
        }
        else if (question.equalsIgnoreCase("Modify")){
            System.out.println("Choose the Year/Month that you would like to modify the Event from");
        }
        MenuContent calendarDateInfoMenu = new CalendarYearMonthMenuContent();
        System.out.println(displayMenu.displayMenu(calendarDateInfoMenu));
        String dateInput = scanner.nextLine();
        helper.invalidCheck(displayMenu, dateInput, calendarDateInfoMenu.numberOfOptions(), calendarDateInfoMenu);
        return dateInput;
    }

    /**
     * Asks and confirms the user input for the calendar type
     * @param displayMenu display menu object used to show the menu
     * @param calendarTypeMenu the type of the menu that asks calendar type
     * @return the choice that the user made
     */
    private String getTypeInput(DisplayMenu displayMenu, MenuContent calendarTypeMenu) {
        System.out.println("Please select the type of calendar you would like to view");
        System.out.println(displayMenu.displayMenu(calendarTypeMenu));
        String typeCalendar = scanner.nextLine();
        if (typeCalendar.equalsIgnoreCase("Return")){
            return "Return";
        }
        typeCalendar = helper.invalidCheck(displayMenu, typeCalendar, calendarTypeMenu.numberOfOptions(), calendarTypeMenu);
        return typeCalendar;
    }


    /**
     * depending on the question parameter outputs different questions. Confirms the date input and its validity
     * output the date if valid
     * @param numberOfDays number of days in the chosen month for validation purpose
     * @param question type of question to display
     * @return the confirmed date
     */
    private int getDateInput(int numberOfDays, String question) {
        int date;
        if (question.equalsIgnoreCase("calendar")) {
            System.out.println("Please type the date to view the calendar from");
        }
        else if (question.equalsIgnoreCase("event")){
            System.out.println("Please type the date to modify the Event from");
        }
        String dateStr = scanner.nextLine();
        List<Integer> tempIntList = new ArrayList<>();
        for (int i = 1; i <= numberOfDays; i++){
            tempIntList.add(i);
        }
        while (!helper.validOption(tempIntList).contains(dateStr)){
            System.out.println("Please type the valid date of the month");
            dateStr = scanner.nextLine();
        }
        date = Integer.parseInt(dateStr);
        return date;
    }

    /**
     * confirm if the string is all numerical
     * @param str the string to check
     * @return true if the string is numerical otherwise false
     */
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

}
