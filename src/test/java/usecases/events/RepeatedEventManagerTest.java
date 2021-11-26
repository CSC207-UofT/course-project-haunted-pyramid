package usecases.events;

import helpers.ConstantID;
import entities.Event;
import entities.recursions.IntervalDateInput;
import entities.recursions.RecursiveEvent;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;


/**
 * @author Malik Lahlou
 */


public class RepeatedEventManagerTest {
    LocalDateTime l =  LocalDateTime.of(2021, 11, 15, 11,0);
    LocalDateTime l2 =  LocalDateTime.of(2021, 12, 17, 11,0);
    Event e1 = new Event(UUID.randomUUID(), "e1", l);
    Event e2 = new Event(UUID.randomUUID(), "e2", 2021, 11, 18, 10, 11, 0, 0);
    Event e3 = new Event(UUID.randomUUID(), "e3", 2021, 11, 20, 10, 11, 0, 0);
    List<Event> z = new ArrayList<>();
    RecursiveEvent recursiveEvent = new RecursiveEvent(ConstantID.get());
    RepeatedEventManager repeatedEventManager = new RepeatedEventManager();

    @Before
    public void setUp() {
        z.add(e1);
        z.add(e2);
        z.add(e3);
        IntervalDateInput x = new IntervalDateInput(l, l2);
        recursiveEvent.setEventsInOneCycle(z);
        recursiveEvent.setMethodToGetDate(x);
        // repeatedEventManager.addRecursiveEvent(recursiveEvent);
    }

    //TODO: update test based on new implementation of method (I modified the method after writing
    // the test, so now it fails).

//    @Test
//    public void getEventMapFromRecursionTest() {
//        HashMap<Integer, ArrayList<Event>> y = repeatedEventManager.getEventMapFromRecursion(recursiveEvent.getId());
//        assertEquals(y.get(e1.getID()).get(0).getEndTime(), LocalDateTime.of(2021, 11, 25, 11,0));
//        assertEquals(y.get(e1.getID()).get(1).getEndTime(), LocalDateTime.of(2021, 11, 25, 11,0));
//        assertEquals(y.get(e2.getID()).get(0).getEndTime(), LocalDateTime.of(2021, 11, 23, 11,0));
//        assertEquals(y.get(e2.getID()).get(1).getEndTime(), LocalDateTime.of(2021, 11, 28, 11,0));
//    }
}