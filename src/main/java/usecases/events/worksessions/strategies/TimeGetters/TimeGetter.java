package usecases.events.worksessions.strategies.TimeGetters;

import entities.Event;
import usecases.events.EventManager;
import usecases.events.worksessions.WorkSessionManager;
import usecases.events.worksessions.WorkSessionScheduler;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public interface TimeGetter {
    Map<LocalDateTime, Long> getTimes(UUID deadline, EventManager eventManager, Long length);
    List<Event> getListSchedule(EventManager eventManager, LocalDate start, UUID deadline);

    /**
     * computes the times between events and the Long length of them in seconds
     *
     * @param start  the start time from which free slot are calculated - first start time of free slot
     * @param end    the end time from which free slots are calculated - last end time of free slot
     * @return Map with key: LocalDateTime start time of free slot, value: Long duration of free slot in seconds
     */
    default Map<LocalDateTime, Long> freeSlots(LocalDateTime start, LocalDateTime end, EventManager eventManager,
                                               UUID deadline){
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

                        if (start.isAfter(end)){
                            return freeSlots;
                        }

                    } else if (!startTimeBeforeEnd) {
                        freeSlots.put(start, Duration.between(start, end).toHours());
                        return freeSlots;
                    }
                    start = event.getEndTime();
                    if (start.isAfter(end)){
                        return freeSlots;
                    }

            }
        }
        return freeSlots;
    }

    default Map<LocalDate, List<Event>> getDaySchedule(EventManager eventManager, UUID deadline){
        Map<LocalDate, List<Event>> daySchedule = new HashMap<>();
        for(LocalDate start = LocalDate.now(); !start.isAfter(eventManager.getEndDate(deadline)); start = start.plusDays(1)){
            daySchedule.put(start, new ArrayList<>());
            for(Event event: this.getListSchedule(eventManager, start, deadline)){
                for (Event eventSplit: eventManager.splitByDay(event)){
                    if (eventManager.getEnd(eventSplit).toLocalDate().isEqual(start)){
                        daySchedule.get(start).add(eventSplit);
                    }
                }
            }
        }
        return daySchedule;
    }

    default List<LocalDateTime> getSplitStartTimes(UUID deadline, EventManager eventManager, Long length){
        List<LocalDateTime> split = new ArrayList<>();
        Map<LocalDateTime, Long> times = this.getTimes(deadline, eventManager, length);
        for (LocalDateTime time: times.keySet()){
            split.add(time);
            if (time.plusHours(times.get(time)).toLocalDate().isAfter(time.toLocalDate())){
                split.add(LocalDateTime.of(time.plusHours(times.get(time)).toLocalDate(), LocalTime.of(0, 0,
                        0)));
            }
        }
        return split;
    }

    default List<LocalDateTime> getStartTimes(UUID deadline, EventManager eventManager, Long length){
        Map<LocalDateTime, Long> timesMap = this.getTimes(deadline, eventManager, length);
        return new ArrayList<>(timesMap.keySet());
    }

    //helper method for mergeSessions
    default Event sessionAdjacent(UUID deadline, EventManager eventManager, Event newSession) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        for (Event session: workSessionManager.getWorkSessions(deadline)){
            if (eventManager.getEnd(session).isEqual(eventManager.getStart(newSession)) || eventManager.getStart(
                    session).isEqual(eventManager.getEnd(newSession))){
                return session;
            }
        }
        return null;
    }

}
