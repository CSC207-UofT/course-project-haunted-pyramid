package presenters;

import helpers.Constants;
import helpers.DisplayCalendarHelper;
import usecases.CalendarManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisplayDailyCalendar extends DisplayCalendar {
    private final int year;
    private final int month;
    private final int date;
    private final DisplayCalendarHelper cf;
    private List<Integer> timeLine;

    public DisplayDailyCalendar(CalendarManager cm, int year, int month, int date) {
        super(cm);
        this.year = year;
        this.month = month;
        this.date = date;
        this.cf = new DisplayCalendarHelper(year, month);
    }
    @Override
    public String displayCalendar() {
        StringBuilder result = new StringBuilder();
        dailyFrame(result);
        addDate(result);
        return result.toString();
    }


    private void dailyFrame(StringBuilder result){
        String chosenDayOfWeek = cf.findStartDayOfWeekString(this.year, this.month, this.date);
        String top = "-".repeat(Constants.DAILY_CAL_SIZE);
        result.append(" ").append(top).append(" ").append("\n");
        String spacer = " ".repeat(Constants.DAILY_CAL_SIZE/2 - chosenDayOfWeek.length()/2);
        String nextSpacer;
        if (chosenDayOfWeek.length() % 2 == 1){
            nextSpacer = " ".repeat(Constants.DAILY_CAL_SIZE/2 - chosenDayOfWeek.length()/2 - 1);
        }
        else {
            nextSpacer = spacer;
        }
        result.append("|").append(spacer).append(chosenDayOfWeek).append(nextSpacer).append("|").append("\n");
        result.append(" ").append(top).append(" ").append("\n");
    }

    private void addDate(StringBuilder result){
        result.append("|");
        String div = " ".repeat(Constants.DAILY_CAL_SIZE/2 - 3);
        result.append(div);
        if (this.month < 10){
            result.append("0").append(this.month).append("/");
        }
        else {
            result.append(this.month).append("/");
        }
        if (this.date < 10){
            result.append("0").append(this.date).append(" ");
        }
        else {
            result.append(this.date).append(" ");
        }
        result.append(div).append("|");
    }

    private void addSpace(StringBuilder result, String dayOfWeek, int length){
        String div = " ".repeat(Constants.DAILY_CAL_SIZE + 5 - length - dayOfWeek.length());
        result.append(div).append("|").append("\n");
    }





    public static void main(String[] args) {
        CalendarManager cm = new CalendarManager();
        DisplayDailyCalendar ddc = new DisplayDailyCalendar(cm, 2021, 11, 3);
        System.out.println(ddc.displayCalendar());
    }
}
