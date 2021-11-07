package helpers;

import entities.Event;
import usecases.EventManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AutoScheduler {

    public void autoschedule(Event deadline, Integer hoursNeeded, Integer sessionLength, EventManager eventManager,
                             boolean procrastinate){

        //one session per day with fewest sessions and most free time in smallest free time section that fits a session
        while(hoursNeeded > 0){
            Map<LocalDate, List<Event>> schedule = eventManager.getRange(LocalDate.now(), deadline.getStartTime().toLocalDate());
            LocalDate mostFreeTime = LocalDate.now();
            LocalDateTime bestSlot = LocalDateTime.now();

            for (LocalDate day: schedule.keySet()){
                if (eventManager.totalHours(schedule.get(day)) < eventManager.totalHours(schedule.get(mostFreeTime))){
                    Map<LocalDateTime, Long> freeSlots = eventManager.freeSlots(LocalDateTime.now(), schedule.get(day),
                            deadline.getStartTime());
                    if (this.maximum(freeSlots.values().toArray(new Long[0])) < sessionLength) {
                        mostFreeTime = day;
                        for (LocalDateTime slot : freeSlots.keySet()) {
                            if (sessionLength < freeSlots.get(slot) && freeSlots.get(slot) < freeSlots.get(bestSlot)){
                                bestSlot = slot;
                            }
                        }
                    }
                }
            }

            eventManager.addEvent(deadline.getName() + "session", bestSlot, bestSlot.plusHours(sessionLength));
        }
    }

    private Long maximum(Long[] durations){
        Long max = 0L;
        for (Long dur: durations){
            if (dur > max){
                max = dur;
            }
        }
        return max;
    }
}
