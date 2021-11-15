package presenters;

import usecases.calendar.CalendarManager;

/**
 * Abstract class that can display different types of calendar images
 * @author Seo Won Yi
 * @see DisplayMonthlyCalendar
 * @see DisplayWeeklyCalendar
 * @see DisplayDailyCalendar
 */
public abstract class DisplayCalendar {
    public CalendarManager cm;

    public DisplayCalendar(CalendarManager cm){
        this.cm = cm;
    }

    /**
     * Display the image of the calendar
     * @return image of the calendar
     */
    public abstract String displayCalendar();
}
