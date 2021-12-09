
package usecases.events;
import entities.Event;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.time.LocalDateTime;
import java.util.*;

public class EventManagerTest {
    private EventManager eventManager;
    private ArrayList<Event> events;

    private final UUID UUID1 = UUID.randomUUID();
    private final UUID UUID2 = UUID.randomUUID();
    private final UUID UUID3 = UUID.randomUUID();
    private final UUID UUID4 = UUID.randomUUID();
    private final UUID UUID5 = UUID.randomUUID();
    private final UUID UUID6 = UUID.randomUUID();

    @Before
    public void start(){
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

    @Test (timeout = 100)
    public void testGetStartTime() {
        assertEquals("02:00", this.eventManager.getStartTimeString(UUID1));
    }

    @Test (timeout = 100)
    public void testGetEndTime() {
        assertEquals("03:00", this.eventManager.getDefaultEventInfoGetter().getEndTimeString(UUID1));
    }

    @Test(timeout = 50)
    public void testTotalHours() {
        assertEquals(80.5, this.eventManager.totalHours(this.events), 0);
    }

    @Test
    public void testSplitByDay(){
        assertEquals(new ArrayList<> (List.of(new Event[] {new Event(UUID6, "6", LocalDateTime.of(2021, 11, 10, 2,
                30), LocalDateTime.of(2021, 11, 10, 23, 59)), new Event(UUID6,
                "6", LocalDateTime.of(2021, 11, 11, 0,
                0), LocalDateTime.of(2021, 11, 11, 23, 59)), new Event(UUID6,
                "6", LocalDateTime.of(2021, 11, 12, 0,
                0), LocalDateTime.of(2021, 11, 12, 23, 59)), new Event(UUID6,
                "6", LocalDateTime.of(2021, 11, 13, 0,
                0), LocalDateTime.of(2021, 11, 13, 4, 30))})),
                this.eventManager.splitByDay(this.eventManager.get(UUID6)));
        assertEquals(new ArrayList<>(List.of(this.eventManager.get(UUID1))), this.eventManager.splitByDay(this.eventManager.get(UUID1)));
    }
}
