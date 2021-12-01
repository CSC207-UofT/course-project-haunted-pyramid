package usecases.events.worksessions.strategies.TimeOrderer;

import usecases.events.EventManager;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EveningPerson implements TimeOrderer {
    @Override
    public void order(UUID deadline, EventManager eventManager, Long length, List<LocalDate> idealDates, List<LocalDateTime> times, TimeGetter timeGetter) {
        List<LocalDateTime> ordered = new ArrayList<>();
        while(!times.isEmpty()){
            LocalDateTime time = latest(times);
            Map<LocalDateTime, Long> freeSlots = timeGetter.freeSlots(time, eventManager.getEnd(deadline), eventManager, deadline);
            ordered.add(time.plusHours(freeSlots.get(time)).minusHours(length));
        }
        times.addAll(ordered);
        this.dateOrder(idealDates, times);
    }

    private LocalDateTime latest(List<LocalDateTime> times){
        LocalDateTime latest = times.get(0);
        for (LocalDateTime time: times){
            if (time.isAfter(latest)){
                latest = time;
            }
        }
        times.remove(latest);
        return latest;
    }
}
