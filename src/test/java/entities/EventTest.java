package entities;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EventTest {
    Event event1 = new Event(1, "1", LocalDateTime.of(2021, 10, 15, 0, 0,
            0), LocalDateTime.of(2021, 10, 15, 3, 0, 0));
    Event event2 = new Event(1, "1", LocalDateTime.of(2021, 10, 15, 0, 0,
            0), LocalDateTime.of(2021, 10, 15, 3, 0, 0));
    Event event3 = new Event(1, "3", LocalDateTime.of(2021, 10, 15, 1, 0,
            0), LocalDateTime.of(2021, 10, 15, 4, 0, 0));
    Event event4 = new Event(1, "1", LocalDateTime.of(2021, 10, 15, 7, 0,
            0), LocalDateTime.of(2021, 10, 15, 9, 0, 0));
    @Test
    public void testEquals() {
        assertEquals(this.event1, this.event2);
    }

    @Test
    public void getStartString() {
        assertEquals("2021-10-15T00:00", this.event1.getStartTime().toString());
    }

    @Test
    public void getEndString() {
        assertEquals("2021-10-15T03:00", this.event1.getEndTime().toString());
    }

    @Test
    public void getLength() {
        assertEquals(2.0, this.event4.getLength(), 0);
    }

    @Test
    public void conflicts() {
        assertTrue(this.event1.conflicts(this.event3));
        assertFalse(this.event1.conflicts(this.event4));
    }

    @Test
    public void addWorkSession(){
        LocalDateTime start = LocalDateTime.of(2002, 12, 5, 2, 30);
        LocalDateTime end = LocalDateTime.of(2002, 12, 5, 3, 30);
        this.event1.addWorkSession(start, end);
        assertEquals(this.event1.getWorkSessions(), new ArrayList<>
                (List.of(new Event[]{new Event(event1.getID(), event1.getName(), start, end)})));
    }
    @Test
    public void pastWorkSessions(){
        LocalDateTime start = LocalDateTime.of(2002, 12, 5, 2, 30);
        LocalDateTime end = LocalDateTime.of(2002, 12, 5, 3, 30);
        LocalDateTime start2 = LocalDateTime.now().plusDays(7L);
        LocalDateTime end2 = start2.plusHours(3L);
        this.event1.addWorkSession(start, end);
        this.event1.addWorkSession(start2, end2);
        assertEquals(this.event1.pastWorkSessions(), new ArrayList<>
                (List.of(new Event[]{new Event(event1.getID(), event1.getName() + " session", start, end)})));
    }

    @Test
    public void futureWorkSessions(){
        LocalDateTime start = LocalDateTime.of(2002, 12, 5, 2, 30);
        LocalDateTime end = LocalDateTime.of(2002, 12, 5, 3, 30);
        LocalDateTime start2 = LocalDateTime.now().plusDays(7L);
        LocalDateTime end2 = start2.plusHours(3L);
        this.event1.addWorkSession(start, end);
        this.event1.addWorkSession(start2, end2);
        assertEquals(this.event1.futureWorkSessions(), new ArrayList<>
                (List.of(new Event[]{new Event(event1.getID(), event1.getName(), start2, end2)})));
    }

    @Test
    public void resetWorkSessions(){
        LocalDateTime start = LocalDateTime.of(2002, 12, 5, 2, 30);
        LocalDateTime end = LocalDateTime.of(2002, 12, 5, 3, 30);
        LocalDateTime start2 = LocalDateTime.now().plusDays(7L);
        LocalDateTime end2 = start2.plusHours(3L);
        this.event1.addWorkSession(start, end);
        this.event1.addWorkSession(start2, end2);
        this.event1.setWorkSessions(new ArrayList<>());
        assertEquals(this.event1.getWorkSessions(), new ArrayList<>());
    }
    @Test
    public void completeSession(){
        LocalDateTime start = LocalDateTime.of(2002, 12, 5, 2, 30);
        LocalDateTime end = LocalDateTime.of(2002, 12, 5, 3, 30);
        LocalDateTime start2 = LocalDateTime.now().plusDays(7L);
        LocalDateTime end2 = start2.plusHours(3L);
        this.event1.setHoursNeeded(10L);
        this.event1.addWorkSession(start, end);
        this.event1.addWorkSession(start2, end2);
        this.event1.completeSession(this.event1.getWorkSessions().get(0));
        assertEquals(this.event1.getWorkSessions(), new ArrayList<>
                (List.of(new Event[]{new Event(event1.getID(), event1.getName(), start2, end2)})));
        assertEquals((long) this.event1.getHoursNeeded(), 9L);
    }
}