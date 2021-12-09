package entities;

import java.util.*;
import java.time.YearMonth;

/**
 * Basic class that deals with calendar behaviour
 * @author Seo Won Yi
 */
public class OurCalendar {

    private final List<Integer> dateInfo; //in the form of [year, month, # of days in the month]
    private boolean conflict;  // if the calendar has any conflicted information
    private List<UUID> conflictEvent; // All the UUID of the events that are conflicting
    private final Map<Integer, List<UUID>> calendarMap; // map of calendar

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
        Map<Integer, List<UUID>> tempMap = new HashMap<>();
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
     * Add the event ID to a calendar for the given date
     * @param eventID the event ID that wants to be added
     */
    public void addEventID(UUID eventID, int date){
        for (int key: this.calendarMap.keySet()) {
            if (key == date) {
                this.calendarMap.get(key).add(eventID);
                return;
            }
        }
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
    public Map<Integer, List<UUID>> getCalendarMap(){
        return this.calendarMap;
    }

    /**
     * Set the conflict to the result
     * @param result boolean to indicate the conflict status
     */
    public void setConflict(boolean result) {
        this.conflict = result;
    }

    /**
     * set the conflict events to the given list
     * @param conflictEvent list of conflicted events
     */
    public void setConflictEvent(List<UUID> conflictEvent) {
        this.conflictEvent = conflictEvent;
    }

}

