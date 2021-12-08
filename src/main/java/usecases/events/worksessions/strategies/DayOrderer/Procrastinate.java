package usecases.events.worksessions.strategies.DayOrderer;

import entities.Event;
import usecases.events.EventManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @see DayOrderer
 * @author Taite Cullen
 */
public class Procrastinate implements DayOrderer{

    /**
     * orders list of days in order of which is latest
     * @param deadline      the UUID of deadline event - refers to end time
     * @param eventManager  EventManager containing deadline
     * @param eligibleDates list of dates with eligible times
     * @param schedule      time ordered list of interfering events
     */
    @Override
    public void order(UUID deadline, EventManager eventManager, List<LocalDate> eligibleDates, Map<LocalDate, List<Event>> schedule) {
        List<LocalDate> ordered = new ArrayList<>();
        while(!eligibleDates.isEmpty()){
            ordered.add(latest(eligibleDates));
        }
        eligibleDates.addAll(ordered);
    }

    /**
     * helper method determines the latest date in a list of dates and removes it from list
     * @param dates list of dates
     * @return the latest date in dates
     */
    private LocalDate latest(List<LocalDate> dates){
        LocalDate latest = dates.get(0);
        for (LocalDate date: dates){
            if (date.isAfter(latest)){
                latest = date;
            }
        }
        dates.remove(latest);
        return latest;
    }
}
