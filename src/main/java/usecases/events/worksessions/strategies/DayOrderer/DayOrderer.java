package usecases.events.worksessions.strategies.DayOrderer;

import entities.Event;
import usecases.events.EventManager;
import usecases.events.worksessions.strategies.TimeOrderer.TimeOrderer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface DayOrderer {
    void order(UUID deadline, EventManager eventManager, List<LocalDate> eligibleDates, Map<LocalDate, List<Event>> schedule);
}
