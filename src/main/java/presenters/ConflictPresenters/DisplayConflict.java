package presenters.ConflictPresenters;

import helpers.EventIDConverter;
import usecases.ConflictChecker;
import usecases.calendar.CalendarManager;
import usecases.events.EventManager;

import java.util.List;
import java.util.UUID;

/**
 * abstract class that is used for displaying conflicted events
 * @author Seo Won Yi
 * @see ConflictChecker
 * @see DisplayDailyConflict
 * @see DisplayWeeklyConflict
 * @see DisplayMonthlyConflict
 */

public abstract class DisplayConflict {
    public CalendarManager cm;
    public EventManager em;
    public ConflictChecker conflictChecker;
    public EventIDConverter converter;

    /**
     * Initialize the ConflictChecker with the given CalendarManager and EventManager objects
     * @param cm CalendarManager object to explore
     * @param em EventManager object to get event information from
     */
    public DisplayConflict(CalendarManager cm, EventManager em){
        this.cm = cm;
        this.em = em;
        this.conflictChecker = new ConflictChecker(this.em, this.cm);
        this.converter = new EventIDConverter(em);
    }

    /**
     * Display conflicted events' names
     * @return display conflicted events' names
     */
    public abstract String displayConflict();

    /**
     * Change events in conflictEvent into string
     * @param conflictEvent list of event UUIDs
     * @return string of conflicted event information
     */
    protected StringBuilder getConflictEventString(List<UUID> conflictEvent) {
        StringBuilder notify = new StringBuilder("The following Events are having conflict:");
        for (UUID eventID : conflictEvent) {
            int eventIntID = this.converter.getIntFromUUID(eventID);
            notify.append("\n").append("ID:").append(eventIntID)
                    .append(" ").append(this.em.getName(this.em.get(eventID)));
        }
        return notify;
    }
}

