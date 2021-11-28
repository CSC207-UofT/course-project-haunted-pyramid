package usecases.calendar;

import entities.OurCalendar;

import java.util.*;

/**
 * Use case of OurCalendar class that will be used for various calendar related classes
 * @author Seo Won Yi
 * @author Shahzada Muhammad Shameel Farooq
 * @see OurCalendar
 */
public class CalendarManager {
    private final int currentMonth; // current month
    private final int currentDate; // current date
    private final int currentYear; // current year
    private final OurCalendar currentCalendar; // calendar object for the current month
    private final List<OurCalendar> futureCalendar; // List of calendar object for the next three months
    private final List<OurCalendar> pastCalendar; //  List of calendar object for the past three months

    /**
     *  Initialize the usecases.calendar.CalendarManager
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
        this.pastCalendar = createCalendarLists(cal, -1);
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
     * adjust the month according to the year
     * if the year is greater than the current year, add more month to match
     * otherwise, subtract month to match
     * @param year year of the given input
     * @param month month of the given input
     * @return adjusted month
     */
    public int adjustMonth(int year, int month) {
        if (year > this.currentYear){
            month = month + 12 * (year - this.currentYear);
        }
        else if (year < this.currentYear){
            month = month - 12 * (this.currentYear - year);
        }
        return month;
    }

    /**
     * add eventID to the calendar with given year, month and date
     * @param eventID event ID to add in the calendar
     * @param year year of the event
     * @param month month of the event
     * @param date date of the event
     */
    public void addToCalendar(UUID eventID, int year, int month, int date){
        // Gets the month as an int from event start time (month)
        int adjustedMonth = adjustMonth(year, month);
        if (adjustedMonth == this.currentMonth){
            this.currentCalendar.addEventID(eventID, date);
        }
        for (int i = 0; i < 3; i++){
            if (adjustedMonth == this.currentMonth + i + 1){
                this.futureCalendar.get(i).addEventID(eventID, date);
            }
        }

        for (int j = 0; j < 3; j++){
            if (adjustedMonth == this.currentMonth - j - 1){
                this.pastCalendar.get(j).addEventID(eventID, date);
            }
        }
    }

    /**
     * Get all the Events' ID information from the specific date of the calendar
     * @param year year of the calendar
     * @param month month of the calendar
     * @param date date of the calendar that ID's will get extracted from
     * @return the list of ID's in the specific date
     */
    public List<UUID> getEventID(int year, int month, int date) {
        int adjustedMonth = adjustMonth(year, month);
        List<UUID> listID = new ArrayList<>();
        if (adjustedMonth == this.currentMonth) {
            listID.addAll(this.currentCalendar.getCalendarMap().get(date));
        }
        for (int i = 0; i < 3; i++){
            if (adjustedMonth == this.currentMonth + i + 1){
                listID.addAll(this.futureCalendar.get(i).getCalendarMap().get(date));
            }
        }
        for (int j = 0; j < 3; j++){
            if (adjustedMonth == this.currentMonth - j - 1){
                listID.addAll(this.pastCalendar.get(j).getCalendarMap().get(date));
            }
        }
        return listID;
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



