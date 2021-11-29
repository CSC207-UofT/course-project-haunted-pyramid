package usecases.events.worksessions.strategies.DayOrderer;

import entities.Event;
import usecases.events.EventManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class notProcrastinate implements DayOrderer{
    @Override
    public void order(UUID deadline, EventManager eventManager, List<LocalDate> eligibleDates, Map<LocalDate, List<Event>> schedule) {
        List<LocalDate> ordered = new ArrayList<>();
        while(!eligibleDates.isEmpty()){
            ordered.add(earliest(eligibleDates));
        }
        eligibleDates.addAll(ordered);
    }

    private LocalDate earliest(List<LocalDate> dates){
        LocalDate earliest = dates.get(0);
        for (LocalDate date: dates){
            if (date.isBefore(earliest)){
                earliest = date;
            }
        }
        dates.remove(earliest);
        return earliest;
    }
}
