package controllers;

import gateways.ICalendar;
import helpers.CalendarSelection;
import helpers.ControllerHelper;
import helpers.DateInfo;
import helpers.EventIDConverter;
import presenters.CalendarFactory.CalendarDisplay;
import presenters.CalendarFactory.CalendarDisplayFactory;
import presenters.MenuStrategies.DisplayMenu;
import presenters.MenuStrategies.CalNextActionMenuContent;
import presenters.MenuStrategies.CalendarTypeMenuContent;
import presenters.MenuStrategies.CalendarYearMonthMenuContent;
import interfaces.MenuContent;
import usecases.EventCalendarCollaborator;
import usecases.calendar.CalendarByType;
import usecases.calendar.CalendarManager;
import usecases.events.EventManager;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller used for displaying calendar
 * @author Seo Won Yi
 * @see CalendarDisplayFactory
 * @see CalendarDisplay
 * @see CalendarManager
 * @see CalendarByType
 * @see entities.OurCalendar
 */

public class CalendarController {
    private final Scanner scanner = new Scanner(System.in);
    private final ControllerHelper helper = new ControllerHelper();
    private final RecursionController recursionController = new RecursionController();

    /**
     * the default current monthly calendar that will show every time the user returns to the main menu
     *
     * @param eventController eventController that stores the entire event information
     * @return the current monthly calendar image
     */
    public String showDefaultCalendar(EventController eventController) {
        CalendarDisplayFactory calendarFactory = getDisplayCalendarFactory(eventController);
        return calendarFactory.displayCurrentCalendarByType("Monthly").displayCalendar();
    }

    /**
     * asks and confirms the inputs of necessary data such as type of calendar, year, month,
     * date to display the appropriate calendar
     *
     * @param eventController event information needed to display on the calendar
     */
    public void showCalendar(EventController eventController) {
        System.out.println("You may type Return to return to the main menu at any time (except Date selection)");
        DisplayMenu displayMenu = new DisplayMenu();
        MenuContent calendarTypeMenu = new CalendarTypeMenuContent();
        CalendarDisplayFactory calendarFactory = getDisplayCalendarFactory(eventController);
        String typeCalendar = getTypeInput(displayMenu, calendarTypeMenu);
        if (typeCalendar.equalsIgnoreCase("Return")) {
            return;
        }
        String dateInput = getCalendarDateInput(displayMenu, "view");
        if (dateInput.equalsIgnoreCase("Return")) {
            return;
        }
        DateInfo dateInfo = new DateInfo();
        CalendarDisplay calendar = selectCalendar(calendarFactory, typeCalendar, dateInput, dateInfo);
        System.out.println(calendar.displayCalendar());
        finalAction(eventController, displayMenu, calendar);
    }

    /**
     * Confirms year/month/date information from the user and
     * show daily calendar for the selected date to direct the user to modification of an event by ID
     *
     * @param eventController event information that needs to be displayed on the calendar
     */
    public void dailyCalendarForModification(EventController eventController) {
        DisplayMenu displayMenu = new DisplayMenu();
        EventIDConverter converter = new EventIDConverter(eventController.getEventManager());
        System.out.println("You may type Return to return to the main menu at any time (except Date selection)");
        String dateInput = getCalendarDateInput(displayMenu, "modify");
        if (dateInput.equalsIgnoreCase("Return")) {
            return;
        }
        CalendarSelection calendarSelection = new CalendarSelection(new DateInfo(), dateInput);
        int year = calendarSelection.getYear();
        int month = calendarSelection.getMonth();
        int numberOfDays = calendarSelection.getNumberOfDays();
        int date = getDateInput(numberOfDays, "event");
        CalendarDisplayFactory calendarFactory = getDisplayCalendarFactory(eventController);
        System.out.println(calendarFactory.displaySpecificCalendarByType("Daily",
                year, month, date).displayCalendar());
        String eventID = getEventID(eventController);
        if (eventID.equalsIgnoreCase("Return")) {
            return;
        }
        eventController.edit(converter.getUUIDFromInt(Integer.parseInt(eventID)));
    }

    /**
     * Confirms year/month information from the user and
     * Show monthly calendar for the selected month to direct the user to set up the recursion of events by ID
     * @param eventController event information needs to be displayed on the calendar
     */
    public void monthlyCalendarForRepetition(EventController eventController) {
        DisplayMenu displayMenu = new DisplayMenu();
        System.out.println("You may type Return to return to the main menu at any time (except Date selection)");
        String dateInput = getCalendarDateInput(displayMenu, "repeat");
        if (dateInput.equalsIgnoreCase("Return")) {
            return;
        }
        CalendarSelection calendarSelection = new CalendarSelection(new DateInfo(), dateInput);
        int year = calendarSelection.getYear();
        int month = calendarSelection.getMonth();
        CalendarDisplayFactory calendarFactory = getDisplayCalendarFactory(eventController);
        System.out.println(calendarFactory.displaySpecificCalendarByType("Monthly",
                year, month, 1).displayCalendar());
        List<UUID> eventIDList = getEventIDList(eventController);
        if (eventIDList.size() == 0) {
            return;
        }
        recursionController.createNewRecursion(eventIDList, eventController.getEventManager());
    }

    /**
     * Get list of event ID via confirming with the user
     * @param eventController EventController object that has event information
     * @return list of eventID
     */
    private List<UUID> getEventIDList(EventController eventController){
        EventIDConverter converter = new EventIDConverter(eventController.getEventManager());
        List<UUID> eventIDList = new ArrayList<>();
        boolean check = false;
        String eventID = getEventID(eventController);
        if (eventID.equalsIgnoreCase("Return")) {
            return new ArrayList<>();
        }
        eventIDList.add(converter.getUUIDFromInt(Integer.parseInt(eventID)));
        while (!check){
            System.out.println("Would you like add another Events in this repetition?");
            System.out.println("y/n");
            String answer = scanner.nextLine();
            while (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("n")) {
                System.out.println("Please type the valid input");
                System.out.println("Would you like to Recurse more Events?");
                System.out.println("y/n");
                answer = scanner.nextLine();
            }
            if (answer.equalsIgnoreCase("n")) {
                check = true;
            }
            else {
                String eventID2 = getEventID(eventController);
                if (eventID2.equalsIgnoreCase("Return")) {
                    return eventIDList;
                }
                else if (!eventIDList.contains(converter.getUUIDFromInt(Integer.parseInt(eventID2)))) {
                    eventIDList.add(converter.getUUIDFromInt(Integer.parseInt(eventID2)));
                }
            }
        }
        return eventIDList;
    }

    /**
     * asks and get eventID after confirming the validity of it
     *
     * @param eventController eventController object used to confirm the eventID's existence
     * @return the valid eventID that will be used for modification
     */
    private String getEventID(EventController eventController) {
        EventManager eventManager = eventController.getEventManager();
        EventIDConverter converter = new EventIDConverter(eventManager);
        Map<Integer, UUID> eventIDMap = converter.getEventIDMap();
        System.out.println("Please type the Event ID that applies or type Return to return to end the task");
        String eventID = scanner.nextLine();
        if (eventID.equalsIgnoreCase("Return")) {
            return "Return";
        }
        while (!(helper.isInteger(eventID) && eventIDMap.containsKey(Integer.valueOf(eventID)))) {
            System.out.println("Please type the valid ID");
            eventID = scanner.nextLine();
            if (eventID.equalsIgnoreCase("Return")) {
                return "Return";
            }
        }
        return eventID;
    }

    /**
     * Helper method that initiates DisplayCalendarFactory with the given event information
     *
     * @param eventController the event information provided
     * @return displayCalendarFactory with the calendarManager that contains event information from eventController
     */
    private CalendarDisplayFactory getDisplayCalendarFactory(EventController eventController) {
        CalendarManager calendarManager = new CalendarManager();
        EventManager eventManager = eventController.getEventManager();
        EventCalendarCollaborator collaborator = new EventCalendarCollaborator(eventManager, calendarManager);
        collaborator.addAllEvents();
        calendarManager = collaborator.getCalendarManager();
        return new CalendarDisplayFactory(calendarManager, eventManager);
    }

    /**
     * choose the action at the end of the calendar display. shows appropriate menu and leads to the next phase
     *
     * @param eventController eventController that can be used as a next phase
     * @param displayMenu     display menu object to show the menu when needed
     */
    private void finalAction(EventController eventController, DisplayMenu displayMenu, CalendarDisplay calendar) {
        System.out.println("Please choose the next action");
        MenuContent calNextActionMenu = new CalNextActionMenuContent();
        System.out.println(displayMenu.displayMenu(calNextActionMenu));
        String finalAction = scanner.nextLine();
        if (finalAction.equalsIgnoreCase("return")) {
            return;
        }
        finalAction = helper.invalidCheck(displayMenu, finalAction, calNextActionMenu.numberOfOptions(), calNextActionMenu);
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
                monthlyCalendarForRepetition(eventController);
            case "5":
                exportIniCalFormat(eventController, calendar);
                break;
            case "6":
                break;
        }
    }

    /**
     * Using the information provided from calendar and eventController, create appropriate iCal file
     * @param eventController eventController that contains event information
     * @param calendar calendar that contains calendar information
     */
    private void exportIniCalFormat(EventController eventController, CalendarDisplay calendar) {
        ICalendar iCalendar = new ICalendar(eventController.getEventManager());
        List<Integer> yearMonthDate = new ArrayList<>();
        yearMonthDate.add(calendar.getYear());
        yearMonthDate.add(calendar.getMonth());
        yearMonthDate.add(calendar.getDate());
        String option;
        if (calendar.size() == 1) {
            option = "DAILY";
        }
        else if (calendar.size() == 7) {
            option = "WEEKLY";
        }
        else {
            option = "MONTHLY";
        }
        System.out.println("The file will be created in the same folder as the project");
        System.out.println("Please type your file name (a-Z, 0-9, -, _, . are allowed");
        String fileName = getFileName();
        iCalendar.create(fileName, option, yearMonthDate);
    }

    /**
     * Get valid file name (a file name is valid if it contains a-Z, 0-9, -, _, or .)
     * @return valid file name
     */
    public String getFileName() {
        Pattern invalidFileName = Pattern.compile("^[-_.A-Za-z0-9]+$");
        String response = scanner.nextLine();
        Matcher matcher = invalidFileName.matcher(response);
        while (!matcher.matches()) {
            System.out.println("Please type valid file name (characters of a-Z, 0-9, -, _, . are allowed)");
            response = scanner.nextLine();
            matcher = invalidFileName.matcher(response);
        }
        return response;
    }

    /**
     * asks and confirms the  date (if necessary) information and uses other necessary information such as the type
     * of the calendar or year, month from the parameter to create the appropriate calendar object.
     *
     * @param calendarFactory DisplayCalendarFactory object used to create the image of the calendar
     * @param typeCalendar    type of the calendar chosen
     * @param dateInput       choice made by the user previous to determine the year/month of the calendar to show
     * @param dateInfo        DateInfo object needed to run the helper method CalendarSelection which provides the year, month
     *                        information
     * @return the DisplayCalendar object which has a method to show the image of the calendar.
     */
    private CalendarDisplay selectCalendar(CalendarDisplayFactory calendarFactory, String typeCalendar,
                                           String dateInput, DateInfo dateInfo) {
        CalendarSelection calendarSelection = new CalendarSelection(dateInfo, dateInput);
        int year = calendarSelection.getYear();
        int month = calendarSelection.getMonth();
        int numberOfDays = calendarSelection.getNumberOfDays();
        int date = 1;
        CalendarDisplay calendar;
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
     *
     * @param displayMenu display menu object used to show the menu
     * @param question    decide an appropriate question message
     * @return the year and month choice made by the user
     */
    private String getCalendarDateInput(DisplayMenu displayMenu, String question) {
        if (question.equalsIgnoreCase("View")) {
            System.out.println("Please select the Year and Month that you would like to view the calendar from");
        } else if (question.equalsIgnoreCase("Modify")) {
            System.out.println("Choose the Year/Month that you would like to modify the Event from");
        }
        else if (question.equalsIgnoreCase("Repeat")) {
            System.out.println("Choose the Year/Month of the Event you would like to repeat");
        }
        MenuContent calendarDateInfoMenu = new CalendarYearMonthMenuContent();
        System.out.println(displayMenu.displayMenu(calendarDateInfoMenu));
        String dateInput = scanner.nextLine();
        helper.invalidCheck(displayMenu, dateInput, calendarDateInfoMenu.numberOfOptions(), calendarDateInfoMenu);
        return dateInput;
    }

    /**
     * Asks and confirms the user input for the calendar type
     *
     * @param displayMenu      display menu object used to show the menu
     * @param calendarTypeMenu the type of the menu that asks calendar type
     * @return the choice that the user made
     */
    private String getTypeInput(DisplayMenu displayMenu, MenuContent calendarTypeMenu) {
        System.out.println("Please select the type of calendar you would like to view");
        System.out.println(displayMenu.displayMenu(calendarTypeMenu));
        String typeCalendar = scanner.nextLine();
        if (typeCalendar.equalsIgnoreCase("Return")) {
            return "Return";
        }
        typeCalendar = helper.invalidCheck(displayMenu, typeCalendar, calendarTypeMenu.numberOfOptions(), calendarTypeMenu);
        return typeCalendar;
    }


    /**
     * depending on the question parameter outputs different questions. Confirms the date input and its validity
     * output the date if valid
     *
     * @param numberOfDays number of days in the chosen month for validation purpose
     * @param question     type of question to display
     * @return the confirmed date
     */
    private int getDateInput(int numberOfDays, String question) {
        int date;
        if (question.equalsIgnoreCase("calendar")) {
            System.out.println("Please type the date to view the calendar from");
        } else if (question.equalsIgnoreCase("event")) {
            System.out.println("Please type the date to modify the Event from");
        }
        String dateStr = scanner.nextLine();
        List<Integer> tempIntList = new ArrayList<>();
        for (int i = 1; i <= numberOfDays; i++) {
            tempIntList.add(i);
        }
        while (!helper.validOption(tempIntList).contains(dateStr)) {
            System.out.println("Please type the valid date of the month");
            dateStr = scanner.nextLine();
        }
        date = Integer.parseInt(dateStr);
        return date;
    }


}
