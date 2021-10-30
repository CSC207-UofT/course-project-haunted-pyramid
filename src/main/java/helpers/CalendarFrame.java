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

    public String startFrame(String startDayOfWeek, int additionalSpacer){
        StringBuilder result = new StringBuilder();
        result.append("Calendar for ").append(this.year).append("/").append(this.month).append("\n");
        String div = "-".repeat(224 + additionalSpacer * 7);
        result.append(" ").append(div).append("\n");
        String space = " ".repeat(12 + additionalSpacer/2);
        switch (startDayOfWeek) {
            case "SUNDAY":
                frameWithDifferentStartDay(result, 0, space);
                break;
            case "MONDAY":
                frameWithDifferentStartDay(result, 1, space);
                break;
            case "TUESDAY":
                frameWithDifferentStartDay(result, 2, space);
                break;
            case "WEDNESDAY":
                frameWithDifferentStartDay(result, 3, space);
                break;
            case "THURSDAY":
                frameWithDifferentStartDay(result, 4, space);
                break;
            case "FRIDAY":
                frameWithDifferentStartDay(result, 5, space);
                break;
            case "SATURDAY":
                frameWithDifferentStartDay(result, 6, space);
                break;
        }
        result.append("|").append("\n");
        result.append(" ").append(div).append("\n");
        return result.toString();
    }

    private void frameWithDifferentStartDay(StringBuilder result, int index, String space){
        for (String days : this.DATES.subList(index, 7)){
            result.append("|").append(space).append(days).append(space);
        }
        for (String days : this.DATES.subList(0, index)){
            result.append("|").append(space).append(days).append(space);
        }
    }

    public String endFrame(int additionalSpacer){
        StringBuilder result = new StringBuilder();
        String div = "-".repeat(224 + additionalSpacer*7);
        result.append(" ").append(div).append("\n");
        return result.toString();
    }
}
