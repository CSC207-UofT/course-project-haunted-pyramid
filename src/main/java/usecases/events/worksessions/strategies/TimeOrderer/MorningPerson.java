package usecases.events.worksessions.strategies.TimeOrderer;

import usecases.events.EventManager;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MorningPerson implements TimeOrderer{
    @Override
    public void order(UUID deadline, EventManager eventManager, Long length, List<LocalDate> idealDates, List<LocalDateTime> times, TimeGetter timeGetter) {
        List<LocalDateTime> ordered = new ArrayList<>();
        while(!times.isEmpty()){
            ordered.add(earliest(times));
        }
        times.addAll(ordered);
        System.out.println(times);
        this.dateOrder(idealDates, times);
    }

    private LocalDateTime earliest(List<LocalDateTime> times){
        LocalDateTime earliest = times.get(0);
        for (LocalDateTime time: times){
            if (time.isBefore(earliest)){
                earliest = time;
            }
        }
        times.remove(earliest);
        return earliest;
    }
}
