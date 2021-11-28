package usecases.events.worksessions.strategies.TimeOrderer;

import usecases.events.EventManager;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface TimeOrderer {
    public void order(UUID deadline, EventManager eventManager, Long length, List<LocalDate> idealDates, List<LocalDateTime> times,
                      TimeGetter timeGetter);

    default void dateOrder(List<LocalDate> idealDates, List<LocalDateTime> times){
        List<LocalDateTime> ordered = new ArrayList<>();
        for (LocalDate date: idealDates){
            for (LocalDateTime time: times){
                if (time.toLocalDate().isEqual(date)){
                    ordered.add(time);
                }
            }
        }
        times.clear();
        times.addAll(ordered);
    }
}
