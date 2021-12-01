package presenters.CalendarFactory;

import helpers.EventIDConverter;
import usecases.calendar.CalendarManager;
import usecases.events.EventManager;

/**
 * Abstract class that can display different types of calendar images
 * @author Seo Won Yi
 * @see DisplayMonthlyCalendar
 * @see DisplayWeeklyCalendar
 * @see DisplayDailyCalendar
 */
public abstract class DisplayCalendar {
    public CalendarManager cm;
    public EventManager em;
    public EventIDConverter converter;
    public CalendarTimePresenter timePresenter;

    public DisplayCalendar(CalendarManager cm, EventManager em){
        this.cm = cm;
        this.em = em;
        this.converter = new EventIDConverter(em);
        this.timePresenter = new CalendarTimePresenter(em);
    }

    /**
     * Display the image of the calendar
     * @return image of the calendar
     */
    public abstract String displayCalendar();
}
