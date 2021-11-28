package usecases.events.worksessions.strategies.TimeGetters;

import entities.Event;
import usecases.events.EventManager;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        Map<LocalDateTime, Long> eligible = this.freeSlots(LocalDateTime.now(), eventManager.getEnd(deadline),
                eventManager, deadline);

        for (LocalDateTime time : eligible.keySet()) {
            if (eligible.get(time) < length) {
                eligible.remove(time);
            }
        }
        return eligible;
    }

    @Override
    public List<Event> getListSchedule(EventManager eventManager, UUID deadline) {
        List<Event> schedule = eventManager.getAllEventsFlatSplit();
        LocalDate current = LocalDate.now();
        while(!current.isAfter(eventManager.getEndDate(deadline))){
            for (LocalTime start: this.freeTime.keySet()){
                schedule.add(new Event(UUID.randomUUID(), "free time", LocalDateTime.of(current, start),
                        LocalDateTime.of(current, this.freeTime.get(start))));
            }
            current = current.plusDays(1);
        }
        return eventManager.timeOrder(schedule);
    }
}
