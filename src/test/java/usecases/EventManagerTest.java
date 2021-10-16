package usecases;
import entities.Event;

import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EventManagerTest {
    private EventManager eventManager;
    private List<Event> events;

    @Before
    public void eventinator(){
        this.events = Arrays.asList(new Event(1, "1", 2021, 10, 1, 2, 3, 0,
                0), new Event(1, "2", 2021, 10, 1, 4, 5, 0,
                0), new Event(1, "3", 2021, 10, 1, 5, 6, 0,
                30), new Event(1, "4", 2021, 10, 2, 9, 10, 30,
                0), new Event(1, "5", 2021, 10, 2, 9, 11, 30,
                30));
        this.eventManager = new EventManager(events);
    }

    @Test(timeout = 50)
    public void testGetDay() {
        Map<String, Event> day = this.eventManager.getDay(LocalDate.of(2021, 10, 1));
        for (Event event: this.events){
            if (event.getDay().isEqual(LocalDate.of(2021, 10, 1))){
                assertTrue(day.containsKey(event.getName()));
            }
            else{
                assertFalse(day.containsKey(event.getName()));
            }
        }
    }
    @Test (timeout = 100)
    public void testGetStart() {
        assertEquals("2021-10-01 02:00", this.eventManager.getStart(this.eventManager.getEvent("1")));
    }

    @Test (timeout = 100)
    public void testGetEnd() {
        assertEquals("2021-10-1 02:00", this.eventManager.getStart(this.eventManager.getEvent("1")));
    }

    @Test(timeout = 50)
    public void testTotalHours() {
        assertEquals((float) 6.0, this.eventManager.totalHours(this.events), 0);
    }
    @Test
    public void testTimeOrder() {
        ArrayList<Event> ordered = new ArrayList<Event>(Arrays.asList(this.eventManager.getEvent("1"),
                this.eventManager.getEvent("2"),
                this.eventManager.getEvent("3"), this.eventManager.getEvent("4"),
                this.eventManager.getEvent("5")));
        ArrayList<Event> unordered = new ArrayList<Event>(Arrays.asList(this.eventManager.getEvent("4"),
                this.eventManager.getEvent("2"),
                this.eventManager.getEvent("1"), this.eventManager.getEvent("5"),
                this.eventManager.getEvent("3")));
        assertEquals(ordered, this.eventManager.timeOrder(unordered));
    }

    @Test
    public void testGetEvent() {
    }

    @Test
    public void testRemoveEvent() {

    }

    @Test
    public void testAddEvent() {
    }

    @Test
    public void testTestGetName() {
    }



    @Test
    public void testEarliest() {
    }


    //TODO finish implementing freeSlots
//    public void testFreeSlots() {
//        Event slot1 = new Event(1, "slot1", 2021, 10, 1, 3, 4, 0,
//                0);
//        Event slot2 = new Event(1, "slot2", 2021, 10, 1, 1, 2, 0,
//                0);
//        ArrayList<Event> expected = new ArrayList<Event>(Arrays.asList(slot2, slot1));
//        ArrayList<Event> slots = this.eventManager.freeSlots(LocalDateTime.of(2021, 10, 1, 1, 0, 0,
//                0), LocalDateTime.of(2021, 10, 1, 6, 30, 0, 0));
//        System.out.println(slots.get(2).getName());
//        assertEquals(expected, slots);
//    }

}