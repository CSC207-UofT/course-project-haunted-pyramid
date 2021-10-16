package entities;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourseTest {
    Course course;
    Event event;


    @Before
    public void setUp() {
        course = new Course("CSC207");
        event = new Event(1, "1", LocalDateTime.of(2021, 10, 15, 0, 0,
                0), LocalDateTime.of(2021, 10, 15, 3, 0, 0));
    }


    @Test
    public void testAddEvent() {
        assertTrue(course.addEvent(event));
        assertEquals(course.getTask("Others").size(), 1);
        assertEquals(course.getTaskList().get("Others").get(0), event);
    }

    @Test
    public void testRemoveEvent() {
        course.addEvent(event);
        assertTrue(course.removeEvent(event));
        assertEquals(course.getTask("Others").size(), 0);
    }
}
