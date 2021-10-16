import org.junit.Before;
import org.junit.Test;
import usecases.CalendarManager;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.time.YearMonth;
import java.util.*;

import entities.Event;

public class CalendarManagerTest {
    CalendarManager calendarManager;
    int year;
    int month;

    @Before
    public void setUp() {
        calendarManager = new CalendarManager();
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
    }

    @Test(timeout = 50)
    public void testGetCurrentMonthlyCalendar() {
        Map<Integer, List<Event>> currentMonthlyCalendar = calendarManager.getMonthlyCalendar();
        YearMonth tempYearMonth = YearMonth.of(year, month);
        int length = tempYearMonth.lengthOfMonth();
        assertEquals(length, currentMonthlyCalendar.size());
        int count = 0;
        for (int i = 1; i <= length; i++){
            count += currentMonthlyCalendar.get(i).size();
        }
        assertEquals(count ,0);
    }

    @Test(timeout = 50)
    public void testGetMonthlyCalendar() {
        Map<Integer, List<Event>> monthlyCalendar = calendarManager.getMonthlyCalendar(2021, 11);
        YearMonth tempYearMonth = YearMonth.of(year, 11);
        int length = tempYearMonth.lengthOfMonth();
        assertEquals(length, monthlyCalendar.size());
        int count = 0;
        for (int i = 1; i <= length; i++){
            count += monthlyCalendar.get(i).size();
        }
        assertEquals(count ,0);
    }

    @Test(timeout = 50)
    public void testGetWeeklyCalender(){
        Map<Integer, List<Event>> weeklyCalendar = calendarManager.getWeeklyCalendar(year, month, 20);
        YearMonth tempYearMonth = YearMonth.of(year, month);
        int length = 7;
        assertEquals(length, weeklyCalendar.size());
        int count = 0;
        for (int i = 20; i <= 26; i++){
            count += weeklyCalendar.get(i).size();
        }
        assertEquals(count ,0);
        Map<Integer, List<Event>> testMap = new HashMap<>();
        for (int i = 20; i <= 26; i++){
            testMap.put(i, new ArrayList<>());
        }
        assertEquals(testMap, weeklyCalendar);
    }

    @Test(timeout = 50)
    public void testAddAndDailyCalendar(){
        Event eventOne = new Event(1, "Test",
                2021, 10, 15, 7, 10, 0, 0);
        calendarManager.addToCalendar(eventOne);
        Map<Integer, List<Event>> dailyCalendar = calendarManager.getDailyCalendar(year, month, 15);
        assertEquals(dailyCalendar.size(), 1);
        Map<Integer, List<Event>> testMap = new HashMap<>();
        List<Event> testList = new ArrayList<>();
        testList.add(eventOne);
        testMap.put(15, testList);
        assertEquals(dailyCalendar, testMap);
    }

    @Test(timeout = 50)
    public void testAddCalendarManager(){
        Event eventOne = new Event(1, "Test",
                2021, 11, 20, 7, 10, 0, 0);
        calendarManager.addToCalendar(eventOne);
        List<Event> eventList = calendarManager.getMonthlyCalendar(2021, 11).get(20);
        assertEquals(1, eventList.size());
        assertEquals(eventOne, eventList.get(0));
        Event eventTwo = new Event(1, "Test",
                2021, 11, 20, 12, 15, 30, 50);
        calendarManager.addToCalendar(eventTwo);
        assertEquals(2, eventList.size());
        assertEquals(eventTwo, eventList.get(1));
    }

}
