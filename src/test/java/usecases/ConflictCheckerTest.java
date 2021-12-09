package usecases;

import entities.Event;
import org.junit.Before;
import org.junit.Test;
import usecases.calendar.CalendarManager;
import usecases.events.EventManager;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class ConflictCheckerTest {
    ConflictChecker conflictChecker;
    EventManager eventManager;
    CalendarManager calendarManager;
    int year;
    int month;
    int date;

    private final UUID UUID1 = UUID.randomUUID();
    private final UUID UUID2 = UUID.randomUUID();
    private final UUID UUID3 = UUID.randomUUID();
    private final UUID UUID4 = UUID.randomUUID();

    @Before
    public void setUp() {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        date = cal.get(Calendar.DAY_OF_MONTH);
        calendarManager = new CalendarManager();
        eventManager = new EventManager(new ArrayList<>());
        conflictChecker = new ConflictChecker(eventManager, calendarManager);
    }

    @Test(timeout = 100)
    public void testNotifyConflict() {
        Event eventOne = new Event(UUID1, "TEST ONE", LocalDateTime.of(year, month, date, 14,0),
                LocalDateTime.of(year, month, date, 18, 0));
        Event eventTwo = new Event(UUID2, "TEST TWO", LocalDateTime.of(year, month, date, 16,0),
                LocalDateTime.of(year, month, date, 18,0));
        Event eventThree = new Event(UUID3, "TEST THREE", LocalDateTime.of(year, month, date, 10, 0),
                LocalDateTime.of(year, month, date, 12, 0));
        int newDate;
        if (date == 1) {
            newDate = 2;
        }
        else {
            newDate = 1;
        }
        Event eventFour = new Event(UUID4, "TEST FOUR", LocalDateTime.of(year, month, newDate, 14, 0),
                LocalDateTime.of(year, month, newDate, 20, 0));
        eventManager.addEvent(eventOne);
        eventManager.addEvent(eventTwo);
        eventManager.addEvent(eventThree);
        eventManager.addEvent(eventFour);
        calendarManager.addToCalendar(UUID1, year, month, date);
        calendarManager.addToCalendar(UUID2, year, month, date);
        calendarManager.addToCalendar(UUID3, year, month, date);
        calendarManager.addToCalendar(UUID4, year, month, newDate);
        List<UUID> uuidList = new ArrayList<>();
        uuidList.add(UUID1);
        uuidList.add(UUID2);
        assertEquals(conflictChecker.notifyConflict(year, month, date), uuidList);
        assertEquals(conflictChecker.notifyConflict(year, month, newDate), new ArrayList<>());
    }
}
