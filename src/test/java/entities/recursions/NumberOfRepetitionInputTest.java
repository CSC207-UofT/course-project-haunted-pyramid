package entities.recursions;

import entities.Event;
import org.junit.Before;
import org.junit.Test;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Malik Lahlou
 */


public class NumberOfRepetitionInputTest {
    NumberOfRepetitionInput x = new NumberOfRepetitionInput(2);
    LocalDateTime l =  LocalDateTime.of(2021, 11, 15, 11,0);
    Event e1 = new Event(1, "e1", l);
    Event e2 = new Event(2, "e2", 2021, 11, 18, 10, 11, 0, 0);
    Event e3 = new Event(3, "e3", 2021, 11, 20, 10, 11, 0, 0);
    ArrayList<Event> z = new ArrayList<>();

    @Before
    public void setUp() {
        z.add(e1);
        z.add(e2);
    }


    @Test
    public void listOfDatesInCyclesWith2Events() {
        List<Event> y = x.listOfDatesInCycles(z);
        assertEquals(y.get(0).getEndTime(), LocalDateTime.of(2021, 11, 18, 11,0));
        assertEquals(y.get(1).getEndTime(), LocalDateTime.of(2021, 11, 21, 11,0));
    }

    @Test
    public void listOfDatesInCyclesWith3Events() {
        z.add(e3);
        List<Event> y = x.listOfDatesInCycles(z);
        assertEquals(y.get(1).getEndTime(), LocalDateTime.of(2021, 11, 23, 11,0));
        assertEquals(y.get(2).getEndTime(), LocalDateTime.of(2021, 11, 25, 11,0));
    }
}