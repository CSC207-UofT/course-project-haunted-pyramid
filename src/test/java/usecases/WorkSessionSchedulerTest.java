package usecases;

import entities.Event;
import org.junit.Before;
import org.junit.Test;

import usecases.events.EventManager;
import usecases.events.worksessions.WorkSessionScheduler;

import java.sql.Array;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class WorkSessionSchedulerTest {
    private List<Event> events = new ArrayList<>();
    private EventManager eventManager;
    private WorkSessionScheduler workSessionScheduler;
    private final UUID UUID1 = UUID.randomUUID();
    private final UUID UUID2 = UUID.randomUUID();
    private final UUID UUID3 = UUID.randomUUID();
    private final UUID UUID4 = UUID.randomUUID();
    private final UUID UUID5 = UUID.randomUUID();
    private final UUID UUID6 = UUID.randomUUID();
    @Before
    public void setUp(){
        Event[] events = new Event[] {new Event(UUID1, "1", 2021, 10, 1, 2, 3, 0,
                0), new Event(UUID2, "2", 2021, 10, 1, 4, 5, 0,
                0), new Event(UUID3, "3", 2021, 10, 1, 5, 7, 0,
                0), new Event(UUID4, "4", 2021, 10, 2, 9, 10, 30,
                0), new Event(UUID5, "5", 2021, 10, 2, 9, 11, 30,
                30), new Event(UUID6, "6", LocalDateTime.of(2021, 11, 10, 2,
                30), LocalDateTime.of(2021, 11, 13, 4, 30))};
        this.events = new ArrayList<>(Arrays.asList(events));
        this.eventManager = new EventManager(this.events);
    }
    @Test
    public void removeIneligibleDays(){

    }
}
