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

public class CalendarController {
    private final Scanner scanner = new Scanner(System.in);
    private final ControllerHelper helper = new ControllerHelper();

    public String showDefaultCalendar(EventController eventController){
        DisplayCalendarFactory calendarFactory = getDisplayCalendarFactory(eventController);
        return calendarFactory.displayCurrentCalendarByType("Monthly").displayCalendar();
    }

    public void showCalendar(EventController eventController) {
        System.out.println("You may type Return to return to the main menu at any time");
        DisplayMenu displayMenu = new DisplayMenu();
        MenuContent calendarTypeMenu = new CalendarTypeMenuContent();
        DisplayCalendarFactory calendarFactory = getDisplayCalendarFactory(eventController);
        String typeCalendar = getTypeInput(displayMenu, calendarTypeMenu);
        if (typeCalendar.equalsIgnoreCase("Return")){
            return;
        }
        String dateInput = getCalendarDateInput(displayMenu);
        if (dateInput.equalsIgnoreCase("Return")){
            return;
        }
        DateInfo dateInfo = new DateInfo();
        DisplayCalendar calendar = selectCalendar(calendarFactory, typeCalendar, dateInput, dateInfo);
        System.out.println(calendar.displayCalendar());
        finalAction(eventController, displayMenu);
    }

    public void dailyCalendarForModification(EventController eventController){
        DisplayMenu displayMenu = new DisplayMenu();
        System.out.println("You may type Return to return to the main menu at any time");
        System.out.println("Choose the Year/Month that you would like to modify the Event from");
        String dateInput = getCalendarDateInput(displayMenu);
        CalendarSelection calendarSelection = new CalendarSelection(new DateInfo(), dateInput);
        int year = calendarSelection.getYear();
        int month = calendarSelection.getMonth();
        int numberOfDays = calendarSelection.getNumberOfDays();
        int date = getDateInput(numberOfDays, "event");
        if (date == 0){
            return;
        }
        DisplayCalendarFactory calendarFactory = getDisplayCalendarFactory(eventController);
        System.out.println(calendarFactory.displaySpecificCalendarByType("Daily",
                year, month, date).displayCalendar());
        System.out.println("Please type the Event ID to access the Event or type Return to return to the main menu");
        String eventID = scanner.nextLine();
        if (eventID.equalsIgnoreCase("return")){
            return;
        }
        while (!(isInteger(eventID) && eventController.getEventManager().containsID(Integer.parseInt(eventID)))){
            System.out.println("Please type the valid ID");
            eventID = scanner.nextLine();
        }
        eventController.edit(Integer.parseInt(eventID));
    }

    private DisplayCalendarFactory getDisplayCalendarFactory(EventController eventController) {
        CalendarManager calendarManager = new CalendarManager();
        for (Event event : eventController.getEventManager().getAllEventsFlatSplit()) {
            calendarManager.addToCalendar(event);
        }
        return new DisplayCalendarFactory(calendarManager);
    }

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

    private String getCalendarDateInput(DisplayMenu displayMenu) {
        System.out.println("Please select the Year and Month that you would like to view the calendar from");
        MenuContent calendarDateInfoMenu = new CalendarYearMonthMenuContent();
        System.out.println(displayMenu.displayMenu(calendarDateInfoMenu));
        String dateInput = scanner.nextLine();
        helper.invalidCheck(displayMenu, dateInput, calendarDateInfoMenu.numberOfOptions(), calendarDateInfoMenu);
        return dateInput;
    }

    private String getTypeInput(DisplayMenu displayMenu, MenuContent calendarTypeMenu) {
        System.out.println("Please select the type of calendar you would like to view");
        System.out.println(displayMenu.displayMenu(calendarTypeMenu));
        String typeCalendar = scanner.nextLine();
        typeCalendar = helper.invalidCheck(displayMenu, typeCalendar, calendarTypeMenu.numberOfOptions(), calendarTypeMenu);
        return typeCalendar;
    }



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


    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

}
