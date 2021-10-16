import org.junit.Before;
import org.junit.Test;
import usecases.CalendarManager;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

}
