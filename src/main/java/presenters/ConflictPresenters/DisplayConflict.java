package presenters.ConflictPresenters;

import usecases.ConflictChecker;
import usecases.calendar.CalendarManager;
import usecases.events.EventManager;

public abstract class DisplayConflict {
    public CalendarManager cm;
    public EventManager em;
    public ConflictChecker conflictChecker;

    /**
     * Initialize the ConflictChecker with the given CalendarManager and EventManager objects
     * @param cm CalendarManager object to explore
     * @param em EventManager object to get event information from
     */
    public DisplayConflict(CalendarManager cm, EventManager em){
        this.cm = cm;
        this.em = em;
        this.conflictChecker = new ConflictChecker(this.em, this.cm);
    }

    /**
     * Display conflicted events' names
     * @return display conflicted events' names
     */
    public abstract String displayConflict();
}

