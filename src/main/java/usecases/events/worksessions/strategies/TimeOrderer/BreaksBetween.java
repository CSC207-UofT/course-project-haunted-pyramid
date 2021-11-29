package usecases.events.worksessions.strategies.TimeOrderer;

import entities.Event;
import usecases.events.EventManager;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BreaksBetween implements TimeOrderer {
    private String spacing;
    public BreaksBetween(String spacing){
        this.spacing = spacing;
    }
    @Override
    public void order(UUID deadline, EventManager eventManager, Long length, List<LocalDate> idealDates,
                      List<LocalDateTime> times, TimeGetter timeGetter) {
        this.mediumSpacing(deadline, eventManager, length, idealDates, times, timeGetter);
    }

    public void mediumSpacing(UUID deadline, EventManager eventManager, Long length, List<LocalDate> idealDates,
                              List<LocalDateTime> times, TimeGetter timeGetter){
        timeGetter.freeSlots(LocalDateTime.now(), eventManager.getEnd(deadline), eventManager, deadline);
    }
    public void largeSpacing(){

    }
    public void smallSpacing(){

    }
}
