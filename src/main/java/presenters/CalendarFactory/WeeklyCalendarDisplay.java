package presenters.CalendarFactory;

import helpers.Constants;
import helpers.DisplayCalendarHelper;
import presenters.ConflictPresenters.ConflictDisplay;
import presenters.ConflictPresenters.WeeklyConflictDisplay;
import usecases.calendar.CalendarManager;
import usecases.events.EventManager;
import usecases.calendar.WeeklyCalendarByType;

import java.util.*;

/**
 * Build the image of weekly calendar and display if asked to
 * @author Seo Won Yi
 */
public class WeeklyCalendarDisplay extends CalendarDisplay {
    private final Map<Integer, List<UUID>> calendarMap;
    private final List<String> defaultTimeLine = new ArrayList<>();
    private final DisplayCalendarHelper cf;
    private final ConflictDisplay conflictDisplay;

    /**
     * Initialize the class by setting
     * @param cm CalendarManager object that will provide calendar information
     * @param year given year
     * @param month given month
     * @param date given date
     */
    public WeeklyCalendarDisplay(CalendarManager cm, EventManager em, int year, int month, int date) {
        super(cm, em, year, month, date);
        WeeklyCalendarByType wc = new WeeklyCalendarByType();
        this.calendarMap = wc.getCalendar(cm, year, month, date);
        for (int i = 0; i < 10; i++){
            this.defaultTimeLine.add("0" + i + ":00");
        }
        for (int j = 10; j < 25; j++){
            this.defaultTimeLine.add(j+ ":00");
        }
        this.cf = new DisplayCalendarHelper(this.year, this.month);
        this.conflictDisplay = new WeeklyConflictDisplay(cm, em, year, month, date);
    }

    /**
     * Display the String image of weekly calendar by running various methods
     * @return string image of weekly calendar
     */
    @Override
    public String displayCalendar(){
        int startingDayOfWeek = cf.findStartDayOfWeekInteger(this.year, this.month, this.date);
        cf.eventSorter(calendarMap, em);
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
        result.append(this.conflictDisplay.displayConflict());
        return result.toString();
    }

    /**
     * Size of the calendar (number of dates)
     * @return size of the calendar (number of dates)
     */
    @Override
    public int size() {
        return this.calendarMap.size();
    }

    /**
     * Add timeline and the contents to the calendar
     * @param startingDayOfWeek starting day of week of the weekly calendar
     * @param result StringBuilder object to be added on
     * @param indexOne index of timelines
     * @param indexTwo index of day of weeks
     */
    private void addTimeLineContent(int startingDayOfWeek, StringBuilder result, int indexOne, int indexTwo) {
        List<String> newTimeLine = cf.updateTimeList(this.defaultTimeLine, gatherTimeLine(indexTwo));
        if (newTimeLine.size() > indexOne){
            addTimeLine(result, newTimeLine, indexOne);
            int tempDiv = addContent(result, newTimeLine, indexOne, indexTwo);
            addSpace(result, tempDiv, startingDayOfWeek + indexTwo);
        }
        else {
            result.append("|");
            addSpace(result, Constants.TIMELINE_SPACER, startingDayOfWeek + indexTwo);
        }
    }

    /**
     * Get the length of the longest timeline in the week
     * @return the length of the longest timeline
     */
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

    /**
     * get a list of timelines based on start / end time of events in a day
     * @param index determines which date to access
     * @return list of timelines
     */
    private List<String> gatherTimeLine(int index){

        List<String> temp = new ArrayList<>();
        List<Integer> keyList = getKeys();
        List<Integer> yearMonth = updateYearMonth(keyList.get(index));
        for (UUID eventID: calendarMap.get(keyList.get(index))){
            String startTime = timePresenter.getStartTime(eventID, yearMonth.get(0), yearMonth.get(1), keyList.get(index));
            String endTime = timePresenter.getEndTime(eventID, yearMonth.get(0), yearMonth.get(1), keyList.get(index));
            if (startTime != null) {
                temp.add(startTime);
            }
            temp.add(endTime);
        }
        return temp;
    }

    /**
     * helper method that return the year and month for the given date in a weekly calendar
     * @param date chosen date
     * @return list of year and month information
     */
    private List<Integer> updateYearMonth(int date) {
        List<Integer> result = new ArrayList<>();
        int year = this.year;
        int month = this.month;
        if (this.date > 20 && date < 10) {
            if (month + 1 <= 12) {
                month = month + 1;
            }
            else {
                year = this.year + 1;
                month = 1;
            }
        }
        result.add(year);
        result.add(month);
        result.add(date);
        return result;
    }

    /**
     * Set up the initial frame of weekly calendar
     * @param startingDayOfWeek starting day of week in integer (1 = MONDAY, 7 = SUNDAY etc...)
     * @param result StringBuilder object to be added on
     * @param cf DisplayCalendarHelper object to provide required methods
     */
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

    /**
     * Add dates to the StringBuilder result
     * @param result StringBuilder object to be added on
     * @param dayOfWeek day of week indicator to provide appropriate spacing
     */
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

    /**
     * Helper method of addDate to add dates in the appropriate format
     * @param result StringBuilder object to be added on
     * @param keyList list of days
     * @param tempDiv space to be provided in between
     * @param index index for determining days in keyList
     */
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

    /**
     * Add timeline to the StringBuilder result
     * @param result StringBuilder object to be added on
     * @param timeLine timeline of the day
     * @param index index to track the access a certain timeline
     */
    private void addTimeLine(StringBuilder result, List<String> timeLine, int index){
        result.append("|").append(" ");
        result.append(timeLine.get(index)).append(" |");
    }

    /**
     * Add blank space for appropriate formatting
     * @param result StringBuilder object to be added on
     * @param length total length of events' names
     * @param dayOfWeek day of week when the space will be applied on
     */
    private void addSpace(StringBuilder result, int length, int dayOfWeek){
        int spacer = getSpacer(dayOfWeek);
        String tempDiv = " ".repeat(Constants.CAL_ROW_SPACER + lengthDecider() + spacer - length +
                Constants.TIMELINE_SPACER);
        result.append(tempDiv);
    }

    /**
     * Get appropriate spacing depending on the day of week of the date
     * @param dayOfWeek day of week
     * @return the space needed to cover the day of week
     */
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

    /**
     * Add content (events' names, IDs, etc...) to the StringBuilder result
     * Return the length of the content
     * @param result StringBuilder object to be added on
     * @param timeLine list of timeline
     * @param time index of timeline to specify the time
     * @param index index needed to determine the specific date
     * @return length of the content
     */
    private Integer addContent(StringBuilder result, List<String> timeLine, int time, int index){
        List<Integer> keyList = getKeys();
        int nameLength = 0;
        List<Integer> yearMonth = updateYearMonth(keyList.get(index));
        for (UUID eventID: calendarMap.get(keyList.get(index))){
            String eventStartTime = timePresenter.getStartTime(eventID, yearMonth.get(0), yearMonth.get(1), keyList.get(index));
            String eventEndTime = timePresenter.getEndTime(eventID, yearMonth.get(0), yearMonth.get(1), keyList.get(index));
            if (eventStartTime == null) {
                eventStartTime = eventEndTime;
            }
            if (cf.convertTimeToInt(timeLine.get(time)) >= cf.convertTimeToInt(eventStartTime)
                && cf.convertTimeToInt(timeLine.get(time)) <= cf.convertTimeToInt(eventEndTime)) {
                nameLength = appendNameGetNameLength(result, nameLength, eventID, eventStartTime);
            }
        }
        return nameLength;
    }

    /**
     * Helper method of addContent. Add event's information to the StringBuilder result and get its length
     * @param result StringBuilder object to be added on
     * @param nameLength length of the name
     * @param eventID ID of an event that will be access
     * @param eventStartTime start time of the event
     * @return length of the event's information
     */
    private int appendNameGetNameLength(StringBuilder result, int nameLength, UUID eventID, String eventStartTime) {
        String eventName = em.getDefaultEventInfoGetter().getName(em.get(eventID));
        int eventIntID = this.converter.getIntFromUUID(eventID);
        eventName = "ID:" + eventIntID + " " + eventName;
        int eventNameSize = eventName.length();
        if (eventStartTime.equals(em.getDefaultEventInfoGetter().getEndTimeString(eventID))){
            eventName = eventName + " Due";
            eventNameSize = eventName.length();
            if (eventNameSize >= Constants.WEEKLY_CAL_NAME_LIMIT){
                eventName = eventName.substring(0, Constants.WEEKLY_CAL_NAME_LIMIT - 9) + "..." + " Due";
                eventNameSize = eventName.length();
            }
        }
        if (eventNameSize <= Constants.WEEKLY_CAL_NAME_LIMIT - 2){
            result.append(" ").append(eventName).append(";");
            nameLength += eventNameSize + 2;
        }
        else {
            result.append(" ").append(eventName, 0, Constants.WEEKLY_CAL_NAME_LIMIT - 5)
                    .append("...").append(";");
            nameLength += Constants.WEEKLY_CAL_NAME_LIMIT;}
        return nameLength;
    }

    /**
     * Get the list of dates in the right order
     * For example, if Nov 29, Nov 30, Dec 1, Dec 2, ..., the order should be 29, 30, 1, 2, ...
     * @return the sorted list of dates
     */
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

    /**
     * Helper method of getKey. Divide up the Dates and add to two different list to help to sort
     * @param tempOne list of dates less than 10
     * @param tempTwo list of dates larger than 20
     * @param items date
     */
    private void divideDate(List<Integer> tempOne, List<Integer> tempTwo, Integer items) {
        if (0 < items && items < 10){
            tempOne.add(items);
        }
        else if (20 < items){
            tempTwo.add(items);
        }
    }

    /**
     * Decide if the calendar needs extra space to display the entire content
     * @return the required extra horizontal space
     */
    private int lengthDecider(){
        List<Integer> keyList = getKeys();
        int temp = 0;
        for (Integer number: keyList){
            List<UUID> eventIDList = calendarMap.get(number);
            for (int i = 0; i < eventIDList.size(); i++) {
                int totalLength = Constants.WEEKLY_CAL_NAME_LIMIT;
                totalLength = getTotalLength(eventIDList, totalLength, i);
                if (temp < totalLength) {
                    temp = totalLength;
                }
            }
        }
        int INITIAL_SPACE = Constants.CAL_ROW_SPACER - Constants.WEEKLY_CAL_NAME_LIMIT;
        if (temp >= Constants.CAL_ROW_SPACER - INITIAL_SPACE){
            temp -= Constants.CAL_ROW_SPACER - INITIAL_SPACE;
        }
        else {
            temp = 0;
        }
        if (temp % 2 == 1){
            temp += 1;
        }
        return temp;
    }

    /**
     * Update totalLength of events' contents based on the events' information that are happening at the same timeline.
     * @param eventIDList List of event IDs to access
     * @param totalLength Previous total length
     * @param index index of eventList that were chosen previously at the method lengthDecider
     * @return the updated totalLength of event's contents
     */
    private int getTotalLength(List<UUID> eventIDList, int totalLength, int index) {
        for (int j = index + 1; j < eventIDList.size(); j++){
            String startTimeStringOne = em.getStartTimeString(eventIDList.get(index));
            String startTimeStringTwo = em.getStartTimeString(eventIDList.get(j));
            String eventName = em.getDefaultEventInfoGetter().getName(em.get(eventIDList.get(j)));
            eventName = "ID:" + eventIDList.get(j) + " " + eventName;
            if (em.getStartTimeString(eventIDList.get(index)) == null){
                startTimeStringOne = em.getDefaultEventInfoGetter().getEndTimeString(eventIDList.get(index));
            }
            if (em.getStartTimeString(eventIDList.get(j)) == null){
                startTimeStringTwo = em.getDefaultEventInfoGetter().getEndTimeString(eventIDList.get(j));
                eventName += " Due";
            }
            if (cf.convertTimeToInt(startTimeStringOne)
                    <= cf.convertTimeToInt(startTimeStringTwo)
                    && cf.convertTimeToInt(em.getDefaultEventInfoGetter().getEndTimeString(eventIDList.get(index)))
                    >= cf.convertTimeToInt(startTimeStringTwo)){
                totalLength += Math.min(eventName.length() + 2,
                        Constants.WEEKLY_CAL_NAME_LIMIT);
            }
        }
        return totalLength;

    }
}
