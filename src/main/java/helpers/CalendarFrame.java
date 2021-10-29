package helpers;

import java.util.ArrayList;

public class CalendarFrame {

    private final ArrayList<String> DATES = new ArrayList<>() {
        {
            add("SUNDAY");
            add("MONDAY");
            add("TUESDAY");
            add("WEDNESDAY");
            add("THURSDAY");
            add("FRIDAY");
            add("SATURDAY");
        }
    };

    private int year;
    private int month;

    public CalendarFrame(int year, int month){
        this.year = year;
        this.month = month;
    }

    public String StartFrame(){
        StringBuilder result = new StringBuilder();
        result.append("Calendar for ").append(this.year).append("/").append(this.month).append("\n");
        String div = "-".repeat(224);
        result.append(" ").append(div).append("\n");
        String space = " ".repeat(12);
        for (String days: this.DATES){
            result.append("|").append(space).append(days).append(space);
        }
        result.append("|").append("\n");
        result.append(" ").append(div).append("\n");
        return result.toString();
    }

    public String EndFrame(){
        StringBuilder result = new StringBuilder();
        String div = "-".repeat(224);
        result.append(" ").append(div).append("\n");
        return result.toString();
    }
}
