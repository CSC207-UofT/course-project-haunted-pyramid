package presenters;
import entities.Event;
import usecases.CalendarManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.*;
import java.util.*;


public class CalendarPresenter {
    private final CalendarManager calendarManager; // CalendarManager to show
    // set up constants
    private final ArrayList<String> DATES = new ArrayList<>(){
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

    // initialize by calling a CalendarManager
    public CalendarPresenter(CalendarManager cm) {
        this.calendarManager = cm;

    }
    /*
     TODO replace Event Object to EventManager or EventController Object
     */

    /**
     * display the monthly calendar for the given year, month according to the information stored in CalendarManager
     * @param year chosen year
     * @param month chosen month
     * @return calendar diagram that shows information stored in CalendarManager
     */
    public String showMonthCalendar(int year, int month){
        String result = convertMonthlyMapToPicture(this.calendarManager.getMonthlyCalendar(year, month), year, month);
        if (this.calendarManager.notifyConflict(year, month).size() == 0){
            return result + "There is no conflict for this month's events";
        }
        else {
            StringBuilder temp = new StringBuilder();
            for (Event item : this.calendarManager.notifyConflict(year, month)){
                temp.append(item.getName()).append("; ");
            }
            return result + "Following Events have conflicts" + "\n" + temp;
        }
    }

    /* TODO build showWeeklyCalendar and showDailyCalendar
     */

    /**
     * helper method for showMonthlyCalendar
     * converts the map input to desired diagram.
     * @param mapObject map obeject input that will be used to display
     * @param year chosen year
     * @param month chosen month
     * @return diagram converted from the map object
     */
    private String convertMonthlyMapToPicture(Map<Integer, List<Event>> mapObject, int year, int month){
        StringBuilder result = new StringBuilder();
        result.append("Calendar for ").append(year).append("/").append(month).append("\n");
        String div = "-".repeat(224);
        result.append(" ").append(div).append("\n");
        String space = " ".repeat(12);
        for (String days: this.DATES){
            result.append("|").append(space).append(days).append(space);
        }
        result.append("|").append("\n");
        result.append(" ").append(div).append("\n");
        result.append("|");
        List<Integer> keyList = new ArrayList<>(mapObject.keySet());
        Collections.sort(keyList);

        String startingDayOfWeek = LocalDate.parse(keyList.get(0) + "/" + month
                        + "/" + year, DateTimeFormatter.ofPattern("d/M/uuuu"))
                .getDayOfWeek().getDisplayName(TextStyle.SHORT_STANDALONE, Locale.CANADA);

        switch (startingDayOfWeek) {
            case "Sun.":
                startingDayOfWeek = "SUNDAY";
                break;
            case "Mon.":
                startingDayOfWeek = "MONDAY";
                break;
            case "Tue.":
                startingDayOfWeek = "TUESDAY";
                break;
            case "Wed.":
                startingDayOfWeek = "WEDNESDAY";
                break;
            case "Thu.":
                startingDayOfWeek = "THURSDAY";
                break;
            case "Fri.":
                startingDayOfWeek = "FRIDAY";
                break;
            case "Sat.":
                startingDayOfWeek = "SATURDAY";
                break;
        }
        List<Integer> usedKeys = new ArrayList<>();
        List<String> appliedDays = new ArrayList<>();
        addDatesToCalendar(result, keyList, startingDayOfWeek, usedKeys, appliedDays);
        addContentsToCalendar(mapObject, result, startingDayOfWeek, usedKeys);


        int iterate = 4;
        if (startingDayOfWeek.equals("FRIDAY") || startingDayOfWeek.equals("SATURDAY")){
            iterate = 5;
        }
        for (int i = 0; i < iterate; i++) {
            result.append("\n").append(" ").append(div).append("\n").append("|");
            keyList = keyList.subList(usedKeys.size(), keyList.size());
            usedKeys = new ArrayList<>();
            appliedDays = new ArrayList<>();
            addDatesToCalendar(result, keyList, "SUNDAY", usedKeys, appliedDays);
            addContentsToCalendar(mapObject, result, "SUNDAY", usedKeys);
        }

        result.append("\n").append(" ").append(div).append("\n");


        return result.toString();
    }

    /**
     * Helper method for convertMonthlyMapToPicture method. Add contents (time, name) of the events to the appropriate
     * dates
     * @param mapObject MapObject to play with (with the information of events)
     * @param result the result string (diagram) that will be added on
     * @param startingDayOfWeek the starting day of week to add content with
     * @param usedKeys days that will be added on
     */
    private void addContentsToCalendar(Map<Integer, List<Event>> mapObject,
                                       StringBuilder result, String startingDayOfWeek, List<Integer> usedKeys) {
        result.append("\n").append("|");
        int startIndex = this.DATES.indexOf(startingDayOfWeek);
        for (int x = 0; x < startIndex; x++) {
            String contentDiv = " ".repeat(this.DATES.get(x).length() + 24);
            result.append(contentDiv).append("|");
        }
        int longestSizeEvent = 0;
        for (int keys : mapObject.keySet()){
            if (longestSizeEvent < mapObject.get(keys).size()- 1){
                longestSizeEvent = mapObject.get(keys).size() - 1;
            }
        }
        for (int z = 0; z <= longestSizeEvent; z++) {
            for (int y = 0; y < this.DATES.size() - startIndex; y++) {
                int size = 0;
                if (usedKeys.size() > y) {
                    if (mapObject.get(usedKeys.get(y)).size() > z) {
                        Event chosenEvent = mapObject.get(usedKeys.get(y)).get(z);
                        size = chosenEvent.getName().length() + 12;
                        if (size < this.DATES.get(startIndex + y).length() + 21) {
                            result.append(" ").append(chosenEvent.getName()).append(": ");
                            result.append(chosenEvent.getStartString(), 11, 16);
                            result.append(" - ");
                            result.append(chosenEvent.getEndString(), 11, 16);
                            size = chosenEvent.getName().length() + 16;
                        }
                    }
                }


                String textDiv = " ".repeat(24 + this.DATES.get(startIndex + y).length() - size);
                result.append(textDiv).append("|");
            }
            if (z == longestSizeEvent){
                break;
            }
            result.append("\n").append("|");
            for (int x = 0; x < startIndex; x++) {
                String contentDiv = " ".repeat(this.DATES.get(x).length() + 24);
                result.append(contentDiv).append("|a");
            }
        }
    }

    /**
     * helper method for convertMonthlyMapToPicture method
     * adds date to the diagram
     * @param result resulting diagram that will be added on
     * @param keyList dates that will be used to add on the diagram
     * @param startingDayOfWeek the starting day of week that dates will be added on
     * @param usedKeys all the dates that are already placed
     * @param appliedDays all the day of weeks that dates have been placed on
     */
    private void addDatesToCalendar(StringBuilder result, List<Integer> keyList,
                                    String startingDayOfWeek, List<Integer> usedKeys, List<String> appliedDays) {
        if (keyList.size() == 0){
            return;
        }
        for (int i = 0; i < this.DATES.size(); i ++) {
            if (!this.DATES.get(i).equals(startingDayOfWeek)) {
                String tempDiv = " ".repeat(this.DATES.get(i).length() + 24);
                result.append(tempDiv).append("|");
            } else {
                result.append(" ").append(keyList.get(0));
                usedKeys.add(keyList.get(0));
                appliedDays.add(this.DATES.get(i));
                int spacer = 0;
                if (keyList.get(0) >= 10){
                    spacer = 1;
                }
                String tempDiv = " ".repeat(this.DATES.get(i).length() + 22 - spacer);
                result.append(tempDiv).append("|");
                switch (this.DATES.get(i)) {
                    case "SUNDAY":
                        for (int j = 1; j <= 6; j++) {
                            dateSpaceEditor(result, keyList, usedKeys, appliedDays, i, j);
                        }
                        break;
                    case "MONDAY":
                        for (int j = 1; j <= 5; j++) {
                            dateSpaceEditor(result, keyList, usedKeys, appliedDays, i, j);
                        }
                        break;
                    case "TUESDAY":
                        for (int j = 1; j <= 4; j++) {
                            dateSpaceEditor(result, keyList, usedKeys, appliedDays, i, j);
                        }
                        break;
                    case "WEDNESDAY":
                        for (int j = 1; j <= 3; j++) {
                            dateSpaceEditor(result, keyList, usedKeys, appliedDays, i, j);
                        }
                        break;
                    case "THURSDAY":
                        for (int j = 1; j <= 2; j++) {
                            dateSpaceEditor(result, keyList, usedKeys, appliedDays, i, j);
                        }
                        break;
                    case "FRIDAY":
                        for (int j = 1; j <= 1; j++) {
                            dateSpaceEditor(result, keyList, usedKeys, appliedDays, i, j);
                        }
                        break;
                }
                break;
            }
        }
    }

    /**
     * helper method to assist addDatesToCalendar method
     * provides appropriate blank spaces to make the diagram beautiful
     * @param result resulting diagram that will be added on
     * @param keyList the dates that are being used to put on the diagram
     * @param usedKeys the dates that have been put on the diagram already
     * @param appliedDays the day of weeks for the dates that are used already
     * @param i index representing the starting day of week
     * @param j index representing remaining day of week
     */
    private void dateSpaceEditor(StringBuilder result, List<Integer> keyList, List<Integer> usedKeys,
                             List<String> appliedDays, int i, int j) {
        String tempDiv;
        int spacer = 0;
        if (j < keyList.size()) {
            spacer = 2;
            result.append(" ").append(keyList.get(j));
            usedKeys.add(keyList.get(j));
            if (keyList.get(j) >= 10) {
                spacer = 3;

            }
        }
        appliedDays.add(this.DATES.get(i + j));
        tempDiv = " ".repeat(this.DATES.get(i + j).length() + 24 - spacer);
        result.append(tempDiv).append("|");
    }
}

