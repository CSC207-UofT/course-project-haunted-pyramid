package usecases.events.worksessions.strategies.TimeGetters;

import entities.Event;
import usecases.events.EventManager;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DefaultTimeGetter implements TimeGetter {
    private final Map<LocalTime, LocalTime> freeTime;

    public DefaultTimeGetter(Map<LocalTime, LocalTime> freeTime){
        this.freeTime = freeTime;
    }

    @Override
    public Map<LocalDateTime, Long> getTimes(UUID deadline, EventManager eventManager, Long length) {
        Map<LocalDateTime, Long> times = this.freeSlots(LocalDateTime.of(eventManager.getStartWorking(deadline),
                        LocalTime.of(0, 0)), eventManager.getEnd(deadline),
                eventManager, deadline);
        Map<LocalDateTime, Long> eligible = new HashMap<>();
        for (LocalDateTime time : times.keySet()) {
            if (times.get(time) >= length) {
                eligible.put(time, times.get(time));
            }
        }
        return eligible;
    }

    @Override
    public List<Event> getListSchedule(EventManager eventManager, LocalDate start, UUID deadline) {
        List<Event> schedule = eventManager.getAllEventsFlatSplit();
        for(LocalDate current = start; !current.isAfter(eventManager.getEndDate(deadline)); current = current.plusDays(1)){
            for (LocalTime startTime: this.freeTime.keySet()){
                schedule.add(new Event(UUID.randomUUID(), "free time", LocalDateTime.of(current, startTime),
                        LocalDateTime.of(current, this.freeTime.get(startTime))));
            }
        }
        return eventManager.timeOrder(schedule);
    }
}
