package usecases;

import entities.Event;
import org.junit.Before;
import org.junit.Test;
import usecases.events.EventManager;
import usecases.events.worksessions.WorkSessionScheduler;

import java.sql.Array;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WorkSessionSchedulerTest {
    private List<Event> events = new ArrayList<>();
    private EventManager eventManager;
    private WorkSessionScheduler workSessionScheduler;
    @Before
    public void setUp(){
        Event[] events = new Event[] {new Event(1, "1", 2021, 10, 1, 2, 3, 0,
                0), new Event(2, "2", 2021, 10, 1, 4, 5, 0,
                0), new Event(3, "3", 2021, 10, 1, 5, 7, 0,
                0), new Event(4, "4", 2021, 10, 2, 9, 10, 30,
                0), new Event(5, "5", 2021, 10, 2, 9, 11, 30,
                30), new Event(6, "6", LocalDateTime.of(2021, 11, 10, 2,
                30), LocalDateTime.of(2021, 11, 13, 4, 30))};
        this.events = new ArrayList<>(Arrays.asList(events));
        this.eventManager = new EventManager((ArrayList<Event>) this.events);
        this.workSessionScheduler = new WorkSessionScheduler(Map.ofEntries(Map.entry(LocalTime.of(4, 0), LocalTime.of(14, 0))), false);
    }
    @Test
    public void removeIneligibleDays(){

    }
}
