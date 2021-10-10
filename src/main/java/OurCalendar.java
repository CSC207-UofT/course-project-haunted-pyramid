import java.util.*;
import java.time.YearMonth;


public class OurCalendar {

    private List<Object> conflictObject;
    private boolean conflict;
    private Map<Integer, List<Object>> calendarMap;
    private List<Integer> currentDate; //in the form of [year, month, day, # of days in the month]

    public OurCalendar() {
        GregorianCalendar temp = new GregorianCalendar();
        int year = temp.get(Calendar.YEAR);
        int month = temp.get(Calendar.MONTH) + 1;
        int day = temp.get(Calendar.DATE);
        YearMonth tempYearMonth = YearMonth.of(year, month);
        int daysInMonth = tempYearMonth.lengthOfMonth();
        this.currentDate = new ArrayList<>() {
            {
                add(year);
                add(month);
                add(day);
                add(daysInMonth);
            }
        };
        this.conflict = false;
        this.conflictObject = new ArrayList<>();
        Map<Integer, List<Object>> tempMap = new HashMap<>();
        for (int i = 1; i <= daysInMonth; i++) {
            tempMap.put(i, new ArrayList<>());
        }
        this.calendarMap = tempMap;
    }
    public boolean isConflict(){
        return this.conflict;
    }

    public static void main(String[] args) {
        OurCalendar a = new OurCalendar();
        System.out.println(a.conflict);
        System.out.println(a.calendarMap);
        System.out.println(a.conflictObject);
        System.out.println(a.currentDate);
        System.out.println(a.isConflict());
    }
}

