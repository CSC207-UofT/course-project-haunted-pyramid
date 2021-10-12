package entities;

import java.util.*;
import java.time.YearMonth;
// Object will be changed to entities.Event

public class OurCalendar {

    private List<Object> conflictObject; // All the objects that are conflicting
    private boolean conflict;  // if the calendar has any conflicted information
    private Map<Integer, List<Object>> calendarMap; // map of calendar
    private List<Integer> dateInfo; //in the form of [year, month, # of days in the month]


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
        this.conflictObject = new ArrayList<>();
        // create an empty calendar map with keys of days and values of list of events (empty to start with) for the
        // provided year and month
        Map<Integer, List<Object>> tempMap = new HashMap<>();
        for (int i = 1; i <= daysInMonth; i++) {
            tempMap.put(i, new ArrayList<>());
        }
        this.calendarMap = tempMap;
    }
    // if no argument, create the current month calendar
    public OurCalendar(){
        GregorianCalendar temp = new GregorianCalendar();
        OurCalendar tempCalendar = new OurCalendar(temp.get(Calendar.YEAR), temp.get(Calendar.MONTH) + 1);
        this.conflictObject = tempCalendar.conflictObject;
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
    public List<Object> getConflictObject(){
        return this.conflictObject;
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
    public Map<Integer, List<Object>> getCalendarMap(){
        return this.calendarMap;
    }

    /**
     * Add the event to a calendar.
     * If the adding results in a conflict, change conflict attribute to true
     * Also add the conflicted objects to the conflictObject
     * @param event the event that wants to be added
     * @param date the date of the event
     */
    public void addEventForDate(Object event, int date){
        // If there is no event for the day, add the event
        if (this.calendarMap.get(date).equals(new ArrayList<>())){
            this.calendarMap.put(date, new ArrayList<>(){
                {
                    add(event);
                }
            });
        }
        // otherwise, add the event for the date and check the conflict
        // if conflicted, apply the changes necessary
        /* uncommented because event.startTime, event.endTime not imported yet
        else {
            // check all the start time and end time of the events
            for (Object item : this.calendarMap.get(date)){
                if ((item.getStartTime() <= event.getStartTime() <= item.getEndTime()) ||
                (item.getStartTime() <= event.getEndTime() <= item.getEndTime())){
                    this.conflict = true;
                    if (!this.conflictObject.contains(item)){
                        this.conflictObject.add(item);
                    }
                    this.conflictObject.add(event);
                }
                this.calendarMap.get(date).add(event);  // add the event to the value list of the given date
            }
        }

         */
    }

    /**
     * Remove an event for the given date
     * If there is no such event, do nothing
     * @param event event that will be removed
     * @param date the date that the event needs to be removed from
     */
    public void removeEventForDate(Object event, int date){
        this.calendarMap.get(date).remove(event);
    }


    public static void main(String[] args) {
        OurCalendar a = new OurCalendar();
        System.out.println(a.getCalendarMap()); // should create a map of calendar for a month
        System.out.println(a.isConflict()); // should return false
        System.out.println(a.getConflictObject()); // should return empty list
        System.out.println(a.getDateInfo()); // should return a list of [year, month, # of dates]

        String b = "hello"; // you can add any object for now (will be changed to entities.Event only once implemented
        a.addEventForDate(b, 20);
        System.out.println(a.calendarMap); // should return a map with "hello" as a value of the key 20
        a.removeEventForDate(b, 20);
        System.out.println(a.calendarMap); // should return a map with "hello" removed from the key 20
    }
}

