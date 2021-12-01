package usecases.events.worksessions.strategies.TimeGetters;

import entities.Event;
import usecases.events.EventManager;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public interface TimeGetter {
    Map<LocalDateTime, Long> getTimes(UUID deadline, EventManager eventManager, Long length);
    List<Event> getListSchedule(EventManager eventManager, UUID deadline);

    /**
     * computes the times between events and the Long length of them in seconds
     *
     * @param start  the start time from which free slot are calculated - first start time of free slot
     * @param end    the end time from which free slots are calculated - last end time of free slot
     * @return Map with key: LocalDateTime start time of free slot, value: Long duration of free slot in seconds
     */
    default Map<LocalDateTime, Long> freeSlots(LocalDateTime start, LocalDateTime end, EventManager eventManager,
                                               UUID deadline){
        List<Event> schedule = this.getListSchedule(eventManager, deadline);
        eventManager.timeOrder(schedule);
        Map<LocalDateTime, Long> freeSlots = new HashMap<>();
        int num = 0;
        while (num < schedule.size()) {
            if (schedule.get(num).hasStart()) {
                if (schedule.get(num).getEndTime().isAfter(start)) {
                    if (schedule.get(num).getStartTime().isBefore(end) && schedule.get(num).getStartTime().isAfter(start)) {
                        freeSlots.put(start, Duration.between(start, schedule.get(num).getStartTime()).toHours());
                        start = schedule.get(num).getEndTime();
                        freeSlots.putAll(this.freeSlots(start, end, eventManager, deadline));
                    } else if (!schedule.get(num).getStartTime().isBefore(end)){
                        freeSlots.put(start, Duration.between(start, end).toHours());
                        return freeSlots;
                    }
                    start = schedule.get(num).getEndTime();
                }
            }
            num += 1;
        }
        return freeSlots;
    }

    default Map<LocalDate, List<Event>> getDaySchedule(EventManager eventManager, UUID deadline){
        Map<LocalDate, List<Event>> daySchedule = new HashMap<>();
        for(LocalDate start = LocalDate.now(); !start.isAfter(eventManager.getEndDate(deadline)); start = start.plusDays(1)){
            daySchedule.put(start, new ArrayList<>());
            for(Event event: this.getListSchedule(eventManager, deadline)){
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
    default Event sessionAdjacent(LocalDateTime time, Long length, UUID deadline, EventManager eventManager) {
        for (Event session: eventManager.getWorkSessions(deadline)){
            if (eventManager.getEnd(session).isEqual(time) || (eventManager.getStartTime(deadline) != null &&
                    LocalDateTime.of(eventManager.getStartDate(deadline),
                            eventManager.getStartTime(deadline)).isEqual(time.plusHours(length)))){
                return session;
            }
        }
        return null;
    }







//    /**
//     * @param deadlineTime the deadline event - end of last freeSlot
//     * @param schedule     the schedule to get free slots between events
//     * @param day          the day to get the freeSlots from
//     * @return freeSlots over the whole day if it is not today or deadline day, otherwise start or end free
//     * slots at now or deadline time
//     */
//    default Map<LocalDateTime, Long> getFreeSlotsDay(LocalDateTime deadlineTime, Map<LocalDate, List<Event>> schedule, LocalDate day) {
//        EventManager eventManager = new EventManager(new ArrayList<>());
//        Map<LocalDateTime, Long> freeSlots;
//        if (day.isEqual(LocalDate.now())) {
//            freeSlots = this.freeSlots(LocalDateTime.now(), schedule.get(day),
//                    LocalDateTime.of(day, LocalTime.of(23, 59)));
//        } else if (day.isEqual(deadlineTime.toLocalDate())) {
//            LocalDateTime start = LocalDateTime.of(day, LocalTime.of(0, 0));
//            freeSlots = this.freeSlots(start, schedule.get(day), deadlineTime);
//        } else {
//            freeSlots = this.freeSlots(LocalDateTime.of(day, LocalTime.of(0, 0)),
//                    schedule.get(day), LocalDateTime.of(day, LocalTime.of(23, 59)));
//        }
//        return freeSlots;
//    }
}
