package presenters;

import usecases.CalendarManager;

public class DisplayWeeklyCalendar extends DisplayCalendar{
    private int year;
    private int month;
    private int date;
    public DisplayWeeklyCalendar(CalendarManager cm, int year, int month, int date) {
        super(cm);
        this.year = year;
        this.month = month;
        this.date = date;
    }

    @Override
    public String displayCalendar() {
        return "test";
    }
}
