package usecases.events.worksessions.strategies.DayOrderer;

import entities.Event;
import usecases.events.EventManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Taite Cullen
 * @see DayOrderer
 */
public class notProcrastinate implements DayOrderer {

    /**
     * orders list of dates in order of which is earliest
     *
     * @param deadline      the UUID of deadline event - refers to end time
     * @param eventManager  EventManager containing deadline
     * @param eligibleDates list of dates with eligible times
     * @param schedule      time ordered list of interfering events
     */
    @Override
    public void order(UUID deadline, EventManager eventManager, List<LocalDate> eligibleDates, Map<LocalDate, List<Event>> schedule) {
        List<LocalDate> ordered = new ArrayList<>();
        while (!eligibleDates.isEmpty()) {
            ordered.add(earliest(eligibleDates));
        }
        eligibleDates.addAll(ordered);
    }

    /**
     * A helper method to determine the earliest date - determines earliest date from dates and removes it from dates
     *
     * @param dates list of dates
     * @return earliest date in dates
     */
    private LocalDate earliest(List<LocalDate> dates) {
        LocalDate earliest = dates.get(0);
        for (LocalDate date : dates) {
            if (date.isBefore(earliest)) {
                earliest = date;
            }
        }
        dates.remove(earliest);
        return earliest;
    }
}
