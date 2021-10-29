package usecases;

import entities.OurCalendar;
import entities.Event;

import java.util.*;

public class CalendarManager {
    private final int currentMonth; // current month
    private final int currentDate; // current date
    private final int currentYear; // current year
    private final OurCalendar currentCalendar; // calendar object for the current month
    private final List<OurCalendar> futureCalendar; // List of calendar object for the past three months
    private final List<OurCalendar> pastCalendar; //  List of calendar object for the next three months


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

        cal.add(Calendar.MONTH, 1);
        int firstNextMonth = cal.get(Calendar.MONTH);
        int firstNextYear = cal.get(Calendar.YEAR);

        cal.add(Calendar.MONTH, 1);
        int secondNextMonth = cal.get(Calendar.MONTH);
        int secondNextYear = cal.get(Calendar.YEAR);

        cal.add(Calendar.MONTH, 1);
        int thirdNextMonth = cal.get(Calendar.MONTH);
        int thirdNextYear = cal.get(Calendar.YEAR);

        // Create a calendar of the future 3 months calendar
        OurCalendar firstFuture = new OurCalendar(firstNextYear, firstNextMonth + 1);
        OurCalendar secondFuture = new OurCalendar(secondNextYear, secondNextMonth + 1);
        OurCalendar thirdFuture = new OurCalendar(thirdNextYear, thirdNextMonth + 1);

        this.futureCalendar = new ArrayList<>(){
            {
                add(firstFuture);
                add(secondFuture);
                add(thirdFuture);
            }
        };


        Calendar c = Calendar.getInstance();
        c.setTime(today);

        c.add(Calendar.MONTH, -1);
        int firstPreviousMonth = c.get(Calendar.MONTH);
        int firstPreviousYear = c.get(Calendar.YEAR);

        c.add(Calendar.MONTH, -1);
        int secondPreviousMonth = c.get(Calendar.MONTH);
        int secondPreviousYear = c.get(Calendar.YEAR);

        c.add(Calendar.MONTH, -1);
        int thirdPreviousMonth = c.get(Calendar.MONTH);
        int thirdPreviousYear = c.get(Calendar.YEAR);

        // Create a calendar of the past 3 months calendar
        OurCalendar firstPast = new OurCalendar(firstPreviousYear, firstPreviousMonth + 1);
        OurCalendar secondPast = new OurCalendar(secondPreviousYear, secondPreviousMonth + 1);
        OurCalendar thirdPast = new OurCalendar(thirdPreviousYear, thirdPreviousMonth + 1);

        this.pastCalendar = new ArrayList<>(){
            {
                add(firstPast);
                add(secondPast);
                add(thirdPast);
            }
        };

    }


    /**
     * Observe the chosen calendar to see if there is any conflict
     * @return a list of Events to show the events that are conflicted
     */
    public List<String> notifyConflict(int year, int month) {
        if (year > this.currentYear){
            month = month + 12;
        }
        else if (year < this.currentYear){
            month = month - 12;
        }
        List<String> eventName = new ArrayList<>();
        // check the chosen calendar
        if (month == this.currentMonth){
            if (this.currentCalendar.isConflict()){
                for (Event event : this.currentCalendar.getConflictEvent()){
                    eventName.add(event.getName());
                }
            }
        }
        else if (month > this.currentMonth && this.currentMonth + 4 > month){
            if (this.futureCalendar.get(month - this.currentMonth - 1).isConflict()){
                for (Event event : this.futureCalendar.get(month - this.currentMonth - 1).getConflictEvent()){
                    eventName.add(event.getName());
                }
            }
        }

        else if (month < this.currentMonth && month + 4 > this.currentMonth) {
            if (this.pastCalendar.get(this.currentMonth - month - 1).isConflict()){
                for (Event event : this.pastCalendar.get(this.currentMonth - month - 1).getConflictEvent()){
                    eventName.add(event.getName());
                }
            }
        }
        return eventName;
    }

    /**
     * Add event to calendar
     * @param event The Event object which is to be added to the calendar
     */
    public void addToCalendar(Event event){
        // Gets the month as an int from event start time (month)
        String m = event.getStartString().split("-")[1];
        int month = Integer.parseInt(m);
        int year = Integer.parseInt(event.getStartString().split("-")[0]);
        if (year == this.currentYear + 1){
            month = month + 12;
        }
        if (month == this.currentMonth){
            this.currentCalendar.addEvent(event);
            this.currentCalendar.updateConflict();
        }
        else if (month == this.currentMonth + 1){
            this.futureCalendar.get(0).addEvent(event);
            this.futureCalendar.get(0).updateConflict();
        }
        else if (month == this.currentMonth + 2){
            this.futureCalendar.get(1).addEvent(event);
            this.futureCalendar.get(1).updateConflict();
        }
        else if (month == this.currentMonth + 3){
            this.futureCalendar.get(2).addEvent(event);
            this.futureCalendar.get(2).updateConflict();
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
        if (year == this.currentYear + 1){
            month = month + 12;
        }
        else if (year == this.currentYear - 1){
            month = month - 12;
        }
        if (month == this.currentMonth){
            this.currentCalendar.removeEvent(event, date);
            this.currentCalendar.updateConflict();
        }
        else if (month == this.currentMonth + 1){
            this.futureCalendar.get(0).removeEvent(event, date);
            this.futureCalendar.get(0).updateConflict();
        }
        else if (month == this.currentMonth + 2){
            this.futureCalendar.get(1).removeEvent(event, date);
            this.futureCalendar.get(1).updateConflict();
        }
        else if (month == this.currentMonth + 3){
            this.futureCalendar.get(2).removeEvent(event, date);
            this.futureCalendar.get(2).updateConflict();
        }
        else if (month == this.currentMonth - 1){
            this.pastCalendar.get(0).removeEvent(event, date);
            this.pastCalendar.get(0).updateConflict();
        }
        else if (month == this.currentMonth - 2){
            this.pastCalendar.get(1).removeEvent(event, date);
            this.pastCalendar.get(1).updateConflict();
        }
        else if (month == this.currentMonth - 3){
            this.pastCalendar.get(2).removeEvent(event, date);
            this.pastCalendar.get(2).updateConflict();
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
        if (year == this.currentYear + 1) {
            month = month + 12;
        } else if (year == this.currentYear - 1) {
            month = month - 12;
        }
        List<String> nameList = new ArrayList<>();
        if (month == this.currentMonth) {
            for (Event item : this.currentCalendar.getCalendarMap().get(date)) {
                nameList.add(item.getName());
            }
        } else if (month == this.currentMonth + 1) {
            for (Event item : this.futureCalendar.get(0).getCalendarMap().get(date)) {
                nameList.add(item.getName());
            }
        } else if (month == this.currentMonth + 2) {
            for (Event item : this.futureCalendar.get(1).getCalendarMap().get(date)) {
                nameList.add(item.getName());
            }
        } else if (month == this.currentMonth + 3) {
            for (Event item : this.futureCalendar.get(2).getCalendarMap().get(date)) {
                nameList.add(item.getName());
            }
        } else if (month == this.currentMonth - 1) {
            for (Event item : this.pastCalendar.get(0).getCalendarMap().get(date)) {
                nameList.add(item.getName());
            }
        } else if (month == this.currentMonth - 2) {
            for (Event item : this.pastCalendar.get(1).getCalendarMap().get(date)) {
                nameList.add(item.getName());
            }
        } else if (month == this.currentMonth - 3) {
            for (Event item : this.pastCalendar.get(2).getCalendarMap().get(date)) {
                nameList.add(item.getName());
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
    public List<StringBuilder> getEventTimes(int year, int month, int date) {
        if (year == this.currentYear + 1) {
            month = month + 12;
        } else if (year == this.currentYear - 1) {
            month = month - 12;
        }
        List<StringBuilder> timeList = new ArrayList<>();
        if (month == this.currentMonth) {
            for (Event item : this.currentCalendar.getCalendarMap().get(date)) {
                StringBuilder tempString = generateTimeString(item);
                timeList.add(tempString);
            }
        } else if (month == this.currentMonth + 1) {
            for (Event item : this.futureCalendar.get(0).getCalendarMap().get(date)) {
                StringBuilder tempString = generateTimeString(item);
                timeList.add(tempString);
            }
        } else if (month == this.currentMonth + 2) {
            for (Event item : this.futureCalendar.get(1).getCalendarMap().get(date)) {
                StringBuilder tempString = generateTimeString(item);
                timeList.add(tempString);
            }
        } else if (month == this.currentMonth + 3) {
            for (Event item : this.futureCalendar.get(2).getCalendarMap().get(date)) {
                StringBuilder tempString = generateTimeString(item);
                timeList.add(tempString);
            }
        } else if (month == this.currentMonth - 1) {
            for (Event item : this.pastCalendar.get(0).getCalendarMap().get(date)) {
                StringBuilder tempString = generateTimeString(item);
                timeList.add(tempString);
            }
        } else if (month == this.currentMonth - 2) {
            for (Event item : this.pastCalendar.get(1).getCalendarMap().get(date)) {
                StringBuilder tempString = generateTimeString(item);
                timeList.add(tempString);
            }
        } else if (month == this.currentMonth - 3) {
            for (Event item : this.pastCalendar.get(2).getCalendarMap().get(date)) {
                StringBuilder tempString = generateTimeString(item);
                timeList.add(tempString);
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
        tempString.append(item.getStartString(), 11, 16);
        tempString.append(" - ");
        tempString.append(item.getEndString(), 11, 16);
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



