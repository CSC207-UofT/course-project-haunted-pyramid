package presenters;

import usecases.CalendarManager;
import usecases.EventManager;

public class DisplayDailyCalendar extends DisplayCalendar {
    private int year;
    private int month;
    private int date;
    public DisplayDailyCalendar(CalendarManager cm, int year, int month, int date) {
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
