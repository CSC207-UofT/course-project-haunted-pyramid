package usecases;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.time.YearMonth;
import java.util.*;

import entities.Event;
import usecases.calendar.CalendarManager;
import usecases.calendar.DailyCalendar;
import usecases.calendar.MonthlyCalendar;
import usecases.calendar.WeeklyCalendar;

public class CalendarManagerTest {
    CalendarManager calendarManager;
    MonthlyCalendar getMonthlyCalendar = new MonthlyCalendar();
    WeeklyCalendar getWeeklyCalendar = new WeeklyCalendar();
    DailyCalendar getDailyCalendar = new DailyCalendar();
    int year;
    int month;
    int date;

    @Before
    public void setUp() {
        calendarManager = new CalendarManager();
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        date = cal.get(Calendar.DATE);
    }

    @Test(timeout = 100)
    public void testGetCurrentMonthlyCalendar() {
        Map<Integer, List<Event>> currentMonthlyCalendar = getMonthlyCalendar.getCalendar(calendarManager);
        YearMonth tempYearMonth = YearMonth.of(year, month);
        int length = tempYearMonth.lengthOfMonth();
        assertEquals(length, currentMonthlyCalendar.size());
        int count = 0;
        for (int i = 1; i <= length; i++){
            count += currentMonthlyCalendar.get(i).size();
        }
        assertEquals(count ,0);
    }

    @Test(timeout = 100)
    public void testGetMonthlyCalendar() {
        Map<Integer, List<Event>> monthlyCalendar = getMonthlyCalendar.getCalendar(calendarManager, 2021, 11);
        YearMonth tempYearMonth = YearMonth.of(year, 11);
        int length = tempYearMonth.lengthOfMonth();
        assertEquals(length, monthlyCalendar.size());
        int count = 0;
        for (int i = 1; i <= length; i++){
            count += monthlyCalendar.get(i).size();
        }
        assertEquals(count ,0);
    }

    @Test(timeout = 100)
    public void testGetWeeklyCalender(){
        Map<Integer, List<Event>> weeklyCalendar = getWeeklyCalendar.getCalendar(calendarManager, year, month, 20);
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
        weeklyCalendar = getWeeklyCalendar.getCalendar(calendarManager, year, month, date);
        assertEquals(weeklyCalendar, getWeeklyCalendar.getCalendar(calendarManager));
    }

    @Test(timeout = 100)
    public void testAddAndDailyCalendar(){
        Event eventOne = new Event(1, "Test1",
                2021, 10, 15, 7, 10, 0, 0);
        calendarManager.addToCalendar(eventOne);
        Map<Integer, List<Event>> dailyCalendar = getDailyCalendar.getCalendar(calendarManager, 2021, 10, 15);
        assertEquals(dailyCalendar.size(), 1);
        Map<Integer, List<Event>> testMap = new HashMap<>();
        List<Event> testList = new ArrayList<>();
        testList.add(eventOne);
        testMap.put(15, testList);
        assertEquals(dailyCalendar, testMap);
    }

    @Test(timeout = 100)
    public void testAddCalendarManager(){
        Event eventOne = new Event(1, "Test1",
                2021, 11, 20, 7, 10, 0, 0);
        calendarManager.addToCalendar(eventOne);
        List<Event> eventList = getMonthlyCalendar.getCalendar(calendarManager, 2021, 11).get(20);
        assertEquals(1, eventList.size());
        assertEquals(eventOne, eventList.get(0));
        Event eventTwo = new Event(2, "Test2",
                2021, 11, 20, 12, 15, 30, 50);
        calendarManager.addToCalendar(eventTwo);
        assertEquals(2, eventList.size());
        assertEquals(eventTwo, eventList.get(1));
    }

    @Test(timeout = 100)
    public void testNotifyConflict(){
        Event eventOne = new Event(1, "Test1",
                year, month, 20, 7, 10, 0, 0);
        Event eventTwo = new Event(2, "Test2",
                year, month, 20, 15, 19, 30, 50);
        Event eventThree = new Event(3, "Test3", year, month,
                20, 8, 13, 0, 0);

        calendarManager.addToCalendar(eventOne);
        calendarManager.addToCalendar(eventTwo);
        calendarManager.addToCalendar(eventThree);
        List<String> testList = new ArrayList<>(){
            {
                add("Test1");
                add("Test3");
            }
        };
        assertEquals(testList, calendarManager.notifyConflict(year, month));
    }

    @Test(timeout = 100)
    public void testYearMonthDate(){
        assertEquals(calendarManager.getCurrentDate(), date);
        assertEquals(calendarManager.getCurrentMonth(), month);
        assertEquals(calendarManager.getCurrentYear(), year);
    }

    @Test(timeout = 100)
    public void testGetEventTimeAndName(){
        Event eventOne = new Event(1, "Test1",
                2021, 12, 20, 7, 10, 0, 0);
        Event eventTwo = new Event(2, "Test2",
                2021, 11, 23, 7, 10, 0, 0);
        Event eventThree = new Event(3, "Test3",
                2021, 10, 18, 18, 20, 0, 0);
        Event eventFour = new Event(4, "Test4",
                2021, 10, 18, 7, 10, 0, 0);
        this.calendarManager.addToCalendar(eventOne);
        this.calendarManager.addToCalendar(eventTwo);
        this.calendarManager.addToCalendar(eventThree);
        this.calendarManager.addToCalendar(eventFour);
        assertEquals(this.calendarManager.getEventNames(2021,10,18).get(0), "Test3");
        assertEquals(this.calendarManager.getEventNames(2021,10,18).get(1), "Test4");
        assertEquals(this.calendarManager.getEventNames(2021,12,20).get(0), "Test1");
        assertEquals(this.calendarManager.getEventNames(2021,11,23).get(0), "Test2");
        String testTimeOne = String.valueOf(this.calendarManager.getEventTimes(2021, 10, 18).get(0));
        String testTimeTwo = String.valueOf(this.calendarManager.getEventTimes(2021, 10, 18).get(1));
        String testTimeThree = String.valueOf(this.calendarManager.getEventTimes(2021, 11, 23).get(0));
        assertEquals(testTimeOne, "18:00 - 20:00");
        assertEquals(testTimeTwo, "07:00 - 10:00");
        assertEquals(testTimeThree, "07:00 - 10:00");
    }
}
