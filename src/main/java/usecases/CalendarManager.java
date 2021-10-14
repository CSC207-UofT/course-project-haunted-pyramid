package usecases;

import entities.OurCalendar;
import entities.Event;

import java.time.LocalDateTime;
import java.util.*;

public class CalendarManager {

    private OurCalendar currentCalendar; // calendar object for the current month
    private List<OurCalendar> futureCalendar; // List of calendar object for the past three months
    private List<OurCalendar> pastCalendar; //  List of calendar object for the next three months
    private final int currentMonth; // current month
    private final int currentDate; // current date
    private final int currentYear; // current year

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
        this.currentCalendar = new OurCalendar(year, month + 1);

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
     * Return Monthly Calendar
     * When there is no argument, return the current month calendar
     * @return Map of the current month calendar
     */
    public Map<Integer, List<Event>> getMonthlyCalendar(){
        return this.currentCalendar.getCalendarMap();
    }

    /**
     * Return Monthly Calendar for the given month and year
     *
     * == Representation Invariant ==
     * month <= currentMonth + 3
     * month >= currentMonth - 3
     * @param month a chosen month for the calendar
     * @return Map of the chosen month calendar
     */
    public Map<Integer, List<Event>> getMonthlyCalendar(int year, int month){
        if (year > this.currentYear){
            month = month + 12;
        }
        else if (year < this.currentYear){
            month = month - 12;
        }
        if (month == this.currentMonth){
            return this.currentCalendar.getCalendarMap();
        }
        else if (month > this.currentMonth && month - this.currentMonth <= 3){
            return this.futureCalendar.get(month - this.currentMonth - 1).getCalendarMap();
        }
        else {
            return this.pastCalendar.get((this.currentMonth - month) - 1).getCalendarMap();
        }
    }

    /**
     * Get weekly calender for the given year, month, and date
     * @param year year of the desired weekly calendar
     * @param month month of the desired weekly calendar
     * @param date starting date of the desired weekly calendar
     * @return map of weekly calendar starting from the desired date
     */
    public Map<Integer, List<Event>> getWeeklyCalendar(int year, int month, int date){
        if (year > this.currentYear){
            month = month + 12;
        }
        else if (year < this.currentYear){
            month = month - 12;
        }
        int currentMonth = this.currentMonth;
        Map<Integer, List<Event>> result = new HashMap<>();
        if (currentMonth == month){
            Map<Integer, List<Event>> currentCal = this.currentCalendar.getCalendarMap();
            int numTotalDays = this.currentCalendar.getDateInfo().get(2);
            weeklyCalGenerator(date, result, currentCal, numTotalDays, 0, this.futureCalendar);
        }
        else if (currentMonth < month && month - currentMonth <= 3){
            Map<Integer, List<Event>> futureCal = this.futureCalendar.get(month - currentMonth - 1).getCalendarMap();
            int numTotalDays = this.futureCalendar.get(month - currentMonth - 1).getDateInfo().get(2);
            if (month - currentMonth == 3 && date > numTotalDays - 6){
                for (int i = date; i <= numTotalDays; i++){
                    result.put(i, futureCal.get(i));
                }
            }
            else {
                weeklyCalGenerator(date, result, futureCal, numTotalDays, month - currentMonth,
                        this.futureCalendar);
            }
        }
        else if (currentMonth > month && currentMonth - month <= 3){
            Map<Integer, List<Event>> pastCal = this.pastCalendar.get(currentMonth - month - 1).getCalendarMap();
            int numTotalDays = this.pastCalendar.get(currentMonth - month - 1).getDateInfo().get(2);
            if (currentMonth - month == 3 && date > numTotalDays - 6){
                int shortage = 7 - numTotalDays - date + 1;
                for (int j = date; j <= numTotalDays; j++){
                    result.put(j, pastCal.get(j));
                }
                for (int k = 1; k <= shortage; k++){
                    result.put(k, this.currentCalendar.getCalendarMap().get(k));
                }
            }
            else {
                weeklyCalGenerator(date, result, pastCal, numTotalDays, currentMonth - month - 2,
                        this.pastCalendar);
            }
        }
        return result;
    }

    /**
     * returns a weekly calendar starting from today
     * @return map of weekly calendar which starts from today
     */
    public Map<Integer, List<Event>> getWeeklyCalendar(){
        return getWeeklyCalendar(this.currentYear, this.currentMonth, this.currentDate);
    }

    /**
     * helper method for the getWeeklyCalendar Method
     * tackles number of situations
     * 1. if the date is less than numTotalDays - 6, appends all the weekly information starting from date to result
     * 2. otherwise, append the current month's weekly information starting from date to result
     * and append next month's weekly information starting from 1 (two combined should equal to seven days)
     * @param date given date of the calendar
     * @param result a map that will get updated as a result
     * @param cal list of events that will be added to result
     * @param numTotalDays totdl number of the day in the chosen month
     * @param index indexing for the calendar objects
     * @param futurePast Either this.futureCalendar or this.pastCalendar
     */
    private void weeklyCalGenerator(int date, Map<Integer, List<Event>> result, Map<Integer,
            List<Event>> cal, int numTotalDays, int index, List<OurCalendar> futurePast) {

        if (date <= numTotalDays - 6){
            for (int i = date; i < date + 7; i++){
                result.put(i, cal.get(i));
            }
        }
        else {
            int shortage = 7 - (numTotalDays - date + 1);
            for (int j = date; j <= numTotalDays; j++){
                result.put(j, cal.get(j));
                for (int k = 1; k <= shortage; k++){
                    Map<Integer, List<Event>> nextMonthCal = futurePast.get(index).getCalendarMap();
                    result.put(k, nextMonthCal.get(k));
                }
            }
        }
    }

    public Map<Integer, List<Event>> getDailyCalendar(int year, int month, int date){
        if (year > this.currentYear){
            month = month + 12;
        }
        else if (year < this.currentYear){
            month = month - 12;
        }
        Map<Integer, List<Event>> result = new HashMap<>();
        if (month == this.currentMonth){
            result.put(date, this.currentCalendar.getCalendarMap().get(date));
        }
        else if (month > this.currentMonth && month - this.currentMonth <= 3){
            OurCalendar futureCal = this.futureCalendar.get(month - this.currentMonth -1);
            result.put(date, futureCal.getCalendarMap().get(date));
        }
        else if (month < this.currentMonth && this.currentMonth - month <= 3){
            OurCalendar pastCal = this.pastCalendar.get(this.currentMonth - month - 1);
            result.put(date, pastCal.getCalendarMap().get(date));
        }
        return result;
    }

    /**
     * Observe the future calendars and the current calendar to see if there is any conflict
     * Also check what months contain conflict AND
     * check what are the events that are involved in the conflict
     * @return a list of (boolean, list of Events, list of months) to show
     * if there is a conflict, what events are involved, and what months are involved respectively
     * if first element of the list is false, second and third elements are empty
     */
    public List<Object> notifyConflict() {
        // initialize all the variables
        boolean conflictCheck = false;
        ArrayList<Event> conflictEvent = new ArrayList<>();
        ArrayList<Integer> monthCollection = new ArrayList<>();
        // check the future calendars
        for (OurCalendar ourCalendar : this.futureCalendar) {
            // if any of the calendar has conflict, make appropriate changes
            if (ourCalendar.isConflict()) {
                conflictCheck = true;
                monthCollection.add(ourCalendar.getDateInfo().get(1));
                for (Event item : ourCalendar.getConflictEvent()) {
                    if (!conflictEvent.contains(item)) {
                        conflictEvent.add(item);
                    }
                }
            }
        }
        // check the current calendar and do the same if conflicted
        if (this.currentCalendar.isConflict()) {
            conflictCheck = true;
            monthCollection.add(this.currentCalendar.getDateInfo().get(1));
            for (Event item : this.currentCalendar.getConflictEvent()) {
                if (!conflictEvent.contains(item)) {
                    conflictEvent.add(item);
                }
            }
        }
        // combine them
        boolean finalConflictCheck = conflictCheck;
        return new ArrayList<>() {
            {
                add(finalConflictCheck);
                add(conflictEvent);
                add(monthCollection);
            }
        };
    }

    /**
     * Add event to calendar
     * @param event The Event object which is to be added to the calendar
     */
    public void addToCalendar(Event event){
        // Gets the month as an int from event start time (month)
        String m = event.getStartString().split("-")[1];
        int month = Integer.parseInt(m);

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

    public static void main(String[] args) {
        CalendarManager cm = new CalendarManager();
        System.out.println(cm.currentDate);
        System.out.println(cm.currentMonth);
        System.out.println(cm.currentYear);
        System.out.println("current month info " + cm.currentCalendar.getDateInfo());
        System.out.println("one month after info " + cm.futureCalendar.get(0).getDateInfo());
        System.out.println("two month after info " + cm.futureCalendar.get(1).getDateInfo());
        System.out.println("three month after info " + cm.futureCalendar.get(2).getDateInfo());
        System.out.println("one month before info " + cm.pastCalendar.get(0).getDateInfo());
        System.out.println("two month before info " + cm.pastCalendar.get(1).getDateInfo());
        System.out.println("three month before info " + cm.pastCalendar.get(2).getDateInfo());
        System.out.println("this shows the current month calendar map " + cm.getMonthlyCalendar());
        System.out.println("this shows the calendar map of January " + cm.getMonthlyCalendar(2022,1));
        System.out.println("this shows the calendar map of November " + cm.getMonthlyCalendar(2021,11));
        System.out.println("this shows the calendar map of July " + cm.getMonthlyCalendar(2021,7));
        System.out.println(cm.getWeeklyCalendar());
        System.out.println(cm.getWeeklyCalendar(2021, 10, 5));
        System.out.println(cm.getWeeklyCalendar(2021, 10, 14));
        System.out.println(cm.getDailyCalendar(2022, 1, 15));
        Event event = new Event(1, "My entities.Event", LocalDateTime.of(2022, 1, 3, 1, 0,
                0), LocalDateTime.of(2022, 1, 3, 3, 30, 0));
        cm.futureCalendar.get(2).addEvent(event);
        System.out.println(cm.getDailyCalendar(2022, 1, 3));
        System.out.println(cm.getWeeklyCalendar(2021, 12, 29));
        System.out.println(cm.getMonthlyCalendar(2022,1));
        Event e2 = new Event(1, "T", LocalDateTime.of(2021, 10, 1,
                1, 0), LocalDateTime.of(2021, 10, 1, 3, 30, 0));
        System.out.println(e2.getStartString());
        System.out.println(cm.currentCalendar.getDateInfo());
        cm.addToCalendar(e2);
        System.out.println(cm.getMonthlyCalendar(2021, 10));

    }

}

