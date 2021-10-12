import java.util.*;
import java.time.YearMonth;

public class CalendarManager {

    private OurCalendar currentCalendar; // calendar object for the current month
    private List<OurCalendar> futureCalendar; // List of calendar object for the past three months
    private List<OurCalendar> pastCalendar; //  List of calendar object for the next three months

    public CalendarManager(){
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        this.currentCalendar = new OurCalendar(year, month);

        cal.add(Calendar.MONTH, 1);
        int firstNextMonth = cal.get(Calendar.MONTH);
        int firstNextYear = cal.get(Calendar.YEAR);

        cal.add(Calendar.MONTH, 1);
        int secondNextMonth = cal.get(Calendar.MONTH);
        int secondNextYear = cal.get(Calendar.YEAR);

        cal.add(Calendar.MONTH, 1);
        int thirdNextMonth = cal.get(Calendar.MONTH);
        int thirdNextYear = cal.get(Calendar.YEAR);

        OurCalendar firstFuture = new OurCalendar(firstNextYear, firstNextMonth);
        OurCalendar secondFuture = new OurCalendar(secondNextYear, secondNextMonth);
        OurCalendar thirdFuture = new OurCalendar(thirdNextYear, thirdNextMonth);

        this.futureCalendar = new ArrayList<>(){
            {
                add(firstFuture);
                add(secondFuture);
                add(thirdFuture);
            }
        };


        Calendar c = Calendar.getInstance();
        c.setTime(today);

        c.add(Calendar.MONTH, -1);
        int firstPreviousMonth = c.get(Calendar.MONTH);
        int firstPreviousYear = c.get(Calendar.YEAR);

        c.add(Calendar.MONTH, -1);
        int secondPreviousMonth = c.get(Calendar.MONTH);
        int secondPreviousYear = c.get(Calendar.YEAR);

        c.add(Calendar.MONTH, -1);
        int thirdPreviousMonth = c.get(Calendar.MONTH);
        int thirdPreviousYear = c.get(Calendar.YEAR);

        OurCalendar firstPast = new OurCalendar(firstPreviousYear, firstPreviousMonth);
        OurCalendar secondPast = new OurCalendar(secondPreviousYear, secondPreviousMonth);
        OurCalendar thirdPast = new OurCalendar(thirdPreviousYear, thirdPreviousMonth);

        this.pastCalendar = new ArrayList<>(){
            {
                add(firstPast);
                add(secondPast);
                add(thirdPast);
            }
        };

    }

    public Map<Integer, List<Object>> getMonthlyCalendar(){
        return this.currentCalendar.getCalendarMap();
    }

    public Map<Integer, List<Object>> getWeeklyCalendar(){
        Date t = new Date();
        Calendar d = Calendar.getInstance();
        int day = d.get(Calendar.DATE);
        int start = day - 1;
        int end = start + 7;

        return this.currentCalendar.getCalendarMap();
    }
}
