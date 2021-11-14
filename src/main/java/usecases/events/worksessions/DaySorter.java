package usecases.events.worksessions;

import entities.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DaySorter {
    List<LocalDate> sorted(Map<LocalDate, List<Event>> days);
}
