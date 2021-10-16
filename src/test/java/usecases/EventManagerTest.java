package usecases;
import entities.Event;

import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EventManagerTest {
    private EventManager eventManager;
    private List<Event> events;

    @Before
    public void eventinator(){
        this.events = Arrays.asList(new Event(1, "October 1 2-3", 2021, 10, 1, 2, 3, 0,
                0), new Event(1, "October 1 4-5", 2021, 10, 1, 4, 5, 0,
                0), new Event(1, "October 1 5-6:30", 2021, 10, 1, 5, 6, 0,
                30), new Event(1, "October 2 9:30-10", 2021, 10, 2, 9, 10, 30,
                0), new Event(1, "October 2 9:30-11:30", 2021, 10, 2, 9, 11, 30,
                30));
        this.eventManager = new EventManager(events);
    }

//    public void testGetDay() {
//        Map<String, Event> day = this.eventManager.getDay(LocalDate.of(2021, 10, 1));
//        for (Event event: this.events){
//            assert !event.getDay().isEqual(LocalDate.of(2021, 10, 1)) || day.containsKey(event.getName());
//        }
//    }

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
    public void testGetStart() {
    }

    @Test
    public void testGetEnd() {
    }

    @Test
    public void testGetAllNames() {
    }

    @Test(timeout = 50)
    public void testTotalHours() {
        assertSame("failure - hours are not equal", (float) 6.0, this.eventManager.totalHours(this.events));
    }

    @Test
    public void testEarliest() {
    }

    @Test
    public void testTimeOrder() {
    }

    @Test
    public void testFreeSlots() {
    }

    public static void main(String[] args){
        List<Event> events = Arrays.asList(new Event(1, "October 1 2-3", 2021, 10, 1, 2, 3, 0,
                0), new Event(1, "October 1 4-5", 2021, 10, 1, 4, 5, 0,
                0), new Event(1, "October 1 5-6:30", 2021, 10, 1, 5, 6, 0,
                30), new Event(1, "October 2 9-10", 2021, 10, 2, 9, 10, 30,
                0), new Event(1, "October 2 9:30-11:30", 2021, 10, 2, 9, 11, 30,
                30));
        EventManager eventManager = new EventManager(events);
        System.out.println(eventManager.getDay(LocalDate.of(2021, 10, 1)).toString());
//        System.out.println(eventManager.freeSlots(LocalDateTime.of(2021, 10, 1, 0, 0),
//                LocalDateTime.of(2021, 10, 2, 0, 0)));
        System.out.println(eventManager.totalHours(events));
    }

}