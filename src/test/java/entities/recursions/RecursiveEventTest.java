package entities.recursions;

import entities.Event;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;


/**
 * @author Malik Lahlou
 */


public class RecursiveEventTest {
    LocalDateTime l =  LocalDateTime.of(2021, 11, 15, 11,0);
    LocalDateTime l2 =  LocalDateTime.of(2021, 12, 17, 11,0);
    Event e1 = new Event(UUID.randomUUID(), "e1", l);
    Event e2 = new Event(UUID.randomUUID(), "e2", 2021, 11, 18, 10, 11, 0, 0);
    Event e3 = new Event(UUID.randomUUID(), "e3", 2021, 11, 20, 10, 11, 0, 0);
    List<Event> z = new ArrayList<>();
    RecursiveEvent recursiveEvent = new RecursiveEvent(UUID.randomUUID());

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
    public void listOfEventsInCyclesTest() {
        List<Event> y = recursiveEvent.listOfEventsInCycles(z);
        assertEquals(y.get(0).getEndTime(), LocalDateTime.of(2021, 11, 20, 11,0));
        assertEquals(y.get(1).getEndTime(), LocalDateTime.of(2021, 11, 23, 11,0));
        assertEquals(y.get(2).getEndTime(), LocalDateTime.of(2021, 11, 25, 11,0));
        assertEquals(y.get(3).getEndTime(), LocalDateTime.of(2021, 11, 28, 11,0));
        assertEquals(y.get(4).getEndTime(), LocalDateTime.of(2021, 11, 30, 11,0));
        assertEquals(y.get(5).getEndTime(), LocalDateTime.of(2021, 12, 3, 11,0));
    }

    @Test
    public void cycleAfterRemovalTest(){
        List<Event> y = recursiveEvent.listOfEventsInCycles(z);
        List<List<Event>> k = recursiveEvent.cycleAfterRemoval(y.get(1), y);
        assertEquals(k.get(1).get(0).getEndTime(), LocalDateTime.of(2021, 11, 20, 11,0));
        assertEquals(k.get(1).get(1).getEndTime(), LocalDateTime.of(2021, 11, 25, 11,0));
        assertEquals(k.get(0).size(), 11);
    }

    @Test
    public void cycleAfterAdditionChangeTest(){
        List<Event> y = recursiveEvent.listOfEventsInCycles(z);
        y.get(3).setEndTime(LocalDateTime.of(2021, 11, 29, 11,0));
        List<List<Event>> k = recursiveEvent.cycleAfterAdditionChange(y.get(3),"change", y);
        assertEquals(k.get(1).get(0).getEndTime(), LocalDateTime.of(2021, 11, 25, 11,0));
        assertEquals(k.get(1).get(1).getEndTime(), LocalDateTime.of(2021, 11, 29, 11,0));
        assertEquals(k.get(1).get(2).getEndTime(), LocalDateTime.of(2021, 11, 30, 11,0));
        assertEquals(k.get(0).size(), 11);
    }

    @Test
    public void cycleAfterAdditionChangeTest2(){
        List<Event> y = recursiveEvent.listOfEventsInCycles(z);
        Event d = new Event(UUID.randomUUID(), "e5", 2021, 11, 22, 10, 11, 0, 0);
        List<List<Event>> k = recursiveEvent.cycleAfterAdditionChange(d,"add", y);
        assertEquals(k.get(1).get(0).getEndTime(), LocalDateTime.of(2021, 11, 20, 11,0));
        assertEquals(k.get(1).get(1).getEndTime(), LocalDateTime.of(2021, 11, 22, 11,0));
        assertEquals(k.get(1).get(2).getEndTime(), LocalDateTime.of(2021, 11, 23, 11,0));
        assertEquals(k.get(0).size(), 11);
        assertEquals(k.get(1).size(), 4);
    }

}