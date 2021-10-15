package entities;

import java.util.*;
import java.time.YearMonth;
import helpers.IsOverlapped;

public class OurCalendar {

    private List<Event> conflictEvent; // All the objects that are conflicting
    private boolean conflict;  // if the calendar has any conflicted information
    private final Map<Integer, List<Event>> calendarMap; // map of calendar
    private final List<Integer> dateInfo; //in the form of [year, month, # of days in the month]


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
        this.conflictEvent = tempCalendar.conflictEvent;
        this.conflict = tempCalendar.conflict;
        this.calendarMap = tempCalendar.calendarMap;
        this.dateInfo = tempCalendar.dateInfo;
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
        // collection of checks for each day in the month
        List<Boolean> checkCollection = new ArrayList<>();
        // iterate each day
        for (int i = 1; i <= this.dateInfo.get(2); i++){
            // create a temporary list to store [start time, end time] info
            List<List<Double>> timeInfo = new ArrayList<>();
            // iterate each event in the list
            for (Event item :this.calendarMap.get(i)) {
                // store start time and end time of the event as a list
                List<Double> individualTimeInfo = new ArrayList<>();
                // if the start date & end date are not the same as the event happening
                // it means the event happens all day
                if (!(Integer.parseInt(item.getStartString().substring(8, 10)) == i)
                        && !(Integer.parseInt(item.getEndString().substring(8, 10)) == i)) {
                    individualTimeInfo.add(0.0);
                    individualTimeInfo.add(2400.0);
                }
                // if the event's start date is not the same but end date is the same
                // event happens from 0 (start of the day) to the end time
                else if (!(Integer.parseInt(item.getStartString().substring(8, 10)) == i)) {
                    individualTimeInfo.add(0.0);
                    individualTimeInfo.add(item.endTimeDouble());
                }
                // else is the same day event
                else {
                    individualTimeInfo.add(item.startTimeDouble());
                    individualTimeInfo.add(item.startTimeDouble() + item.getLength() * 100);
                }
                timeInfo.add(individualTimeInfo);
            }
            // compare if any of the start time, end time overlaps within the day
            for (int j = 0; j < (timeInfo.size() - 1); j++){
                for (List<Double> timePair : timeInfo.subList(j + 1, timeInfo.size())){
                    IsOverlapped check = new IsOverlapped(timeInfo.get(j), timePair);
                    // if overlaps store the information needed
                    if (check.getResult()){
                        checkCollection.add(true);
                        if (!(this.conflictEvent.contains(this.calendarMap.get(i).get(j)))){
                            this.conflictEvent.add(this.calendarMap.get(i).get(j));
                        }
                        if (!(this.conflictEvent.contains(this.calendarMap.get(i).get(j + 1)))){
                            this.conflictEvent.add(this.calendarMap.get(i).get(j + 1));
                        }
                    }
                    else {
                        checkCollection.add(false);
                    }
                }
            }
        }
        // if there is any kind of conflict return true
        if (checkCollection.contains(true)){
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

