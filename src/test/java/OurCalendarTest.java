import org.junit.Before;
import org.junit.Test;
import entities.OurCalendar;
import entities.Event;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class OurCalendarTest {
    OurCalendar calendar;
    int year;
    int month;
    int date;

    @Before
    public void setUp(){
        calendar = new OurCalendar();
        Calendar cal = Calendar.getInstance();
        Date today = new Date();
        cal.setTime(today);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        date = cal.get(Calendar.DATE);
    }

    @Test(timeout = 100)
    public void testUpdateConflict(){
        assertFalse(calendar.isConflict());
        Event eventOne = new Event(1, "Test1",
                2021, 10, 15, 7, 10, 0, 0);
        Event eventTwo = new Event(2, "Test2",
                2021, 10, 15, 7, 10, 0, 0);
        calendar.addEvent(eventOne);
        calendar.addEvent(eventTwo);
        calendar.updateConflict();
        assertTrue(calendar.isConflict());

    }

    @Test(timeout = 100)
    public void testAddEvent(){
        Event eventOne = new Event(1, "Test1",
                2021, 10, 15, 7, 10, 0, 0);
        calendar.addEvent(eventOne);
        Map<Integer, List<Event>> tempMap = new HashMap<>();
        List<Event> myList  = new ArrayList<>();
        List<Event> withEvent = new ArrayList<>(){
            {
                add(eventOne);
            }
        };
        for (int i = 1; i <= 31; i++){
            tempMap.put(i, myList);
        }
        tempMap.put(15, withEvent);
        assertEquals(calendar.getCalendarMap(), tempMap);
    }

    @Test(timeout = 100)
    public void testGetCalendarMap(){
        Map<Integer, List<Event>> tempMap = new HashMap<>();
        List<Event> myList  = new ArrayList<>();

        for (int i = 1; i <= 31; i++){
            tempMap.put(i, myList);
        }
        assertEquals(calendar.getCalendarMap(), tempMap);
    }

    @Test(timeout = 100)
    public void testGetConflictEvent(){
        Event eventOne = new Event(1, "Test1",
                2021, 10, 15, 7, 10, 0, 0);
        Event eventTwo = new Event(2, "Test2",
                2021, 10, 15, 7, 10, 0, 0);
        calendar.addEvent(eventOne);
        calendar.addEvent(eventTwo);
        calendar.updateConflict();
        List<Event> myList = new ArrayList<>(){
            {
                add(eventOne);
                add(eventTwo);
            }

        };
        assertEquals(calendar.getConflictEvent(), myList);
    }
}

