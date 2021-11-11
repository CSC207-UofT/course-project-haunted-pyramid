package helpers;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateInfo {
    public List<Integer> getDateInfo(int index) {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        return getYearMonthDays(cal, index);
    }

    private List<Integer> getYearMonthDays(Calendar cal, int interval) {
        List<Integer> yearMonthDays = new ArrayList<>();
        cal.add(Calendar.MONTH, interval);
        yearMonthDays.add(cal.get(Calendar.YEAR));
        yearMonthDays.add(cal.get(Calendar.MONTH) + 1);
        YearMonth tempYearMonth = YearMonth.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
        yearMonthDays.add(tempYearMonth.lengthOfMonth());
        return yearMonthDays;
    }
}
