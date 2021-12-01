
package usecases.events;
import entities.Event;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class EventManagerTest {
    private EventManager eventManager;
    private ArrayList<Event> events;

    private final UUID UUID1 = UUID.randomUUID();
    private final UUID UUID2 = UUID.randomUUID();
    private final UUID UUID3 = UUID.randomUUID();
    private final UUID UUID4 = UUID.randomUUID();
    private final UUID UUID5 = UUID.randomUUID();
    private final UUID UUID6 = UUID.randomUUID();

    @Before
    public void start(){
        Event[] events = new Event[] {new Event(UUID1, "1", 2021, 10, 1, 2, 3, 0,
                0), new Event(UUID2, "2", 2021, 10, 1, 4, 5, 0,
                0), new Event(UUID3, "3", 2021, 10, 1, 5, 7, 0,
                0), new Event(UUID4, "4", 2021, 10, 2, 9, 10, 30,
                0), new Event(UUID5, "5", 2021, 10, 2, 9, 11, 30,
                30), new Event(UUID6, "6", LocalDateTime.of(2021, 11, 10, 2,
                30), LocalDateTime.of(2021, 11, 13, 4, 30))};
        this.events = new ArrayList<>(Arrays.asList(events));
        this.eventManager = new EventManager(this.events);
    }


    @Test(timeout = 100)
    public void testGetDay() {
        Map<UUID, Event> day = this.eventManager.getDay(LocalDate.of(2021, 10, 1));
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
        assertEquals("02:00", this.eventManager.getStartTimeString(UUID1));
    }

    @Test (timeout = 100)
    public void testGetEndTime() {
        assertEquals("03:00", this.eventManager.getEndTimeString(UUID1));
    }

    @Test(timeout = 50)
    public void testTotalHours() {
        assertEquals(80.5, this.eventManager.totalHours(this.events), 0);
    }

    @Test
    public void testTimeOrder() {
        List<Event> ordered = List.of(new Event[] {this.eventManager.get(UUID1),
                this.eventManager.get(UUID2),
                this.eventManager.get(UUID3), this.eventManager.get(UUID4),
                this.eventManager.get(UUID5)});
        List<Event> unordered = List.of(new Event[] {this.eventManager.get(UUID4),
                this.eventManager.get(UUID2),
                this.eventManager.get(UUID1), this.eventManager.get(UUID5),
                this.eventManager.get(UUID3)});
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

        this.eventManager.get(UUID1).addWorkSession(LocalDateTime.of(2021, 10, 2, 2, 0),
                LocalDateTime.of(2021, 10, 2, 3, 0));
        expected.put(this.eventManager.get(UUID3).getEndTime(), Duration.between(this.eventManager.get(UUID3).getEndTime(),
                LocalDateTime.of(2021, 10, 2, 2, 0)).toHours());
        expected.put(LocalDateTime.of(2021, 10, 2, 3, 0),
                Duration.between(LocalDateTime.of(2021, 10, 2, 3, 0),
                        this.eventManager.get(UUID4).getStartTime()).toHours());
        expected.put(start, Duration.between(start, this.eventManager.get(UUID1).getStartTime()).toHours());
        expected.put(this.eventManager.get(UUID1).getEndTime(),
                Duration.between(this.eventManager.get(UUID1).getEndTime(),
                        this.eventManager.get(UUID2).getStartTime()).toHours());
        expected.put(this.eventManager.get(UUID5).getEndTime(), Duration.between(this.eventManager.get(UUID5).getEndTime(),
                end).toHours());

        Map<LocalDateTime, Long> actual = this.eventManager.freeSlots(start, this.eventManager.getAllEvents(), end);

        assertEquals(expected, actual);
    }
    

    @Test
    public void testSplitByDay(){
        assertEquals(new ArrayList<> (List.of(new Event[] {new Event(UUID6, "6", LocalDateTime.of(2021, 11, 10, 2,
                30), LocalDateTime.of(2021, 11, 10, 23, 59)), new Event(UUID6,
                "6", LocalDateTime.of(2021, 11, 11, 0,
                0), LocalDateTime.of(2021, 11, 11, 23, 59)), new Event(UUID6,
                "6", LocalDateTime.of(2021, 11, 12, 0,
                0), LocalDateTime.of(2021, 11, 12, 23, 59)), new Event(UUID6,
                "6", LocalDateTime.of(2021, 11, 13, 0,
                0), LocalDateTime.of(2021, 11, 13, 4, 30))})),
                this.eventManager.splitByDay(this.eventManager.get(UUID6)));
        assertEquals(new ArrayList<>(List.of(this.eventManager.get(UUID1))), this.eventManager.splitByDay(this.eventManager.get(UUID1)));
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
                this.eventManager.get(UUID4),
                this.eventManager.get(UUID2),
                this.eventManager.get(UUID1),
                this.eventManager.get(UUID3),
                this.eventManager.get(UUID5),
                });
        assertEquals(unordered.get(2), this.eventManager.earliest(unordered));
        assertTrue(this.eventManager.get(UUID1).getStartTime().isBefore(this.eventManager.get(UUID3).getStartTime()));
    }
}
