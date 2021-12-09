package usecases;

import entities.Event;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.*;

import usecases.calendar.CalendarManager;

public class CalendarManagerTest {
    CalendarManager calendarManager;
    int year;
    int month;
    int date;
    Event eventOne;
    Event eventTwo;

    private final UUID UUID1 = UUID.randomUUID();
    private final UUID UUID2 = UUID.randomUUID();
    private final UUID UUID3 = UUID.randomUUID();
    private final UUID UUID4 = UUID.randomUUID();
    private final UUID UUID5 = UUID.randomUUID();
    private final UUID UUID6 = UUID.randomUUID();

    @Before
    public void setUp() {
        calendarManager = new CalendarManager();
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        date = cal.get(Calendar.DATE);
        eventOne = new Event(UUID1, "TEST ONE", LocalDateTime.of(year, month, date, 10, 0));
        int testYear;
        int testMonth;
        if (month == 12) {
            testYear = year + 1;
            testMonth = 1;
        }
        else {
            testYear = year;
            testMonth = month + 1;
        }
        eventTwo = new Event(UUID2, "TEST TWO", LocalDateTime.of(testYear, testMonth, date, 16, 0),
                LocalDateTime.of(testYear, testMonth, date, 20, 30));
    }

    @Test(timeout = 100)
    public void testAddToCalendar() {
        this.calendarManager.addToCalendar(eventOne.getID(), year, month, date);
        this.calendarManager.addToCalendar(eventTwo.getID(), eventTwo.getStartTime().getYear(),
                eventTwo.getStartTime().getDayOfMonth(), date);
        assertEquals(calendarManager.getFutureCalendar().get(0).getCalendarMap().get(date).get(0),
                eventTwo.getID());
        assertEquals(calendarManager.getCurrentCalendar().getCalendarMap().get(date).get(0), eventOne.getID());
    }
}
