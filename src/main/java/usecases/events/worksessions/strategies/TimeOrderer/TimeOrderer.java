package usecases.events.worksessions.strategies.TimeOrderer;

import usecases.events.EventManager;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * step three in WorkSessionScheduler Autoschedule method - reorders a list of eligible times and returns them in order of
 * desirability according to 'order' algorithm and (if applicable) date order method from desirable dates
 *
 * @author Taite Cullen
 * @see usecases.events.worksessions.strategies.DayOrderer.DayOrderer
 */
public interface TimeOrderer {
    /**
     * sorts and/or modifies a list of times to be in increasing priority:
     *
     * @param deadline     the event that all times must fall before
     * @param eventManager the eventManager containing the deadline
     * @param length       the duration that will be placed at the ideal start time
     * @param idealDates   an ordered list of ideal dates
     * @param times        a list of eligible times that will be modified
     * @param timeGetter   the timeGetter used to get the eligible times
     * @see TimeOrderer
     */
    void order(UUID deadline, EventManager eventManager, Long length, List<LocalDate> idealDates, List<LocalDateTime> times,
               TimeGetter timeGetter);

    /**
     * default method for all TimeOrderer implementations - sort times to be in date order by a list of ideal dates
     *
     * @param idealDates ideal dates in decreasing order of desirability
     * @param times      times to be sorted
     */
    default void dateOrder(List<LocalDate> idealDates, List<LocalDateTime> times) {
        List<LocalDateTime> ordered = new ArrayList<>();
        for (LocalDate date : idealDates) {
            for (LocalDateTime time : times) {
                if (time.toLocalDate().isEqual(date)) {
                    ordered.add(time);
                }
            }
        }
        times.clear();
        times.addAll(ordered);
    }
}
