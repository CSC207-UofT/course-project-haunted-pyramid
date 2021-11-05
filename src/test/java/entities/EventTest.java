package entities;

import org.junit.Test;

import java.time.LocalDateTime;

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
        assertEquals("2021-10-15 00:00", this.event1.getStartString());
    }

    @Test
    public void getEndString() {
        assertEquals("2021-10-15 03:00", this.event1.getEndString());
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
}