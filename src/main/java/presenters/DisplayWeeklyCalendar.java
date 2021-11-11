package presenters;

import entities.Event;
import helpers.Constants;
import helpers.DisplayCalendarHelper;
import usecases.CalendarManager;
import usecases.EventManager;
import usecases.WeeklyCalendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DisplayWeeklyCalendar extends DisplayCalendar {
    private final int year;
    private final int month;
    private final int date;
    private final Map<Integer, List<Event>> calendarMap;
    private final List<String> defaultTimeLine = new ArrayList<>();
    private final EventManager eventManager = new EventManager();
    private final DisplayCalendarHelper cf;

    public DisplayWeeklyCalendar(CalendarManager cm, int year, int month, int date) {
        super(cm);
        this.year = year;
        this.month = month;
        this.date = date;
        WeeklyCalendar wc = new WeeklyCalendar();
        this.calendarMap = wc.getCalendar(cm, year, month, date);
        for (int i = 0; i < 10; i++){
            this.defaultTimeLine.add("0" + i + ":00");
        }
        for (int j = 10; j < 25; j++){
            this.defaultTimeLine.add(j+ ":00");
        }
        this.cf = new DisplayCalendarHelper(this.year, this.month);

    }

    @Override
    public String displayCalendar(){
        int startingDayOfWeek = cf.findStartDayOfWeekInteger(this.year, this.month, this.date);
        cf.eventSorter(calendarMap);
        StringBuilder result = new StringBuilder();
        setUpCalendar(startingDayOfWeek, result, cf);
        addDate(result, startingDayOfWeek);
        for (int i = 0; i < getLongestTimeLine(); i++){
            for (int j = 0; j < 7; j++){
                addTimeLineContent(startingDayOfWeek, result, i, j);
            }
            result.append("|").append("\n");
        }
        result.append(cf.endFrame(lengthDecider()));
        return result.toString();
    }

    private void addTimeLineContent(int startingDayOfWeek, StringBuilder result, int indexOne, int indexTwo) {
        List<String> newTimeLine = cf.updateTimeList(this.defaultTimeLine, gatherTimeLine(indexTwo));
        if (newTimeLine.size() > indexOne){
            addTimeLine(result, newTimeLine, indexOne);
            int tempDiv = addContent(result, newTimeLine, indexOne, indexTwo);
            addSpace(result, tempDiv, startingDayOfWeek + indexTwo);
        }
        else {
            result.append("|");
            addSpace(result, -8, startingDayOfWeek + indexTwo);
        }
    }

    private int getLongestTimeLine(){
        int length = this.defaultTimeLine.size();
        for (int i = 0; i < this.calendarMap.size(); i++){
            List<String> newTimeList = cf.updateTimeList(this.defaultTimeLine, gatherTimeLine(i));
            if (newTimeList.size() > length){
                length = newTimeList.size();
            }
        }
        return length;
    }
    private List<String> gatherTimeLine(int index){
        List<String> temp = new ArrayList<>();
        List<Integer> keyList = getKeys();
        for (Event event: calendarMap.get(keyList.get(index))){
            if (eventManager.getStartTimeString(event) != null) {
                temp.add(eventManager.getStartTimeString(event));
            }
            temp.add(eventManager.getEndTimeString(event));
        }
        return temp;
    }


    private void setUpCalendar(int startingDayOfWeek, StringBuilder result, DisplayCalendarHelper cf){
        switch (startingDayOfWeek) {
            case 1:
                result.append(cf.startFrame("MONDAY", lengthDecider()));
                break;
            case 2:
                result.append(cf.startFrame("TUESDAY", lengthDecider()));
                break;
            case 3:
                result.append(cf.startFrame("WEDNESDAY", lengthDecider()));
                break;
            case 4:
                result.append(cf.startFrame("THURSDAY", lengthDecider()));
                break;
            case 5:
                result.append(cf.startFrame("FRIDAY", lengthDecider()));
                break;
            case 6:
                result.append(cf.startFrame("SATURDAY", lengthDecider()));
                break;
            case 7:
                result.append(cf.startFrame("SUNDAY", lengthDecider()));
                break;
        }
    }

    private void addDate(StringBuilder result, int dayOfWeek){
        result.append("|");
        List<Integer> keyList = getKeys();
        for (int i = 0; i < keyList.size(); i++){
            int spacer = getSpacer(dayOfWeek + i);
            int tempSpacer = spacer/2;
            if (spacer % 2 == 1){
                tempSpacer = spacer/2 + 1;
            }
            String tempDiv = " ".repeat(tempSpacer + lengthDecider()/2 + (Constants.CAL_ROW_SPACER/2 - 3));
            String preDiv = " ".repeat((Constants.CAL_ROW_SPACER/2 - 3) + spacer/2 + lengthDecider()/2);
            result.append(preDiv);
            addDateHelper(result, keyList, tempDiv, i);
        }

        result.append("\n");
    }

    private void addDateHelper(StringBuilder result, List<Integer> keyList, String tempDiv, int index) {
        int month = this.month;
        if (index != 0 && keyList.get(index) == 1){
            month = this.month + 1;
        }
        if (keyList.get(index) < 10 && month < 10){
            result.append(" 0").append(month).append("/").append
                    ("0").append(keyList.get(index)).append(tempDiv).append("|");
        }
        else if (keyList.get(index) < 10 && month >= 10){
        result.append(" ").append(month).append("/").append
                ("0").append(keyList.get(index)).append(tempDiv).append("|");
        }
        else if (keyList.get(index) >= 10 && month < 10){
            result.append(" 0").append(month).append
                    ("/").append(keyList.get(index)).append(tempDiv).append("|");
        }
        else{
            result.append(" ").append(month).append
                    ("/").append(keyList.get(index)).append(tempDiv).append("|");
        }
    }


    private void addTimeLine(StringBuilder result, List<String> timeLine, int index){
        result.append("|").append(" ");
        result.append(timeLine.get(index)).append(" |");
    }

    private void addSpace(StringBuilder result, int length, int dayOfWeek){
        int spacer = getSpacer(dayOfWeek);
        String tempDiv = " ".repeat(Constants.CAL_ROW_SPACER + lengthDecider() + spacer - 8 - length);
        result.append(tempDiv);
    }

    private int getSpacer(int dayOfWeek) {
        int spacer = 0;
        if (dayOfWeek > 7){
            dayOfWeek -= 7;
        }
        switch (dayOfWeek) {
            case 1:
            case 7:
            case 5:
                spacer = 6;
                break;
            case 2:
                spacer = 7;
                break;
            case 3:
                spacer = 9;
                break;
            case 4:
            case 6:
                spacer = 8;
                break;
        }
        return spacer;
    }

    private Integer addContent(StringBuilder result, List<String> timeLine, int time, int index){
        List<Integer> keyList = getKeys();
        int temp = 0;
        for (Event event: calendarMap.get(keyList.get(index))){
            String eventStartTime = eventManager.getStartTimeString(event);
            if (eventStartTime == null){
                eventStartTime = eventManager.getEndTimeString(event);
            }
            if (cf.convertTimeToInt(timeLine.get(time)) >= cf.convertTimeToInt(eventStartTime)
                && cf.convertTimeToInt(timeLine.get(time)) <= cf.convertTimeToInt(eventManager.getEndTimeString(event))){
                String eventName = eventManager.getName(event);
                int eventNameSize = eventName.length();
                if (eventStartTime.equals(eventManager.getEndTimeString(event))){
                    eventName = eventName + " Due";
                    eventNameSize = eventName.length();
                    if (eventNameSize >= Constants.WEEKLY_CAL_NAME_LIMIT){
                        eventName = eventName.substring(0, Constants.WEEKLY_CAL_NAME_LIMIT - 9) + "..." + " Due";
                        eventNameSize = eventName.length();
                    }
                }
                if (eventNameSize <= Constants.WEEKLY_CAL_NAME_LIMIT - 2){
                    result.append(" ").append(eventName).append(";");
                    temp += eventNameSize + 2;
                }
                else {
                    result.append(" ").append(eventName, 0, Constants.WEEKLY_CAL_NAME_LIMIT - 5).append("...").append(";");
                    temp += Constants.WEEKLY_CAL_NAME_LIMIT;}
            }
        }
        return temp;
    }

    private List<Integer> getKeys(){
        List<Integer> lst = new ArrayList<>(calendarMap.keySet());
        Collections.sort(lst);
        List<Integer> tempOne = new ArrayList<>();
        List<Integer> tempTwo = new ArrayList<>();
        for (Integer items: lst){
            divideDate(tempOne, tempTwo, items);
        }
        if (tempOne.size() != 0 && tempTwo.size() != 0){
            lst = new ArrayList<>();
            lst.addAll(tempTwo);
            lst.addAll(tempOne);
        }
        return lst;
    }

    private void divideDate(List<Integer> tempOne, List<Integer> tempTwo, Integer items) {
        if (0 < items && items < 10){
            tempOne.add(items);
        }
        else if (20 < items){
            tempTwo.add(items);
        }
    }

    private int lengthDecider(){
        List<Integer> keyList = getKeys();
        int temp = 0;
        for (Integer number: keyList){
            List<Event> eventList = calendarMap.get(number);
            for (int i = 0; i < eventList.size(); i++) {
                int totalLength = Math.min(eventManager.getName(eventList.get(i)).length() + 6,
                        Constants.WEEKLY_CAL_NAME_LIMIT);
                totalLength = getTotalLength(eventList, totalLength, i);
                if (temp < totalLength) {
                    temp = totalLength;
                }
            }
        }
        if (temp >= Constants.CAL_ROW_SPACER - 10){
            temp -= Constants.CAL_ROW_SPACER - 10;
        }
        else {
            temp = 0;
        }
        if (temp % 2 == 1){
            temp += 1;
        }
        return temp;
    }

    private int getTotalLength(List<Event> eventList, int totalLength, int index) {
        for (int j = index + 1; j < eventList.size(); j++){
            String startTimeStringOne = eventManager.getStartTimeString(eventList.get(index));
            String startTimeStringTwo = eventManager.getStartTimeString(eventList.get(j));
            String eventName = eventManager.getName(eventList.get(j));
            if (eventManager.getStartTimeString(eventList.get(index)) == null){
                startTimeStringOne = eventManager.getEndTimeString(eventList.get(index));
            }
            if (eventManager.getStartTimeString(eventList.get(j)) == null){
                startTimeStringTwo = eventManager.getEndTimeString(eventList.get(j));
                eventName += " Due";
            }
            if (cf.convertTimeToInt(startTimeStringOne)
                    <= cf.convertTimeToInt(startTimeStringTwo)
                    && cf.convertTimeToInt(eventManager.getEndTimeString(eventList.get(index)))
                    >= cf.convertTimeToInt(startTimeStringTwo)){
                totalLength += Math.min(eventName.length() + 2,
                        Constants.WEEKLY_CAL_NAME_LIMIT);
            }
        }
        return totalLength;

    }
}
