package usecases.events.worksessions.strategies.TimeGetters;

import entities.Event;
import usecases.events.EventManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Taite Cullen
 * @see TimeGetter
 */
public class DefaultTimeGetter implements TimeGetter {
    private final Map<LocalTime, LocalTime> freeTime;

    /**
     * @param freeTime map of key: start time to value: end time of free time slots
     */
    public DefaultTimeGetter(Map<LocalTime, LocalTime> freeTime) {
        this.freeTime = freeTime;
    }

    /**
     * returns a list of times when the available time between each time and an event that starts after the time is greater
     * than length, and the time is before now and after deadline startWorking
     *
     * @param deadline     the event that all times must fall before, and that preferred session length and total hours are
     *                     based on
     * @param eventManager EventManager UUID deadline stored in
     * @param length       the preferred length of the session to be scheduled
     * @return list of eligible times
     */
    @Override
    public Map<LocalDateTime, Long> getTimes(UUID deadline, EventManager eventManager, Long length) {
        Map<LocalDateTime, Long> times = this.freeSlots(LocalDateTime.of(eventManager.getStartWorking(deadline),
                        LocalTime.of(0, 0)), eventManager.getDefaultEventInfoGetter().getEnd(deadline),
                eventManager, deadline);
        Map<LocalDateTime, Long> eligible = new HashMap<>();
        for (LocalDateTime time : times.keySet()) {
            if (times.get(time) >= length && time.isAfter(LocalDateTime.now())) {
                eligible.put(time, times.get(time));
            }
        }
        return eligible;
    }

    /**
     * adds free time every day and a block of time around the current hour to a schedule as events and returns the schedule
     * in time order
     *
     * @param eventManager EventManager UUID deadline stored in
     * @param start        the date to get the start of the schedule from
     * @param deadline     the event that all times must fall before, and that preferred session length and total hours
     *                     are based on
     * @return timeordered list of interfereing events
     */
    @Override
    public List<Event> getListSchedule(EventManager eventManager, LocalDate start, UUID deadline) {
        List<Event> schedule = eventManager.getAllEventsFlatSplit();
        schedule.add(new Event(UUID.randomUUID(), "now", LocalDateTime.of(LocalDate.now(), LocalTime.of(
                LocalTime.now().getHour(), 0)), LocalDateTime.of(LocalDate.now(), LocalTime.of(LocalTime.now().
                plusHours(1).getHour(), 0))));
        for (LocalDate current = start; !current.isAfter(eventManager.getDefaultEventInfoGetter().getEndDate(deadline)); current = current.plusDays(1)) {
            for (LocalTime startTime : this.freeTime.keySet()) {
                schedule.add(new Event(UUID.randomUUID(), "free time", LocalDateTime.of(current, startTime),
                        LocalDateTime.of(current, this.freeTime.get(startTime))));
            }
        }
        return eventManager.eventHelper.timeOrder(schedule);
    }
}
