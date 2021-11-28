package presenters.CalendarFactory;
import usecases.calendar.CalendarManager;
import usecases.events.EventManager;

/**
 * A Factory class that determines which type of DisplayCalendar class to show
 * @author Seo Won Yi
 * @see DisplayCalendar
 * @see DisplayDailyCalendar
 * @see DisplayWeeklyCalendar
 * @see DisplayMonthlyCalendar
 */
public class DisplayCalendarFactory {
    private final CalendarManager calendarManager;
    private final EventManager eventManager;

    public DisplayCalendarFactory(CalendarManager cm, EventManager em) {
        this.calendarManager = cm;
        this.eventManager = em;
    }

    /**
     * Create a DisplayCalendar object with the given year, month and date. The type of the calendar is determined by
     * calendarType argument
     * @param calendarType Type of the calendar
     * @param year year of the calendar
     * @param month month of the calendar
     * @param date date of the calendar
     * @return DisplayCalendar object with the given information
     */
    public DisplayCalendar displaySpecificCalendarByType(String calendarType, int year, int month, int date){
        if (calendarType.equalsIgnoreCase("MONTHLY")) {
            return new DisplayMonthlyCalendar(this.calendarManager, this.eventManager, year, month);
        }
        else if (calendarType.equalsIgnoreCase("WEEKLY")){
            return new DisplayWeeklyCalendar(this.calendarManager, this.eventManager, year, month, date);
        }
        else if (calendarType.equalsIgnoreCase("DAILY")){
            return new DisplayDailyCalendar(this.calendarManager, this.eventManager, year, month, date);
        }
        return null;
    }

    /**
     * Create a DisplayCalendar object for the current date. The type of the calendar is determined by calendarType
     * argument
     * @param calendarType Type of the calendar
     * @return DisplayCalendar object for the current date
     */
    public DisplayCalendar displayCurrentCalendarByType(String calendarType){
        int year = calendarManager.getCurrentYear();
        int month = calendarManager.getCurrentMonth();
        int date = calendarManager.getCurrentDate();

        return displaySpecificCalendarByType(calendarType, year, month, date);
    }
}

