package presenters;

import usecases.calendar.CalendarManager;


public abstract class DisplayCalendar {
    public CalendarManager cm;

    public DisplayCalendar(CalendarManager cm){
        this.cm = cm;
    }
    public abstract String displayCalendar();
}
