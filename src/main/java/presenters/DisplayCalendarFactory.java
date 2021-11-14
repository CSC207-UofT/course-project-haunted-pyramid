package presenters;
import usecases.calendar.CalendarManager;

/**
 * @author Seo Won Yi
 */
public class DisplayCalendarFactory {
    private final CalendarManager calendarManager;

    public DisplayCalendarFactory(CalendarManager cm) {
        this.calendarManager = cm;
    }


    public DisplayCalendar displaySpecificCalendarByType(String calendarType, int year, int month, int date){
        if (calendarType.equalsIgnoreCase("MONTHLY")) {
            return new DisplayMonthlyCalendar(this.calendarManager, year, month);
        }
        else if (calendarType.equalsIgnoreCase("WEEKLY")){
            return new DisplayWeeklyCalendar(this.calendarManager, year, month, date);
        }
        else if (calendarType.equalsIgnoreCase("DAILY")){
            return new DisplayDailyCalendar(this.calendarManager, year, month, date);
        }
        return null;
    }

    public DisplayCalendar displayCurrentCalendarByType(String calendarType){
        int year = calendarManager.getCurrentYear();
        int month = calendarManager.getCurrentMonth();
        int date = calendarManager.getCurrentDate();

        return displaySpecificCalendarByType(calendarType, year, month, date);
    }
}

