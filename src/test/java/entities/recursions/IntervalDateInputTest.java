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

public class IntervalDateInputTest {
    private final UUID UUID1 = UUID.randomUUID();
    private final UUID UUID2 = UUID.randomUUID();
    private final UUID UUID3 = UUID.randomUUID();

    LocalDateTime l =  LocalDateTime.of(2021, 11, 15, 11,0);
    LocalDateTime l2 =  LocalDateTime.of(2021, 12, 17, 11,0);
    LocalDateTime l3 =  LocalDateTime.of(2021, 12, 31, 11,0);
    Event e1 = new Event(UUID1, "e1", l);
    Event e2 = new Event(UUID2, "e2", 2021, 11, 18, 10, 11, 0, 0);
    Event e3 = new Event(UUID3, "e3", 2021, 11, 20, 10, 11, 0, 0);
    List<Event> z = new ArrayList<>();

    @Before
    public void setUp() {
        z.add(e1);
        z.add(e2);
        z.add(e3);
    }


    @org.junit.Test
    public void listOfDatesInCyclesWith3EventsAndStartDateBeforeEvent1() {
        IntervalDateInput x = new IntervalDateInput(l, l2);
        List<Event> y = x.listOfDatesInCycles(z);
        assertEquals(y.get(0).getEndTime(), LocalDateTime.of(2021, 11, 20, 11,0));
        assertEquals(y.get(1).getEndTime(), LocalDateTime.of(2021, 11, 23, 11,0));
        assertEquals(y.get(y.size()-1).getEndTime(), LocalDateTime.of(2021, 12, 15, 11,0));
        assertEquals(y.get(y.size()-1).getName().split("-")[0], e1.getName());
    }

    @Test
    public void listOfDatesInCyclesWith3EventsAndStartDateAfterEvent1() {
        IntervalDateInput x = new IntervalDateInput(l2, l3);
        List<Event> y = x.listOfDatesInCycles(z);
        assertEquals(y.get(0).getEndTime(), LocalDateTime.of(2021, 12, 18, 11,0));
        assertEquals(y.get(1).getEndTime(), LocalDateTime.of(2021, 12, 20, 11,0));
    }
}