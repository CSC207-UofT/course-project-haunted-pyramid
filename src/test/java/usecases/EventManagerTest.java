/*
package usecases;
import entities.Event;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EventManagerTest {
    private EventManager eventManager;
    private ArrayList<Event> events;

    @Before
    public void start(){
        Event[] events = new Event[] {new Event(1, "1", 2021, 10, 1, 2, 3, 0,
                0), new Event(2, "2", 2021, 10, 1, 4, 5, 0,
                0), new Event(3, "3", 2021, 10, 1, 5, 6, 0,
                30), new Event(4, "4", 2021, 10, 2, 9, 10, 30,
                0), new Event(5, "5", 2021, 10, 2, 9, 11, 30,
                30)};
        this.events = new ArrayList<>(Arrays.asList(events));
        this.eventManager = new EventManager(this.events);
    }

    @Test(timeout = 50)
    public void testGetDay() {
        Map<Integer, Event> day = this.eventManager.getDay(LocalDate.of(2021, 10, 1));
        for (Event event: this.events){
            if (event.getDay().isEqual(LocalDate.of(2021, 10, 1))){
                assertTrue(day.containsKey(event.getID()));
            }
            else{
                assertFalse(day.containsKey(event.getID()));
            }
        }
    }
    @Test (timeout = 100)
    public void testGetStart() {
        assertEquals("2021-10-01 02:00", this.eventManager.getStart(this.eventManager.get(1)));
    }

    @Test (timeout = 100)
    public void testGetEnd() {
        assertEquals("2021-10-01 02:00", this.eventManager.getStart(this.eventManager.get(1)));
    }

    @Test(timeout = 50)
    public void testTotalHours() {
        assertEquals((float) 6.0, this.eventManager.totalHours(this.events), 0);
    }
    @Test
    public void testTimeOrder() {
        List<Event> ordered = List.of(new Event[] {this.eventManager.get(1),
                this.eventManager.get(2),
                this.eventManager.get(3), this.eventManager.get(4),
                this.eventManager.get(5)});
        List<Event> unordered = List.of(new Event[] {this.eventManager.get(4),
                this.eventManager.get(2),
                this.eventManager.get(1), this.eventManager.get(5),
                this.eventManager.get(3)});
        assertEquals(ordered, this.eventManager.timeOrder(unordered));
    }
}*/
