package entities;

import org.junit.Before;
import org.junit.Test;

import java.time.YearMonth;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class OurCalendarTest {
    OurCalendar calendar;
    int year;
    int month;
    int date;
    int numOfDays;

    private final UUID UUID1 = UUID.randomUUID();

    @Before
    public void setUp(){
        calendar = new OurCalendar();
        Calendar cal = Calendar.getInstance();
        Date today = new Date();
        cal.setTime(today);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        date = cal.get(Calendar.DATE);
        YearMonth yearMonth = YearMonth.of(year, month);
        List<Integer> dateInfoList = new ArrayList<>();
        numOfDays = yearMonth.lengthOfMonth();
    }

    @Test(timeout = 100)
    public void testDateInfo() {
        List<Integer> dateInfoList = new ArrayList<>();
        dateInfoList.add(year);
        dateInfoList.add(month);
        dateInfoList.add(numOfDays);
        assertEquals(calendar.getDateInfo(), dateInfoList);
    }

    @Test(timeout = 100)
    public void testAddEventID(){
        Event eventOne = new Event(UUID1, "Test1",
                year, month, date, 7, 10, 0, 0);
        calendar.addEventID(UUID1, date);
        Map<Integer, List<UUID>> tempMap = new HashMap<>();
        List<UUID> myList  = new ArrayList<>();
        List<UUID> withEvent = new ArrayList<>(){
            {
                add(eventOne.getID());
            }
        };
        for (int i = 1; i <= numOfDays; i++){
            tempMap.put(i, myList);
        }
        tempMap.put(date, withEvent);
        assertEquals(calendar.getCalendarMap(), tempMap);
    }

    @Test(timeout = 100)
    public void testEmptyCalendarMap(){
        Map<Integer, List<UUID>> tempMap = new HashMap<>();
        List<UUID> myList  = new ArrayList<>();

        for (int i = 1; i <= numOfDays; i++){
            tempMap.put(i, myList);
        }
        assertEquals(calendar.getCalendarMap(), tempMap);
    }
}

