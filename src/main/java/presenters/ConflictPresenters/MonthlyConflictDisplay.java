package presenters.ConflictPresenters;

import usecases.calendar.CalendarManager;
import usecases.events.EventManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Display conflicted events in the month
 * @author Seo Won Yi
 * @see usecases.ConflictChecker
 * @see ConflictDisplay
 */

public class MonthlyConflictDisplay extends ConflictDisplay {
    private final int year;
    private final int month;
    private final int numOfDays;

    /**
     * Display the conflict of the given month
     * @param cm CalendarManager object to explore
     * @param em EventManager object to get event information from
     * @param year given year
     * @param month given month
     * @param dateNumber number of days within a month
     */
    public MonthlyConflictDisplay(CalendarManager cm, EventManager em, int year, int month, int dateNumber) {
        super(cm, em);
        this.year = year;
        this.month = month;
        this.numOfDays = dateNumber;
    }

    /**
     * Display the conflicted events names within a month
     * @return the conflicted events names
     */
    @Override
    public String displayConflict() {
        List<UUID> conflictEvent = new ArrayList<>();
        for (int i = 1; i <= numOfDays; i++) {
            List<UUID> tempConflictEventList = this.conflictChecker.notifyConflict(year, month, i);
            for (UUID eventID : tempConflictEventList) {
                if (!conflictEvent.contains(eventID)) {
                    conflictEvent.add(eventID);
                }
            }
        }
        if (conflictEvent.size() == 0) {
            return "There is no conflict for the given month";
        }
        else {
            StringBuilder notify = getConflictEventString(conflictEvent);
            return notify.toString();
        }
    }
}
