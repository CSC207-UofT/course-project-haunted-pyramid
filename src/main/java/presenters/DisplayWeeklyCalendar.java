package presenters;

import entities.Event;
import helpers.CalendarFrame;
import usecases.CalendarManager;
import usecases.WeeklyCalendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisplayWeeklyCalendar extends DisplayCalendar {
    private final int year;
    private final int month;
    private final int date;
    private final Map<Integer, List<Event>> calendarMap;
    private List<String> timeLine = new ArrayList<>();
    public DisplayWeeklyCalendar(CalendarManager cm, int year, int month, int date) {
        super(cm);
        this.year = year;
        this.month = month;
        this.date = date;
        WeeklyCalendar wc = new WeeklyCalendar();
        this.calendarMap = wc.getCalendar(cm, year, month, date);
        for (int i = 0; i < 10; i++){
            this.timeLine.add("0" + i + ":00");
        }

    }

    @Override
    public String displayCalendar(){
        LocalDate localDate = LocalDate.of(year, month, date);
        DayOfWeek dayOfWeek = DayOfWeek.from(localDate);
        CalendarFrame cf = new CalendarFrame(this.year, this.month);
        StringBuilder result = new StringBuilder();
        int startingDayOfWeek = dayOfWeek.getValue();
        setUpCalendar(startingDayOfWeek, result, cf);
        return result.toString();
    }

    private void setUpCalendar(int startingDayOfWeek, StringBuilder result, CalendarFrame cf){
        if (startingDayOfWeek == 1){
            result.append(cf.startFrame("MONDAY"));
        }
        else if (startingDayOfWeek == 2){
            result.append(cf.startFrame("TUESDAY"));
        }
        else if (startingDayOfWeek == 3){
            result.append(cf.startFrame("WEDNESDAY"));
        }
        else if (startingDayOfWeek == 4){
            result.append(cf.startFrame("THURSDAY"));
        }
        else if (startingDayOfWeek == 5){
            result.append(cf.startFrame("FRIDAY"));
        }
        else if (startingDayOfWeek == 6){
            result.append(cf.startFrame("SATURDAY"));
        }
        else if (startingDayOfWeek == 7){
            result.append(cf.startFrame("SUNDAY"));
        }
    }

    private void addTimeLine(){

    }

    public static void main(String[] args) {
        CalendarManager cm = new CalendarManager();
        DisplayWeeklyCalendar dwc = new DisplayWeeklyCalendar(cm, 2021, 10, 28);
        System.out.println(dwc.displayCalendar());

    }
}
