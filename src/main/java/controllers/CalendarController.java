package controllers;

import usecases.CalendarManager;

public class CalendarController {

    private final CalendarManager calendarManager;

    public CalendarController() {
        this.calendarManager = new CalendarManager();
    }

    public CalendarManager getCalendarManager() {
        return calendarManager;
    }
}
