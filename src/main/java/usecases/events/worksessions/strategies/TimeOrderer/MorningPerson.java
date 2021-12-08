package usecases.events.worksessions.strategies.TimeOrderer;

import usecases.events.EventManager;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * a low priority time orderer that orders a list of times by what is earliest in the day
 *
 * @author Taite Cullen
 */
public class MorningPerson implements TimeOrderer {

    /**
     * sorts a list of times by (in increasing priority):
     * 1. earliest in the day
     * 2. most ideal days from input list of ideal dates
     *
     * @param deadline     the event that all times must fall before
     * @param eventManager the eventManager containing the deadline
     * @param length       the duration that will be placed at the ideal start time
     * @param idealDates   an ordered list of ideal dates
     * @param times        a list of eligible times that will be returned ordered
     * @param timeGetter   the timeGetter used to get the eligible times
     * @see TimeOrderer order
     */
    @Override
    public void order(UUID deadline, EventManager eventManager, Long length, List<LocalDate> idealDates, List<LocalDateTime> times, TimeGetter timeGetter) {
        List<LocalDateTime> ordered = new ArrayList<>();
        while (!times.isEmpty()) {
            ordered.add(earliest(times));
        }
        times.addAll(ordered);
        this.dateOrder(idealDates, times);
    }

    /**
     * a helper method to determine the earliest time in a list of times
     *
     * @param times the LocalDateTimes from which the earliest will be determined
     * @return LocalDateTime the earliest time from times
     */
    private LocalDateTime earliest(List<LocalDateTime> times) {
        LocalDateTime earliest = times.get(0);
        for (LocalDateTime time : times) {
            if (time.isBefore(earliest)) {
                earliest = time;
            }
        }
        times.remove(earliest);
        return earliest;
    }
}
