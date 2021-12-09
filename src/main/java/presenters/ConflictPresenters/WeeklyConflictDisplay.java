package presenters.ConflictPresenters;

import usecases.calendar.CalendarManager;
import usecases.events.EventManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Display conflicted events within the week
 * @author Seo Won Yi
 * @see usecases.ConflictChecker
 * @see ConflictDisplay
 */

public class WeeklyConflictDisplay extends ConflictDisplay {
    private final int year;
    private final int month;
    private final int date;

    /**
     * Display the conflict of the given week
     * @param cm CalendarManager object to explore
     * @param em EventManager object to get event information from
     * @param year given year
     * @param month given month
     * @param date given date
     */
    public WeeklyConflictDisplay(CalendarManager cm, EventManager em, int year, int month, int date) {
        super(cm, em);
        this.year = year;
        this.month = month;
        this.date = date;
    }

    /**
     * Display the conflicted events names within a week
     * @return the conflicted events names
     */
    @Override
    public String displayConflict() {
        List<UUID> conflictEvent = new ArrayList<>();
        for (int i = date; i <= date + 6; i++) {
            List<UUID> tempConflictEventList = this.conflictChecker.notifyConflict(year, month, i);
            for (UUID eventID : tempConflictEventList) {
                if (!conflictEvent.contains(eventID)) {
                    conflictEvent.add(eventID);
                }
            }
        }
        if (conflictEvent.size() == 0) {
            return "There is no conflict for the given week";
        }
        else {
            StringBuilder notify = getConflictEventString(conflictEvent);
            return notify.toString();
        }
    }
}
