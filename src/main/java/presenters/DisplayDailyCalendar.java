package presenters;

import entities.Event;
import helpers.Constants;
import helpers.DisplayCalendarHelper;
import usecases.CalendarManager;
import usecases.DailyCalendar;
import usecases.EventManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DisplayDailyCalendar extends DisplayCalendar {
    private final int year;
    private final int month;
    private final int date;
    private final DisplayCalendarHelper cf;
    private final Map<Integer, List<Event>> calendarMap;
    private final EventManager eventManager;
    private List<String> timeLine;
    private final String dayOfWeek;

    public DisplayDailyCalendar(CalendarManager cm, int year, int month, int date) {
        super(cm);
        this.year = year;
        this.month = month;
        this.date = date;
        this.cf = new DisplayCalendarHelper(year, month);
        DailyCalendar dc = new DailyCalendar();
        this.calendarMap = dc.getCalendar(cm, year, month, date);
        this.eventManager = new EventManager();
        this.dayOfWeek = cf.findStartDayOfWeekString(this.year, this.month, this.date);
    }
    @Override
    public String displayCalendar() {
        StringBuilder result = new StringBuilder();
        cf.eventSorter(calendarMap);
        setTimeLine();
        int lengthDecider = getLongestEventLength();
        dailyFrame(result, lengthDecider);
        addDate(result, lengthDecider);
        addTimeLineWithContent(result, lengthDecider);
        String bottom = "-".repeat(Constants.DAILY_CAL_SIZE * 2 + lengthDecider);
        result.append(" ").append(bottom).append(" ");
        return result.toString();
    }


    private void dailyFrame(StringBuilder result, int lengthDecider){
        String top = "-".repeat(Constants.DAILY_CAL_SIZE * 2 + lengthDecider);
        result.append(" ").append(top).append(" ").append("\n");
        String spacer = " ".repeat(Constants.DAILY_CAL_SIZE + lengthDecider/2 - this.dayOfWeek.length()/2);
        String nextSpacer;
        if (this.dayOfWeek.length() % 2 == 1){
            nextSpacer = " ".repeat(Constants.DAILY_CAL_SIZE + lengthDecider/2 - this.dayOfWeek.length()/2 - 1);
        }
        else {
            nextSpacer = spacer;
        }
        result.append("|").append(spacer).append(this.dayOfWeek).append(nextSpacer).append("|").append("\n");
        result.append(" ").append(top).append(" ").append("\n");
    }

    private void addDate(StringBuilder result, int lengthDecider){
        result.append("|");
        String div = " ".repeat(Constants.DAILY_CAL_SIZE + lengthDecider/2 - 3);
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
        result.append(div).append("|").append("\n");
    }

    private void addSpace(StringBuilder result, int length, int lengthDecider){
        String div = " ".repeat(Constants.DAILY_CAL_SIZE*2 + lengthDecider - 8 - length);
        result.append(div).append("|").append("\n");
    }

    private void setTimeLine(){
        List<String> intTimeLine = getDefaultTimeLine();
        List<String> additionalTimeLine = getAdditionalTimeLine();
        this.timeLine = cf.updateTimeList(intTimeLine, additionalTimeLine);
    }

    private List<String> getDefaultTimeLine(){
        List<String> defaultTimeLine = new ArrayList<>();
        for (int i = 0; i <= 24; i++){
            if (i < 10){
                defaultTimeLine.add("0" + i + ":00");
            }
            else {
                defaultTimeLine.add(i + ":00");
            }
        }
        return defaultTimeLine;
    }

    private List<String> getAdditionalTimeLine() {
        List<String> additionalTimeLine = new ArrayList<>();
        for (Event event : calendarMap.get(this.date)) {
            String startTime = eventManager.getStartTimeString(event);
            String endTime = eventManager.getEndTimeString(event);
            if (startTime == null){
                startTime = endTime;
            }
            if (!additionalTimeLine.contains(startTime)){
                additionalTimeLine.add(startTime);
            }
            if (!additionalTimeLine.contains(endTime)){
                additionalTimeLine.add(endTime);
            }
        }
        return additionalTimeLine;
    }

    private void addTimeLineWithContent(StringBuilder result, int lengthDecider){
        for (String timeLine : this.timeLine){
            result.append("|").append(" ").append(timeLine).append(" ").append("|");
            int contentLength = addContent(result, timeLine);
            addSpace(result, contentLength, lengthDecider);
        }
    }

    private int addContent(StringBuilder result, String time){
        int totalLength = 0;
        for (Event event : this.calendarMap.get(this.date)){
            String startTime = eventManager.getStartTimeString(event);
            String endTime = eventManager.getEndTimeString(event);
            if (startTime == null){
                startTime = endTime;
            }
            String eventName = eventManager.getName(event);
            int eventNameSize = eventName.length();
            int eventID = eventManager.getID(event);
            if (eventName.length() > Constants.DAILY_CAL_SIZE){
                eventName = eventName.substring(0, Constants.DAILY_CAL_SIZE) + "...";
                eventNameSize = eventName.length();
            }
            if (!startTime.equals(endTime) && startTime.equals(time)) {
                result.append(" ");
                result.append("ID:").append(eventID).append(" ").append(eventName).append(" Start;");
                totalLength += eventNameSize + 13;
            }
            else if (!startTime.equals(endTime) && endTime.equals(time)){
                result.append(" ");
                result.append("ID:").append(eventID).append(" ").append(eventName).append(" End;");
                totalLength += eventNameSize + 11;
            }
            else if (startTime.equals(endTime) && startTime.equals(time)){
                result.append(" ");
                result.append("ID:").append(eventManager.getID(event)).append(" ").append(eventName).append(" Due;");
                totalLength += eventNameSize + 11;
            }
        }
        return totalLength;
    }

    private int getLongestEventLength() {
        int longestLength = 0;
        for (String time : this.timeLine) {
            int tempLength = 0;
            for (Event event : this.calendarMap.get(this.date)) {
                String eventStartTime = this.eventManager.getStartTimeString(event);
                String eventEndTime = this.eventManager.getEndTimeString(event);
                String eventName = eventManager.getName(event);
                if (eventStartTime == null){
                    eventStartTime = eventEndTime;
                }
                int nameMin = Math.min(eventName.length() + 13, Constants.DAILY_CAL_SIZE + 13);
                if (eventStartTime.equals(time)){
                    tempLength += nameMin;
                }
                else if (eventEndTime.equals(time)){
                    tempLength += nameMin;
                }
            }
            if (tempLength > longestLength){
                longestLength = tempLength;
            }
        }
        if (longestLength > 90){
            longestLength -= 90;
        }
        else {
            longestLength = 0;
        }
        if (longestLength % 2 == 1){
            longestLength += 1;
        }
        return longestLength;
    }
}
