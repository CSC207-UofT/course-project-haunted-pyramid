package presenters.ConflictPresenters;

import usecases.calendar.CalendarManager;
import usecases.events.EventManager;

import java.util.List;
import java.util.UUID;

public class DisplayDailyConflict extends DisplayConflict {
    private final int year;
    private final int month;
    private final int date;

    /**
     * Display the conflict of the given date
     * @param cm CalendarManager object to explore
     * @param em EventManager object to get event information from
     * @param year given year
     * @param month given month
     * @param date given date
     */
    public DisplayDailyConflict(CalendarManager cm, EventManager em, int year, int month, int date) {
        super(cm, em);
        this.year = year;
        this.month = month;
        this.date = date;
    }

    /**
     * Display the conflicted events names within a day
     * @return the conflicted events names
     */
    @Override
    public String displayConflict() {
        List<UUID> conflictEvent = this.conflictChecker.notifyConflict(year, month, date);
        if (conflictEvent.size() == 0) {
            return "There is no conflict for the given date";
        }
        else {
            StringBuilder notify = getConflictEventString(conflictEvent);
            return notify.toString();
        }
    }
}
