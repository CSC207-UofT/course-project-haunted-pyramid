package usecases;

import org.junit.Before;
import org.junit.Test;
import usecases.calendar.*;

import java.time.YearMonth;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class CalendarByTypeTest {
    CalendarManager calendarManager;
    int year;
    int month;
    int newYear;
    int newMonth;
    int DATE_ONE = 1;
    int DATE_SIX = 6;
    int DATE_TWENTY = 20;
    int numOfDays;
    final UUID UUID1 = UUID.randomUUID();
    final UUID UUID2 = UUID.randomUUID();
    final UUID UUID3 = UUID.randomUUID();
    final UUID UUID4 = UUID.randomUUID();

    @Before
    public void setUp() {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        YearMonth yearMonth = YearMonth.of(year, month);
        numOfDays = yearMonth.lengthOfMonth();
        if (month == 12) {
            newYear = year + 1;
            newMonth = 1;
        }
        else {
            newYear = year;
            newMonth = month + 1;
        }

        calendarManager = new CalendarManager();
        calendarManager.addToCalendar(UUID1, year, month, DATE_ONE);
        calendarManager.addToCalendar(UUID2, year, month, DATE_ONE);
        calendarManager.addToCalendar(UUID3, newYear, newMonth, DATE_SIX);
        calendarManager.addToCalendar(UUID4, year, month, DATE_TWENTY);
    }

    @Test(timeout = 100)
    public void testMonthlyCalendarByType() {
        CalendarByType monthlyCalendar = new MonthlyCalendarByType();
        assertEquals(monthlyCalendar.getCalendar(calendarManager).size(), numOfDays);
        List<UUID> uuidList = new ArrayList<>();
        uuidList.add(UUID1);
        uuidList.add(UUID2);
        assertEquals(monthlyCalendar.getCalendar(calendarManager).get(1), uuidList);
        assertEquals(monthlyCalendar.getCalendar(calendarManager).get(20), new ArrayList<>(){{
            add(UUID4);
        }});
        assertEquals(monthlyCalendar.getCalendar(calendarManager).get(6), new ArrayList<>());
    }

    @Test(timeout = 100)
    public void testWeeklyCalendarByType() {
        WeeklyCalendarByType weeklyCalendarByType = new WeeklyCalendarByType();
        assertEquals(weeklyCalendarByType.getCalendar(calendarManager).size(), 7);
        assertEquals(weeklyCalendarByType.getCalendar(calendarManager, newYear, newMonth, DATE_ONE).get(6),
                new ArrayList<>(){{
            add(UUID3);
        }});
    }

    @Test(timeout = 100)
    public void testDailyCalendarByType() {
        DailyCalendarByType dailyCalendarByType = new DailyCalendarByType();
        assertEquals(dailyCalendarByType.getCalendar(calendarManager).size(), 1);
        assertEquals(dailyCalendarByType.getCalendar(calendarManager, year, month, DATE_ONE).get(1).size(), 2);
        assertEquals(dailyCalendarByType.getCalendar(calendarManager, year, month, DATE_TWENTY).get(20).size(), 1);
    }
}

