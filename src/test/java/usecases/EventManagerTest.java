
package usecases;
import entities.Event;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import usecases.events.EventManager;

import java.sql.Array;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class EventManagerTest {
    private EventManager eventManager;
    private ArrayList<Event> events;

    @Before
    public void start(){
        Event[] events = new Event[] {new Event(1, "1", 2021, 10, 1, 2, 3, 0,
                0), new Event(2, "2", 2021, 10, 1, 4, 5, 0,
                0), new Event(3, "3", 2021, 10, 1, 5, 7, 0,
                0), new Event(4, "4", 2021, 10, 2, 9, 10, 30,
                0), new Event(5, "5", 2021, 10, 2, 9, 11, 30,
                30), new Event(6, "6", LocalDateTime.of(2021, 11, 10, 2,
                30), LocalDateTime.of(2021, 11, 13, 4, 30))};
        this.events = new ArrayList<>(Arrays.asList(events));
        this.eventManager = new EventManager(this.events);
    }

    @Test(timeout = 100)
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
    public void testGetStartTime() {
        assertEquals("02:00", this.eventManager.getStartTimeString(this.eventManager.get(1)));
    }

    @Test (timeout = 100)
    public void testGetEndTime() {
        assertEquals("03:00", this.eventManager.getEndTimeString(this.eventManager.get(1)));
    }

    @Test(timeout = 50)
    public void testTotalHours() {
        assertEquals(80.5, this.eventManager.totalHours(this.events), 0);
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

    @Test
    public void testGetRange(){
        Map<LocalDate, List<Event>> range = this.eventManager.getRange(LocalDate.of(2021, 10, 1),
                LocalDate.of(2021, 10, 2));

        List<Event> expected = this.eventManager.flatSplitEvents(List.of(this.eventManager.getDay(LocalDate.of(2021,
                10, 1)).values().toArray(new Event[0])));
        List<Event> actual = range.get(LocalDate.of(2021, 10, 1));
        assertTrue(expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(expected));

    }

    @Test
    public void testFreeSlots(){
        Map<LocalDateTime, Long> expected = new HashMap<>();
        LocalDateTime start = LocalDateTime.of(2021, 10, 1, 1, 0);
        LocalDateTime end = LocalDateTime.of(2021, 10, 5, 0, 0);

        this.eventManager.get(1).addWorkSession(LocalDateTime.of(2021, 10, 2, 2, 0),
                LocalDateTime.of(2021, 10, 2, 3, 0));
        expected.put(this.eventManager.get(3).getEndTime(), Duration.between(this.eventManager.get(3).getEndTime(),
                LocalDateTime.of(2021, 10, 2, 2, 0)).toHours());
        expected.put(LocalDateTime.of(2021, 10, 2, 3, 0),
                Duration.between(LocalDateTime.of(2021, 10, 2, 3, 0),
                        this.eventManager.get(4).getStartTime()).toHours());
        expected.put(start, Duration.between(start, this.eventManager.get(1).getStartTime()).toHours());
        expected.put(this.eventManager.get(1).getEndTime(),
                Duration.between(this.eventManager.get(1).getEndTime(),
                        this.eventManager.get(2).getStartTime()).toHours());
        expected.put(this.eventManager.get(5).getEndTime(), Duration.between(this.eventManager.get(5).getEndTime(),
                end).toHours());

        Map<LocalDateTime, Long> actual = this.eventManager.freeSlots(start, this.eventManager.getAllEvents(), end);

        assertEquals(expected, actual);
    }

    @Test
    public void testSplitByDay(){
        assertEquals(new ArrayList<> (List.of(new Event[] {new Event(6, "6", LocalDateTime.of(2021, 11, 10, 2,
                30), LocalDateTime.of(2021, 11, 10, 23, 59)), new Event(6,
                "6", LocalDateTime.of(2021, 11, 11, 0,
                0), LocalDateTime.of(2021, 11, 11, 23, 59)), new Event(6,
                "6", LocalDateTime.of(2021, 11, 12, 0,
                0), LocalDateTime.of(2021, 11, 12, 23, 59)), new Event(6,
                "6", LocalDateTime.of(2021, 11, 13, 0,
                0), LocalDateTime.of(2021, 11, 13, 4, 30))})),
                this.eventManager.splitByDay(this.eventManager.get(6)));
        assertEquals(new ArrayList<>(List.of(this.eventManager.get(1))), this.eventManager.splitByDay(this.eventManager.get(1)));
    }

    @Test
    public void testFlattenWorkSessions(){
        List<Event> expected = this.eventManager.getAllEvents();
        for(Event event: this.eventManager.getAllEvents()){
            event.addWorkSession(LocalDateTime.of(2021, 12, 5, 2, 0),
                    LocalDateTime.of(2021, 12, 5, 3, 0));
            expected.addAll(event.getWorkSessions());
        }
        List<Event> actual = this.eventManager.flattenWorkSessions(this.eventManager.getAllEvents());
        assertTrue(expected.size() == this.eventManager.flattenWorkSessions(this.eventManager.getAllEvents()).size()
        && expected.containsAll(actual) && actual.containsAll(expected));
    }

    @Test
    public void testFlatSplitEvents(){
        List<Event> expected = new ArrayList<>();
        for(Event event: this.eventManager.getAllEvents()){
            expected.addAll(this.eventManager.splitByDay(event));
            event.addWorkSession(LocalDateTime.of(2021, 12, 5, 2, 0),
                    LocalDateTime.of(2021, 12, 5, 3, 0));
            expected.addAll(event.getWorkSessions());
        }
        List<Event> actual = this.eventManager.flatSplitEvents(this.eventManager.getAllEvents());
        assertTrue((expected.size() == this.eventManager.flatSplitEvents(this.eventManager.getAllEvents()).size())
            && expected.containsAll(actual) && actual.containsAll(expected));
    }

    @Test
    public void testEarliest(){
        List<Event> unordered = List.of(new Event[] {
                this.eventManager.get(4),
                this.eventManager.get(2),
                this.eventManager.get(1),
                this.eventManager.get(3),
                this.eventManager.get(5),
                });
        assertEquals(unordered.get(2), this.eventManager.earliest(unordered));
        assertTrue(this.eventManager.get(1).getStartTime().isBefore(this.eventManager.get(3).getStartTime()));
    }
}
