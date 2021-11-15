package usecases.events;

import entities.ConstantID;
import entities.Event;
import entities.recursions.IntervalDateInput;
import entities.recursions.RecursiveEvent;
import org.junit.Before;
import org.junit.Test;
import usecases.events.RepeatedEventManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;


/**
 * @author Malik Lahlou
 */


public class RepeatedEventManagerTest {
    LocalDateTime l =  LocalDateTime.of(2021, 11, 15, 11,0);
    LocalDateTime l2 =  LocalDateTime.of(2021, 12, 17, 11,0);
    Event e1 = new Event(ConstantID.get(), "e1", l);
    Event e2 = new Event(ConstantID.get(), "e2", 2021, 11, 18, 10, 11, 0, 0);
    Event e3 = new Event(ConstantID.get(), "e3", 2021, 11, 20, 10, 11, 0, 0);
    ArrayList<Event> z = new ArrayList<>();
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
        repeatedEventManager.addRecursiveEvent(recursiveEvent);
    }

    @Test
    public void getEventsFromRecursionTest() {
        HashMap<Integer, ArrayList<Event>> y = repeatedEventManager.getEventsFromRecursion(recursiveEvent.getId());
        assertEquals(y.get(e1.getID()).get(0).getEndTime(), LocalDateTime.of(2021, 11, 20, 11,0));
        assertEquals(y.get(e1.getID()).get(1).getEndTime(), LocalDateTime.of(2021, 11, 25, 11,0));
        assertEquals(y.get(e2.getID()).get(0).getEndTime(), LocalDateTime.of(2021, 11, 23, 11,0));
        assertEquals(y.get(e2.getID()).get(1).getEndTime(), LocalDateTime.of(2021, 11, 28, 11,0));
    }
}