package presenters.CalendarFactory;

import helpers.EventIDConverter;
import usecases.calendar.CalendarManager;
import usecases.events.EventManager;

/**
 * Abstract class that can display different types of calendar images
 * @author Seo Won Yi
 * @see MonthlyCalendarDisplay
 * @see WeeklyCalendarDisplay
 * @see DailyCalendarDisplay
 */
public abstract class CalendarDisplay {
    public CalendarManager cm;
    public EventManager em;
    public EventIDConverter converter;
    public CalendarTimePresenter timePresenter;
    public int year;
    public int month;
    public int date;

    public CalendarDisplay(CalendarManager cm, EventManager em, int year, int month, int date){
        this.cm = cm;
        this.em = em;
        this.converter = new EventIDConverter(em);
        this.timePresenter = new CalendarTimePresenter(em);
        this.year = year;
        this.month = month;
        this.date = date;
    }

    /**
     * Display the image of the calendar
     * @return image of the calendar
     */
    public abstract String displayCalendar();

    /**
     * Size of the calendar (number of dates)
     * @return size of the calendar (number of dates)
     */
    public abstract int size();

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return date;
    }
}
