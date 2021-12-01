package usecases.events.worksessions.strategies.DayOrderer;

import entities.Event;
import usecases.events.EventManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Procrastinate implements DayOrderer{

    @Override
    public void order(UUID deadline, EventManager eventManager, List<LocalDate> eligibleDates, Map<LocalDate, List<Event>> schedule) {
        List<LocalDate> ordered = new ArrayList<>();
        while(!eligibleDates.isEmpty()){
            ordered.add(latest(eligibleDates));
        }
        eligibleDates.addAll(ordered);
    }

    public static LocalDate latest(List<LocalDate> dates){
        LocalDate latest = dates.get(0);
        for (LocalDate date: dates){
            if (date.isAfter(latest)){
                latest = date;
            }
        }
        dates.remove(latest);
        return latest;
    }

    public static void main(String[] args){
        ArrayList<LocalDate> test = new ArrayList<>(){
            {
                add(LocalDate.now());
                add(LocalDate.now().plusDays(1));
                add(LocalDate.now().plusDays(2));
            }};
    }
}
