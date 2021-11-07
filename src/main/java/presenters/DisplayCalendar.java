package presenters;

import usecases.CalendarManager;

import java.time.DayOfWeek;
import java.time.LocalDate;

public abstract class DisplayCalendar {
    public CalendarManager cm;

    public DisplayCalendar(CalendarManager cm){
        this.cm = cm;
    }
    public abstract String displayCalendar();
}
