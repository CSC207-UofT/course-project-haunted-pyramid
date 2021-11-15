package entities.recursions;

import helpers.ConstantID;
import entities.Event;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


/**
 * @author Malik Lahlou
 */


public class RecursiveEventTest {
    LocalDateTime l =  LocalDateTime.of(2021, 11, 15, 11,0);
    LocalDateTime l2 =  LocalDateTime.of(2021, 12, 17, 11,0);
    Event e1 = new Event(1, "e1", l);
    Event e2 = new Event(2, "e2", 2021, 11, 18, 10, 11, 0, 0);
    Event e3 = new Event(3, "e3", 2021, 11, 20, 10, 11, 0, 0);
    ArrayList<Event> z = new ArrayList<>();
    RecursiveEvent recursiveEvent = new RecursiveEvent(ConstantID.get());

    @Before
    public void setUp() {
        z.add(e1);
        z.add(e2);
        z.add(e3);
        IntervalDateInput x = new IntervalDateInput(l, l2);
        recursiveEvent.setEventsInOneCycle(z);
        recursiveEvent.setMethodToGetDate(x);
    }

    @Test
    public void createEventInCyclesTest() {
        ArrayList<Event> y = recursiveEvent.createEventInCycles(e1);
        assertEquals(y.get(0).getEndTime(), LocalDateTime.of(2021, 11, 20, 11,0));
        assertEquals(y.get(1).getEndTime(), LocalDateTime.of(2021, 11, 25, 11,0));
        assertEquals(y.get(2).getEndTime(), LocalDateTime.of(2021, 11, 30, 11,0));
        assertEquals(y.get(3).getEndTime(), LocalDateTime.of(2021, 12, 5, 11,0));
        assertEquals(y.get(4).getEndTime(), LocalDateTime.of(2021, 12, 10, 11,0));
        assertEquals(y.get(5).getEndTime(), LocalDateTime.of(2021, 12, 15, 11,0));
        ArrayList<Event> k = recursiveEvent.createEventInCycles(e2);
        assertEquals(k.get(0).getEndTime(), LocalDateTime.of(2021, 11, 23, 11,0));
        assertEquals(k.get(1).getEndTime(), LocalDateTime.of(2021, 11, 28, 11,0));
    }
}