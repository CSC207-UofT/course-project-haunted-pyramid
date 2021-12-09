package usecases.events;

import entities.Event;
import entities.recursions.IntervalDateInput;
import entities.recursions.RecursiveEvent;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;


/**
 * @author Malik Lahlou
 */


public class RepeatedEventManagerTest {
    LocalDateTime l = LocalDateTime.of(2021, 11, 15, 11, 0);
    LocalDateTime l2 = LocalDateTime.of(2021, 12, 17, 11, 0);
    Event e1 = new Event(UUID.randomUUID(), "e1", l);
    Event e2 = new Event(UUID.randomUUID(), "e2", 2021, 11, 18, 10, 11, 0, 0);
    Event e3 = new Event(UUID.randomUUID(), "e3", 2021, 11, 20, 10, 11, 0, 0);
    List<Event> z = new ArrayList<>();
    RecursiveEvent recursiveEvent = new RecursiveEvent(UUID.randomUUID());
    RepeatedEventManager repeatedEventManager = new RepeatedEventManager();

    @Before
    public void setUp() {
        z.add(e1);
        z.add(e2);
        z.add(e3);
        IntervalDateInput x = new IntervalDateInput(l, l2);
        recursiveEvent.setEventsInOneCycle(z);
        recursiveEvent.setMethodToGetDate(x);
        repeatedEventManager.addRecursion(recursiveEvent);
    }

    @Test
    public void getThisEventFromRecursionTest() {
        List<Event> y = repeatedEventManager.getAllEventsFromRecursiveEvent(recursiveEvent.getId());
        Event event = repeatedEventManager.getThisEventFromRecursion(y.get(2).getID());
        assertNotNull(event);
        List<Event> ef = recursiveEvent.listOfEventsInCycles(z);
        Event event1 = repeatedEventManager.getThisEventFromRecursion(ef.get(2).getID());
        assertNull(event1);
    }

    @Test
    public void eventListToMapTest() {
        List<Event> ef = recursiveEvent.listOfEventsInCycles(z);
        Map<LocalDateTime, List<Event>> map = repeatedEventManager.eventListToMap(ef, 2);
        assertTrue(map.containsKey(LocalDateTime.of(2021, 11, 20, 11, 0)));
        assertTrue(map.containsKey(LocalDateTime.of(2021, 11, 25, 11, 0)));
        assertEquals(map.get(LocalDateTime.of(2021, 11, 25, 11, 0)).size(), 2);
    }
}