package usecases;

import entities.Event;

import java.util.List;
import java.util.Map;

public abstract class GetCalendar {
    abstract Map<Integer, List<Event>> getCalendar(CalendarManager cm);
}
