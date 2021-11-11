package controllers;

import entities.Event;
import helpers.ControllerHelper;
import helpers.DateInfo;
import presenters.DisplayCalendar;
import presenters.DisplayCalendarFactory;
import presenters.DisplayMenu;
import presenters.MenuStrategies.CalendarTypeMenuContent;
import presenters.MenuStrategies.CalendarYearMonthMenuContent;
import presenters.MenuStrategies.MenuContent;
import usecases.CalendarManager;
import usecases.EventManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CalendarController {
    private final Scanner scanner = new Scanner(System.in);
    private final ControllerHelper helper = new ControllerHelper();

    public String showDefaultCalendar(EventManager eventManager){
        CalendarManager calendarManager = new CalendarManager();
        for (Event event : eventManager.getAllEventsFlatSplit()){
            calendarManager.addToCalendar(event);
        }
        DisplayCalendarFactory calendarFactory = new DisplayCalendarFactory(calendarManager);
        return calendarFactory.displayCurrentCalendarByType("Monthly").displayCalendar();
    }

    public void showCalendar(EventManager eventManager) {
        DisplayMenu displayMenu = new DisplayMenu();
        MenuContent calendarTypeMenu = new CalendarTypeMenuContent();
        boolean exit = false;
        while (!exit) {
            CalendarManager calendarManager = new CalendarManager();
            for (Event event : eventManager.getAllEventsFlatSplit()) {
                calendarManager.addToCalendar(event);
            }
            DisplayCalendarFactory calendarFactory = new DisplayCalendarFactory(calendarManager);
            String typeCalendar = getTypeInput(displayMenu, calendarTypeMenu);
            String dateInput = getCalendarDateInput(displayMenu);
            DateInfo dateInfo = new DateInfo();
            DisplayCalendar calendar = selectCalendar(calendarFactory, typeCalendar, dateInput, dateInfo);
            System.out.println(calendar.displayCalendar());
            System.out.println("Please choose the next action");
            exit = true;
            }
        }

    private DisplayCalendar selectCalendar(DisplayCalendarFactory calendarFactory, String typeCalendar,
                                           String dateInput, DateInfo dateInfo) {
        int year = dateInfo.getDateInfo(0).get(0);
        int month = dateInfo.getDateInfo(0).get(1);
        int numberOfDays = dateInfo.getDateInfo(0).get(2);
        switch (dateInput) {
            case "1":
                year = dateInfo.getDateInfo(3).get(0);
                month = dateInfo.getDateInfo(3).get(1);
                numberOfDays = dateInfo.getDateInfo(3).get(2);
                break;
            case "2":
                year = dateInfo.getDateInfo(2).get(0);
                month = dateInfo.getDateInfo(2).get(1);
                numberOfDays = dateInfo.getDateInfo(2).get(2);
                break;
            case "3":
                year = dateInfo.getDateInfo(1).get(0);
                month = dateInfo.getDateInfo(1).get(1);
                numberOfDays = dateInfo.getDateInfo(1).get(2);
                break;
            case "4":
                break;
            case "5":
                year = dateInfo.getDateInfo(-1).get(0);
                month = dateInfo.getDateInfo(-1).get(1);
                numberOfDays = dateInfo.getDateInfo(-1).get(2);
                break;
            case "6":
                year = dateInfo.getDateInfo(-2).get(0);
                month = dateInfo.getDateInfo(-2).get(1);
                numberOfDays = dateInfo.getDateInfo(-2).get(2);
                break;
            case "7":
                year = dateInfo.getDateInfo(-3).get(0);
                month = dateInfo.getDateInfo(-3).get(1);
                numberOfDays = dateInfo.getDateInfo(-3).get(2);
                break;
        }
        int date = 1;
        DisplayCalendar calendar;
        switch (typeCalendar) {
            case "1":
                calendar = calendarFactory.displaySpecificCalendarByType("monthly", year, month, date);
                break;
            case "2":
                date = getDateInput(numberOfDays);
                calendar = calendarFactory.displaySpecificCalendarByType("weekly", year, month, date);
                break;
            case "3":
                date = getDateInput(numberOfDays);
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



    private int getDateInput(int numberOfDays) {
        int date;
      
        System.out.println("Please type the date to view the calendar from");

        String dateStr = scanner.nextLine();
        List<Integer> tempIntList = new ArrayList<>();
        for (int i = 1; i <= numberOfDays; i++){
            tempIntList.add(i);
        }
        while (!helper.validOption(tempIntList).contains(dateStr)){
            System.out.println("Please type the valid date for the month");
            dateStr = scanner.nextLine();
        }
        date = Integer.parseInt(dateStr);
        return date;
    }


    public static void main(String[] args) {
        CalendarController calendarController = new CalendarController();
        EventManager eventManager = new EventManager();
        calendarController.showCalendar(eventManager);
    }
}
