package presenters;

import usecases.CalendarManager;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DisplayDailyCalendar extends DisplayCalendar {
    private int year;
    private int month;
    private int date;
    public DisplayDailyCalendar(CalendarManager cm, int year, int month, int date) {
        super(cm);
        this.year = year;
        this.month = month;
        this.date = date;
    }
    @Override
    public String displayCalendar() {
        StringBuilder result = new StringBuilder();
        String chosenDayOfWeek = dailyFrame(result);
        addDate(result, chosenDayOfWeek);
        return result.toString();
    }


    private String dailyFrame(StringBuilder result){
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
        String top = "-".repeat(100);
        result.append(" ").append(top).append(" ").append("\n");
        String spacer = " ".repeat(50 - chosenDayOfWeek.length()/2);
        result.append("|").append(spacer).append(chosenDayOfWeek).append(spacer).append("|").append("\n");
        result.append(" ").append(top).append(" ").append("\n");
        return chosenDayOfWeek;
    }

    private void addDate(StringBuilder result, String dayOfWeek){
        result.append("|");
        String div = " ".repeat(47);
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
        String div = " ".repeat(105 - length - dayOfWeek.length());
        result.append(div).append("|").append("\n");
    }



    public static void main(String[] args) {
        CalendarManager cm = new CalendarManager();
        DisplayDailyCalendar ddc = new DisplayDailyCalendar(cm, 2021, 11, 1);
        System.out.println(ddc.displayCalendar());
    }
}
