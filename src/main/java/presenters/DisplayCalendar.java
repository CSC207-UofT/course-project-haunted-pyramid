package presenters;

import usecases.CalendarManager;

public abstract class DisplayCalendar {
    public CalendarManager cm;

    public DisplayCalendar(CalendarManager cm){
        this.cm = cm;
    }
    protected abstract String displayCalendar();
}
