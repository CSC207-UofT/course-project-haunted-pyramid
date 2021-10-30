package presenters;

import usecases.CalendarManager;
import usecases.EventManager;

public abstract class DisplayCalendar {
    public CalendarManager cm;

    public DisplayCalendar(CalendarManager cm){
        this.cm = cm;
    }
    public abstract String displayCalendar();
}
