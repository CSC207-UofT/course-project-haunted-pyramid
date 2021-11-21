package presenters;

import entities.Event;
import helpers.Constants;
import helpers.DisplayCalendarHelper;
import usecases.calendar.CalendarManager;
import usecases.calendar.DailyCalendar;
import usecases.events.EventManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Build the image of daily calendar and display if asked
 * @author Seo Won Yi
 * @see DisplayCalendar
 * @see DailyCalendar
 * @see DisplayCalendarHelper
 */
public class DisplayDailyCalendar extends DisplayCalendar {
    private final int year;
    private final int month;
    private final int date;
    private final DisplayCalendarHelper cf;
    private final Map<Integer, List<Event>> calendarMap;
    private final EventManager eventManager;
    private List<String> timeLine;
    private final String dayOfWeek;

    /**
     * Set up the DisplayDailyCalendar
     * @param cm CalendarManager Object that will bring the information from
     * @param year given year
     * @param month given month
     * @param date given date
     */
    public DisplayDailyCalendar(CalendarManager cm, int year, int month, int date) {
        super(cm);
        this.year = year;
        this.month = month;
        this.date = date;
        this.cf = new DisplayCalendarHelper(year, month);
        DailyCalendar dc = new DailyCalendar();
        this.calendarMap = dc.getCalendar(cm, year, month, date);
        this.eventManager = new EventManager(new ArrayList<>());
        this.dayOfWeek = cf.findStartDayOfWeekString(this.year, this.month, this.date);
    }

    /**
     * Return the image of daily calendar
     * @return return the image of the daily calendar
     */
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

    /**
     * Create frame of the daily calendar
     * @param result StringBuilder object that will be added on
     * @param lengthDecider Extra length to be added to the frame of the calendar
     */
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

    /**
     * Add date to the image
     * @param result StringBuild object that will be added on
     * @param lengthDecider Extra length to be added for the blank spaces around the date
     */
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

    /**
     * Add blank spaces to result
     * @param result StringBuilder object that will be added on
     * @param length length to be subtracted from the default blank space (name of the events)
     * @param lengthDecider extra space to be added if the name of the events are too large
     */
    private void addSpace(StringBuilder result, int length, int lengthDecider){
        String div = " ".repeat(Constants.DAILY_CAL_SIZE*2 + lengthDecider + Constants.TIMELINE_SPACER - length);
        result.append(div).append("|").append("\n");
    }

    /**
     * set up the timeline
     */
    private void setTimeLine(){
        List<String> intTimeLine = getDefaultTimeLine();
        List<String> additionalTimeLine = getAdditionalTimeLine();
        this.timeLine = cf.updateTimeList(intTimeLine, additionalTimeLine);
    }

    /**
     * Get a list of String of default timeline (from 00:00 to 24:00)
     * @return A list of timeline strings
     */
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

    /**
     * Get a list of String of every timeline information in the calendar (start time and end time)
     * @return A list of timeline strings
     */
    private List<String> getAdditionalTimeLine() {
        List<String> additionalTimeLine = new ArrayList<>();
        for (Event event : calendarMap.get(this.date)) {
            String endTime = eventManager.getEndTimeString(event);
            String startTime = getStartTime(event, endTime);
            if (!additionalTimeLine.contains(startTime)){
                additionalTimeLine.add(startTime);
            }
            if (!additionalTimeLine.contains(endTime)){
                additionalTimeLine.add(endTime);
            }
        }
        return additionalTimeLine;
    }

    /**
     * Add timeline information and content of the event to the StringBuilder result
     * @param result StringBuilder object to be added on
     * @param lengthDecider Extra space to be added if the name of the event is too long
     */
    private void addTimeLineWithContent(StringBuilder result, int lengthDecider){
        for (String timeLine : this.timeLine){
            result.append("|").append(" ").append(timeLine).append(" ").append("|");
            int contentLength = addContent(result, timeLine);
            addSpace(result, contentLength, lengthDecider);
        }
    }

    /**
     * Add content (event information) to the StringBuilder result if the event's start or end time is the same as
     * time argument
     * @param result StringBuilder object to be added on
     * @param time time to be considered (for example, if the time is the same as the event's start time etc...)
     * @return the total length of the event name added to the result for the same time
     */
    private int addContent(StringBuilder result, String time){
        int totalLength = 0;
        for (Event event : this.calendarMap.get(this.date)){
            String endTime = eventManager.getEndTimeString(event);
            String startTime = getStartTime(event, endTime);
            String eventName = eventManager.getName(event);
            UUID eventID = eventManager.getID(event);
            if (eventName.length() > Constants.DAILY_CAL_SIZE){
                eventName = eventName.substring(0, Constants.DAILY_CAL_SIZE) + "...";
            }
            if (!startTime.equals(endTime) && startTime.equals(time)) {
                totalLength = getTotalLengthStart(result, totalLength, eventName, eventID);
            }
            else if (!startTime.equals(endTime) && endTime.equals(time)){
                totalLength = getTotalLengthEnd(result, totalLength, eventName, eventID);
            }
            else if (startTime.equals(endTime) && startTime.equals(time)){
                totalLength = getTotalLengthDue(result, totalLength, eventName, eventID);
            }
        }
        return totalLength;
    }

    /**
     * Add the StringBuilder result with the event information (ID, name + start)
     * Return the updated total length of the event name added
     * @param result StringBuilder object to be added on
     * @param totalLength Total length of names
     * @param eventName name of the event
     * @param eventID ID of the event
     * @return the updated total length after appending the event to the StringBuilder result
     */
    private int getTotalLengthStart(StringBuilder result, int totalLength, String eventName, UUID eventID) {
        result.append(" ");
        result.append("ID:").append(eventID).append(" ").append(eventName).append(" Start;");
        String extra = " " + "ID:" + eventID + " " + " Start;";
        totalLength += eventName.length() + extra.length();
        return totalLength;
    }

    /**
     * Add the StringBuilder result with the event information (ID, name + end)
     * Return the updated total length of the event name added
     * @param result StringBuilder object to be added on
     * @param totalLength Total length of names
     * @param eventName name of the event
     * @param eventID ID of the event
     * @return the updated total length after appending the event to the StringBuilder result
     */
    private int getTotalLengthEnd(StringBuilder result, int totalLength, String eventName, UUID eventID) {
        result.append(" ");
        result.append("ID:").append(eventID).append(" ").append(eventName).append(" End;");
        String extra = " " + "ID:" + eventID + " " + " End;";
        totalLength += eventName.length() + extra.length();
        return totalLength;
    }

    /**
     * Add the StringBuilder result with the event information (ID, name + due)
     * Return the updated total length of the event name added
     * @param result StringBuilder object to be added on
     * @param totalLength Total length of names
     * @param eventName name of the event
     * @param eventID ID of the event
     * @return the updated total length after appending the event to the StringBuilder result
     */
    private int getTotalLengthDue(StringBuilder result, int totalLength, String eventName, UUID eventID) {
        result.append(" ");
        result.append("ID:").append(eventID).append(" ").append(eventName).append(" Due;");
        String extra = " " + "ID:" + eventID + " " + " Due;";
        totalLength += eventName.length() + extra.length();
        return totalLength;
    }

    /**
     * Get the start time of the event as a string
     * Get end time if start time does not exist
     * @param event Event object to get start time from
     * @param endTime end time of the event obtained previously
     * @return the string of start time
     */
    private String getStartTime(Event event, String endTime) {
        String startTime = eventManager.getStartTimeString(event);
        if (startTime == null){
            startTime = endTime;
        }
        return startTime;
    }

    /**
     * Get the longest possible outcome of the event name compiled at the same time
     * @return the length of the longest possible name
     */
    private int getLongestEventLength() {
        int longestLength = 0;
        for (String time : this.timeLine) {
            int tempLength = 0;
            for (Event event : this.calendarMap.get(this.date)) {
                String eventEndTime = this.eventManager.getEndTimeString(event);
                String eventName = eventManager.getName(event);
                eventName += " " + "ID:" + eventManager.getID(event) + " " + " Start;";
                String eventStartTime = getStartTime(event, eventEndTime);
                int nameMin = Math.min(eventName.length(),
                        Constants.DAILY_CAL_SIZE);
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
        longestLength = trimLength(longestLength);
        return longestLength;
    }

    /**
     * Trim the longest length by the default space provided
     * @param longestLength possible longest length of the events' names compiled at the same time
     * @return trimmed longest length
     */
    private int trimLength(int longestLength) {
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
