package usecases.events.worksessions.strategies.TimeGetters;

import entities.Event;
import usecases.events.EventManager;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * TimeGetter interface - step one of WorkSessionScheduler Strategy Pattern
 *
 * @author Taite Cullen
 */
public interface TimeGetter {
    /**
     * @param deadline     the event that all times must fall before, and that preferred session length and total hours are
     *                     based on
     * @param eventManager EventManager UUID deadline stored in
     * @param length       the preferred length of the session to be scheduled
     * @return a map with key: start time to value: total length of available period
     */
    Map<LocalDateTime, Long> getTimes(UUID deadline, EventManager eventManager, Long length);

    /**
     * @param eventManager EventManager UUID deadline stored in
     * @param start        the date to get the start of the schedule from
     * @param deadline     the event that all times must fall before, and that preferred session length and total hours
     *                     are based on
     * @return a list of all the events that count as interfering with potential work sessions
     */
    List<Event> getListSchedule(EventManager eventManager, LocalDate start, UUID deadline);

    /**
     * computes the times between events and the Long length of them in seconds
     *
     * @param start the start time from which free slot are calculated - first start time of free slot
     * @param end   the end time from which free slots are calculated - last end time of free slot
     * @return Map with key: LocalDateTime start time of free slot, value: Long duration of free slot in seconds
     */
    default Map<LocalDateTime, Long> freeSlots(LocalDateTime start, LocalDateTime end, EventManager eventManager,
                                               UUID deadline) {
        // Schedule in a form of a list
        List<Event> schedule = this.getListSchedule(eventManager, start.toLocalDate(), deadline);
        // Sorts the list
        eventManager.timeOrder(schedule);

        Map<LocalDateTime, Long> freeSlots = new HashMap<>();

        for (Event event : schedule) {

            boolean hasStart = event.hasStart();
            boolean isAfterStart = event.getEndTime().isAfter(start);

            if (hasStart && isAfterStart) {

                // start time of event is before End AND start time is after Start (event is in between start and end)
                boolean startTimeBeforeEnd = event.getStartTime().isBefore(end);
                boolean startTimeAfterStart = event.getStartTime().isAfter(start);

                if (startTimeBeforeEnd && startTimeAfterStart) {

                    // Adds the key value pair to the Hash map
                    freeSlots.put(start, Duration.between(start, event.getStartTime()).toHours());

                    start = event.getEndTime();

                    if (start.isAfter(end)) {
                        return freeSlots;
                    }

                } else if (!startTimeBeforeEnd) {
                    freeSlots.put(start, Duration.between(start, end).toHours());
                    return freeSlots;
                }
                start = event.getEndTime();
                if (start.isAfter(end)) {
                    return freeSlots;
                }

            }
        }
        return freeSlots;
    }

    /**
     * @param eventManager EventManager
     * @param deadline     deadline Event
     * @return a map with key:day, value: List of all Events that occur on day that count as interfering with potential
     * session
     */
    default Map<LocalDate, List<Event>> getDaySchedule(EventManager eventManager, UUID deadline) {
        Map<LocalDate, List<Event>> daySchedule = new HashMap<>();
        for (LocalDate start = LocalDate.now(); !start.isAfter(eventManager.getDefaultEventInfoGetter().getEndDate(deadline)); start = start.plusDays(1)) {
            daySchedule.put(start, new ArrayList<>());
            for (Event event : this.getListSchedule(eventManager, start, deadline)) {
                for (Event eventSplit : eventManager.splitByDay(event)) {
                    if (eventManager.getDefaultEventInfoGetter().getEnd(eventSplit).toLocalDate().isEqual(start)) {
                        daySchedule.get(start).add(eventSplit);
                    }
                }
            }
        }
        return daySchedule;
    }

    /**
     * @param deadline     UUID of deadline event
     * @param eventManager EventManager containing deadline event
     * @param length       Long proposed length of the session
     * @return list of potential start times based on get
     */
    default List<LocalDateTime> getStartTimes(UUID deadline, EventManager eventManager, Long length) {
        Map<LocalDateTime, Long> timesMap = this.getTimes(deadline, eventManager, length);
        return new ArrayList<>(timesMap.keySet());
    }

}
