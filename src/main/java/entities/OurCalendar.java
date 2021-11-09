package entities;

import java.util.*;
import java.time.YearMonth;
import helpers.IsOverlapped;

public class OurCalendar {

    private final List<Integer> dateInfo; //in the form of [year, month, # of days in the month]
    private boolean conflict;  // if the calendar has any conflicted information
    private List<Event> conflictEvent; // All the objects that are conflicting
    private final Map<Integer, List<Event>> calendarMap; // map of calendar


    /**
     * Initialize the OurCalendar class for the given month.
     * If not year and month are provided, create based on the current year and month
     * @param year year of the calendar
     * @param month month of the calendar
     */
    // if provided a year and month and date, create a calendar that matches that year and month
    public OurCalendar(int year, int month) {
        YearMonth tempYearMonth = YearMonth.of(year, month);
        int daysInMonth = tempYearMonth.lengthOfMonth(); // # of days in the given month
        // store the [year, month, # of days in the month] information
        this.dateInfo = new ArrayList<>() {
            {
                add(year);
                add(month);
                add(daysInMonth);
            }
        };

        // no conflict because empty
        this.conflict = false;
        // empty list of conflicted object to start with
        this.conflictEvent = new ArrayList<>();
        // create an empty calendar map with keys of days and values of list of events (empty to start with) for the
        // provided year and month
        Map<Integer, List<Event>> tempMap = new HashMap<>();
        for (int i = 1; i <= daysInMonth; i++) {
            tempMap.put(i, new ArrayList<>());
        }
        this.calendarMap = tempMap;
    }

    // if no argument, create the current month calendar
    public OurCalendar(){
        GregorianCalendar temp = new GregorianCalendar();
        OurCalendar tempCalendar = new OurCalendar(temp.get(Calendar.YEAR), temp.get(Calendar.MONTH) + 1);
        this.dateInfo = tempCalendar.dateInfo;
        this.conflict = tempCalendar.conflict;
        this.conflictEvent = tempCalendar.conflictEvent;
        this.calendarMap = tempCalendar.calendarMap;
    }

    /**
     * Check if there is any conflict for the current calendar
     * @return true if there exists conflict
     */
    public boolean isConflict(){
        return this.conflict;
    }

    /**
     * get conflictObject
     * @return conflictObject
     */
    public List<Event> getConflictEvent(){
        return this.conflictEvent;
    }

    /**
     * get dateInfo
     * @return dateInfo
     */
    public List<Integer> getDateInfo(){
        return this.dateInfo;
    }

    /**
     * get calendarMap
     * @return calendarMap
     */
    public Map<Integer, List<Event>> getCalendarMap(){
        return this.calendarMap;
    }

    /**
     * Add the event to a calendar for the appropriate dates
     *
     * == Representation Invariant ==
     *
     * the event is set up for the same month as the schedule
     *
     * @param event the event that wants to be added
     */

    public void addEvent(Event event){
        // get starting timeline info
        String startInfo = event.getStartString();
        // get ending timeline info
        String endInfo = event.getEndString();
        // extract start date and convert it to int
        int startDate = Integer.parseInt(startInfo.substring(8, 10));
        // extract end date and convert it to int
        int endDate = Integer.parseInt(endInfo.substring(8, 10));
        // collect all dates that the event will happen
        List<Integer> applicableDates = new ArrayList<>();
        for (int i = startDate; i <= endDate; i++){
            applicableDates.add(i);
        }
        // add the event to all dates that apply
        for (int dates : applicableDates){
            this.calendarMap.get(dates).add(event);
        }
    }

    /**
     * check if there is any conflict in the calendar (monthly)
     * if there is any conflict, update conflictEvent with conflicted events
     * also change conflict attribute to true
     * otherwise, reset conflict related attributes to default
     */
    public void updateConflict(){
        // iterate each day
        for (int date = 1; date <= this.dateInfo.get(2); date++){
            // create a temporary list to store [start time, end time] info
            List<List<Double>> timeInfo = new ArrayList<>();
            // iterate each event in the list
            for (Event item :this.calendarMap.get(date)) {
                // store start time and end time of the event as a list
                List<Double> individualTimeInfo = new ArrayList<>();
                individualTimeInfo.add(item.startTimeDouble());
                individualTimeInfo.add(item.startTimeDouble() + item.getLength() * 100);
                timeInfo.add(individualTimeInfo);
            }
            // run the helper method to compare the times and update the conflictEvent
            checkOverLap(timeInfo, date);
        }
        // if there is any kind of conflict return true
        if (this.conflictEvent.size() != 0){
            this.conflict = true;
        }
        // if there is no conflict at all
        // return false and set the conflict related attributes to default
        else {
            this.conflict = false;
            this.conflictEvent = new ArrayList<>();
        }
    }

    /**
     * Helper method of updateConflict
     * Compare the lists in time info using the class isOverLapped
     * If any items are overlapping, add the items to the conflictEvent
     * @param timeInfo the list that contains the list of start and end times
     * @param date date of the calendar that we are checking for conflict
     */
    private void checkOverLap(List<List<Double>> timeInfo, int date) {
        for (int j = 0; j < (timeInfo.size() - 1); j++){
            List<List<Double>> timeSubList = timeInfo.subList(j + 1, timeInfo.size());
            for (int k = 0; k < timeSubList.size(); k++){
                IsOverlapped check = new IsOverlapped(timeInfo.get(j), timeSubList.get(k));
                // if overlaps store the information needed
                if (check.getResult()){
                    if (!(this.conflictEvent.contains(this.calendarMap.get(date).get(j)))){
                        this.conflictEvent.add(this.calendarMap.get(date).get(j));
                    }
                    if (!(this.conflictEvent.contains(this.calendarMap.get(date).get(j + k + 1)))){
                        this.conflictEvent.add(this.calendarMap.get(date).get(j + k + 1));
                    }
                }
            }
        }
    }

    /**
     * Remove an event
     * If there is no such event, do nothing
     * @param event event that will be removed
     */
    public void removeEvent(Event event) {
        for (int i = 1; i <= this.dateInfo.get(2); i++) {
            this.calendarMap.get(i).removeIf(item -> item == event);
        }
    }

    /**
     * Remove an event for the specific date
     * @param event event that will be removed
     * @param date date that the event will be removed from
     */
    public void removeEvent(Event event, int date){
        this.calendarMap.get(date).removeIf(item -> item == event);
    }
}

