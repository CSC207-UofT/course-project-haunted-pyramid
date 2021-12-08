package usecases.events.worksessions.strategies.DayOrderer;

import entities.Event;
import usecases.events.EventManager;
import usecases.events.worksessions.strategies.TimeOrderer.TimeOrderer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * An object for ordering a list of days - step 2 in WorkSessionScheduler strategy method
 *
 * @author Taite Cullen
 */
public interface DayOrderer {
    /**
     * modifies eligibleDates to be in order of most desirable to least desirable date
     *
     * @param deadline      the UUID of deadline event - refers to end time
     * @param eventManager  EventManager containing deadline
     * @param eligibleDates list of dates with eligible times
     * @param schedule      time ordered list of interfering events
     */
    void order(UUID deadline, EventManager eventManager, List<LocalDate> eligibleDates, Map<LocalDate, List<Event>> schedule);
}
