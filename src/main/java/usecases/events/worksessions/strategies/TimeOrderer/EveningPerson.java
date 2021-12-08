package usecases.events.worksessions.strategies.TimeOrderer;

import usecases.events.EventManager;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Taite Cullen
 * @see TimeOrderer
 */
public class EveningPerson implements TimeOrderer {

    /**
     * reorders list in order ideal date order, and secondly latest times at night. changes all times to be one
     * length away from the end of their corresponding time slots
     *
     * @param deadline     the event that all times must fall before
     * @param eventManager the eventManager containing the deadline
     * @param length       the duration that will be placed at the ideal start time
     * @param idealDates   an ordered list of ideal dates
     * @param times        a list of eligible times that will be modified
     * @param timeGetter   the timeGetter used to get the eligible times
     */
    @Override
    public void order(UUID deadline, EventManager eventManager, Long length, List<LocalDate> idealDates, List<LocalDateTime> times, TimeGetter timeGetter) {
        List<LocalDateTime> ordered = new ArrayList<>();
        while (!times.isEmpty()) {
            LocalDateTime time = latest(times);
            Map<LocalDateTime, Long> freeSlots = timeGetter.freeSlots(time, eventManager.getDefaultEventInfoGetter().getEnd(deadline), eventManager, deadline);
            ordered.add(time.plusHours(freeSlots.get(time)).minusHours(length));
        }
        times.addAll(ordered);
        ordered.clear();
        while (!times.isEmpty()) {
            ordered.add(latest(times));
        }
        times.addAll(ordered);
        this.dateOrder(idealDates, times);
    }

    /**
     * determines the latest in a list of times, removes it from input list times
     *
     * @param times list of LocalDateTimes
     * @return latest time in times
     */
    private LocalDateTime latest(List<LocalDateTime> times) {
        LocalDateTime latest = times.get(0);
        for (LocalDateTime time : times) {
            if (time.toLocalTime().isAfter(latest.toLocalTime())) {
                latest = time;
            }
        }
        times.remove(latest);
        return latest;
    }
}
