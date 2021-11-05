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

    public String findStartDayOfWeek(int year, int month, int date){
        LocalDate localDate = LocalDate.of(year, month, date);
        DayOfWeek dayOfWeek = DayOfWeek.from(localDate);
        int startingDayOfWeek = dayOfWeek.getValue();
        String chosenDayOfWeek = "Monday";
        switch (startingDayOfWeek) {
            case 1:
                chosenDayOfWeek = "MONDAY";
                break;
            case 2:
                chosenDayOfWeek = "TUESDAY";
                break;
            case 3:
                chosenDayOfWeek = "WEDNESDAY";
                break;
            case 4:
                chosenDayOfWeek = "THURSDAY";
                break;
            case 5:
                chosenDayOfWeek = "FRIDAY";
                break;
            case 6:
                chosenDayOfWeek = "SATURDAY";
                break;
            case 7:
                chosenDayOfWeek = "SUNDAY";
                break;
        }
        return chosenDayOfWeek;
    }
}
