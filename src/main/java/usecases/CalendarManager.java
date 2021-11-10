package usecases;

import entities.OurCalendar;
import entities.Event;

import java.util.*;

public class CalendarManager {
    private final int currentMonth; // current month
    private final int currentDate; // current date
    private final int currentYear; // current year
    private final OurCalendar currentCalendar; // calendar object for the current month
    private final List<OurCalendar> futureCalendar; // List of calendar object for the next three months
    private final List<OurCalendar> pastCalendar; //  List of calendar object for the past three months


    /**
     *  Initialize the usecases.CalendarManager
     */
    public CalendarManager(){
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int date = cal.get(Calendar.DAY_OF_MONTH);
        this.currentMonth = month + 1;
        this.currentDate = date;
        this.currentYear = year;

        // Create a calendar of the current month
        this.currentCalendar = new OurCalendar(this.currentYear, this.currentMonth);

        this.futureCalendar = createCalendarLists(cal, 1);

        cal.setTime(today);

        pastCalendar = createCalendarLists(cal, -1);

    }

    /**
     * helper method for constructing list of OurCalendars
     * interval determines the difference between the current and the next month
     * ( if interval = 1, the next three month will be put in the list
     * if interval = -1, the past three month will be put in the list)
     * @param cal Calendar object with the current date info
     * @param interval The difference between the current and the next month
     * @return list of OurCalendar
     */
    private List<OurCalendar> createCalendarLists(Calendar cal, int interval) {

        cal.add(Calendar.MONTH, interval);
        int firstNextMonth = cal.get(Calendar.MONTH) + 1;
        int firstNextYear = cal.get(Calendar.YEAR);

        cal.add(Calendar.MONTH, interval);
        int secondNextMonth = cal.get(Calendar.MONTH) + 1;
        int secondNextYear = cal.get(Calendar.YEAR);

        cal.add(Calendar.MONTH, interval);
        int thirdNextMonth = cal.get(Calendar.MONTH) + 1;
        int thirdNextYear = cal.get(Calendar.YEAR);

        // Create a calendar of the future 3 months calendar
        OurCalendar firstNext = new OurCalendar(firstNextYear, firstNextMonth);
        OurCalendar secondNext = new OurCalendar(secondNextYear, secondNextMonth);
        OurCalendar thirdNext = new OurCalendar(thirdNextYear, thirdNextMonth);

        return new ArrayList<>(){
            {
                add(firstNext);
                add(secondNext);
                add(thirdNext);
            }};
        }



    /**
     * Observe the chosen calendar to see if there is any conflict
     * @return a list of Events to show the events that are conflicted
     */
    public List<String> notifyConflict(int year, int month) {
        int adjustedMonth = adjustMonth(year, month);
        List<String> eventName = new ArrayList<>();
        // check the chosen calendar
        if (adjustedMonth == this.currentMonth){
            if (this.currentCalendar.isConflict()){
                for (Event event : this.currentCalendar.getConflictEvent()){
                    eventName.add(event.getName());
                }
            }
        }
        else if (adjustedMonth > this.currentMonth && this.currentMonth + 4 > adjustedMonth){
            if (this.futureCalendar.get(adjustedMonth - this.currentMonth - 1).isConflict()){
                for (Event event : this.futureCalendar.get(adjustedMonth - this.currentMonth - 1).getConflictEvent()){
                    eventName.add(event.getName());
                }
            }
        }

        else if (adjustedMonth < this.currentMonth && adjustedMonth + 4 > this.currentMonth) {
            if (this.pastCalendar.get(this.currentMonth - adjustedMonth - 1).isConflict()){
                for (Event event : this.pastCalendar.get(this.currentMonth - adjustedMonth - 1).getConflictEvent()){
                    eventName.add(event.getName());
                }
            }
        }
        return eventName;
    }

    /**
     * adjust the month according to the year
     * if the year is greater than the current year, add more month to match
     * otherwise, subtract month to match
     * @param year year of the given input
     * @param month month of the given input
     * @return adjusted month
     */
    private int adjustMonth(int year, int month) {
        if (year > this.currentYear){
            month = month + 12 * (year - this.currentYear);
        }
        else if (year < this.currentYear){
            month = month - 12 * (this.currentYear - year);
        }
        return month;
    }

    /**
     * Add event to calendar
     * @param event The Event object which is to be added to the calendar
     */
    public void addToCalendar(Event event){
        // Gets the month as an int from event start time (month)
        String eventMonthInfo = event.getEndTime().toString().split("-")[1];
        String eventYearInfo = event.getEndTime().toString().split("-")[0];
        int month = Integer.parseInt(eventMonthInfo);
        int year = Integer.parseInt(eventYearInfo);
        int adjustedMonth = adjustMonth(year, month);
        if (adjustedMonth == this.currentMonth){
            this.currentCalendar.addEvent(event);
            this.currentCalendar.updateConflict();
        }
        for (int i = 0; i < 3; i++){
            if (adjustedMonth == this.currentMonth + i + 1){
                this.futureCalendar.get(i).addEvent(event);
                this.futureCalendar.get(i).updateConflict();
            }
        }

        for (int j = 0; j < 3; j++){
            if (adjustedMonth == this.currentMonth - j - 1){
                this.pastCalendar.get(j).addEvent(event);
                this.pastCalendar.get(j).updateConflict();
            }
        }
    }

    /**
     * remove an event from a specific date
     * @param event event to be removed
     * @param year the year that event will be removed from
     * @param month the month that event will be removed from
     * @param date the date that event will be removed from
     */
    public void removeFromCalendar(Event event, int year, int month, int date){
        int adjustedMonth = adjustMonth(year, month);
        if (adjustedMonth == this.currentMonth){
            this.currentCalendar.removeEvent(event, date);
            this.currentCalendar.updateConflict();
        }
        for (int i = 0; i < 3; i++){
            if (adjustedMonth == this.currentMonth + i + 1){
                this.futureCalendar.get(i).removeEvent(event, date);
                this.futureCalendar.get(i).updateConflict();
            }
        }
        for (int j = 0; j < 3; j++){
            if (adjustedMonth == this.currentMonth - j - 1){
                this.pastCalendar.get(j).removeEvent(event, date);
                this.pastCalendar.get(j).updateConflict();
            }
        }
    }

    /**
     * remove the event from the entire calendar
     * @param event event to be removed
     */
    public void removeFromCalendar(Event event){
        for (int i = 0; i < 3; i++){
            this.currentCalendar.removeEvent(event);
            this.currentCalendar.updateConflict();
            this.futureCalendar.get(i).removeEvent(event);
            this.futureCalendar.get(i).updateConflict();
            this.pastCalendar.get(i).removeEvent(event);
            this.pastCalendar.get(i).updateConflict();
        }
    }

    /**
     * Get all the Events' names from the specific date of the calendar
     * @param year year of the calendar
     * @param month month of the calendar
     * @param date date of the calendar that you want to extract events' names from
     * @return return the list of the events' names
     */
    public List<String> getEventNames(int year, int month, int date) {
        int adjustedMonth = adjustMonth(year, month);
        List<String> nameList = new ArrayList<>();
        if (adjustedMonth == this.currentMonth) {
            for (Event item : this.currentCalendar.getCalendarMap().get(date)) {
                nameList.add(item.getName());
            }
        }
        for (int i = 0; i < 3; i++){
            if (adjustedMonth == this.currentMonth + i + 1){
                for (Event item : this.futureCalendar.get(i).getCalendarMap().get(date)) {
                    nameList.add(item.getName());
                }
            }
        }
        for (int j = 0; j < 3; j++) {
            if (adjustedMonth == this.currentMonth - j - 1) {
                for (Event item : this.pastCalendar.get(j).getCalendarMap().get(date)) {
                    nameList.add(item.getName());
                }
            }
        }
        return nameList;
    }

    /**
     * Get all the Events' time information from the specific date of the calendar
     * @param year year of the calendar
     * @param month month of the calendar
     * @param date date of the calendar that you want to extract events' names from
     * @return return the string of the events' time information
     */
    public List<String> getEventTimes(int year, int month, int date) {
        int adjustedMonth = adjustMonth(year, month);
        List<String> timeList = new ArrayList<>();
        if (adjustedMonth == this.currentMonth) {
            for (Event item : this.currentCalendar.getCalendarMap().get(date)) {
                StringBuilder tempString = generateTimeString(item);
                timeList.add(tempString.toString());
            }
        }
        for (int i = 0; i < 3; i++){
            if (adjustedMonth == this.currentMonth + i + 1){
                for (Event item : this.futureCalendar.get(i).getCalendarMap().get(date)) {
                    StringBuilder tempString = generateTimeString(item);
                    timeList.add(tempString.toString());
                }
            }
        }
        for (int j = 0; j < 3; j++){
            if (adjustedMonth == this.currentMonth - j - 1){
                for (Event item : this.pastCalendar.get(j).getCalendarMap().get(date)) {
                    StringBuilder tempString = generateTimeString(item);
                    timeList.add(tempString.toString());
                }
            }
        }
        return timeList;
    }

    /**
     * helper method for getEventTimes
     * @param item Event item
     * @return String of event's time information (start time - end time)
     */
    private StringBuilder generateTimeString(Event item) {
        StringBuilder tempString = new StringBuilder();
        if (item.getStartTime() != null) {
            tempString.append(item.getStartTime().toString(), 11, 16);
            tempString.append(" - ");
            tempString.append(item.getEndTime().toString(), 11, 16);
        }
        else {
            tempString.append("Due ");
            tempString.append(item.getEndTime().toString(), 11, 16);
        }
        return tempString;
    }

    /**
     * getter for currentDate
     * @return currentDate
     */
    public int getCurrentDate(){
        return this.currentDate;
    }

    /**
     * getter for currentMonth
     * @return currentMonth
     */
    public int getCurrentMonth(){
        return this.currentMonth;
    }

    /**
     * getter for currentYear
     * @return currentYear
     */
    public int getCurrentYear(){
        return this.currentYear;
    }

    public OurCalendar getCurrentCalendar() {
        return this.currentCalendar;
    }
    public List<OurCalendar> getFutureCalendar(){
        return this.futureCalendar;
    }

    public List<OurCalendar> getPastCalendar(){
        return this.pastCalendar;
    }

}



