package presenters;
import usecases.*;

import java.time.LocalDate;
import java.time.format.*;
import java.util.*;


public class CalendarPresenter{
    private final CalendarManager calendarManager;

    public CalendarPresenter(CalendarManager cm) {
        this.calendarManager = cm;
    }


    public String displaySpecificCalendarByType(String calendarType, int year, int month, int date){
        if (calendarType.equalsIgnoreCase("MONTHLY")) {
            DisplayMonthlyCalendar dmc = new DisplayMonthlyCalendar(this.calendarManager, year, month);
            return dmc.displayCalendar();
        }
        else if (calendarType.equalsIgnoreCase("WEEKLY")){
            DisplayWeeklyCalendar dwc = new DisplayWeeklyCalendar(this.calendarManager, year, month, date);
            return dwc.displayCalendar();
        }
        else if (calendarType.equalsIgnoreCase("DAILY")){
            DisplayDailyCalendar ddc = new DisplayDailyCalendar(this.calendarManager, year, month, date);
            return ddc.displayCalendar();
        }
        return null;
    }

    public String displayCurrentCalendarByType(String calendarType){
        if (calendarType.equalsIgnoreCase("MONTHLY")) {
            DisplayMonthlyCalendar dmc = new DisplayMonthlyCalendar(this.calendarManager,
                    calendarManager.getCurrentYear(), calendarManager.getCurrentMonth());
            return dmc.displayCalendar();
        }
        else if (calendarType.equalsIgnoreCase("WEEKLY")){
            DisplayWeeklyCalendar dwc = new DisplayWeeklyCalendar(this.calendarManager,
                    calendarManager.getCurrentYear(), calendarManager.getCurrentMonth(),
                    calendarManager.getCurrentDate());
            return dwc.displayCalendar();
        }
        else if (calendarType.equalsIgnoreCase("DAILY")){
            DisplayDailyCalendar ddc = new DisplayDailyCalendar(this.calendarManager,
                    calendarManager.getCurrentYear(), calendarManager.getCurrentMonth(),
                    calendarManager.getCurrentDate());
            return ddc.displayCalendar();
        }
        return null;
    }


}

