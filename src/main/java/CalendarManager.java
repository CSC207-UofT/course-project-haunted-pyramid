import java.util.*;


public class CalendarManager {

    private OurCalendar currentCalendar; // calendar object for the current month
    private List<OurCalendar> futureCalendar; // List of calendar object for the past three months
    private List<OurCalendar> pastCalendar; //  List of calendar object for the next three months

    /**
     *  Initialize the CalendarManager
     */
    public CalendarManager(){
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);


        this.currentCalendar = new OurCalendar(year, month + 1);

        cal.add(Calendar.MONTH, 1);
        int firstNextMonth = cal.get(Calendar.MONTH);
        int firstNextYear = cal.get(Calendar.YEAR);

        cal.add(Calendar.MONTH, 1);
        int secondNextMonth = cal.get(Calendar.MONTH);
        int secondNextYear = cal.get(Calendar.YEAR);

        cal.add(Calendar.MONTH, 1);
        int thirdNextMonth = cal.get(Calendar.MONTH);
        int thirdNextYear = cal.get(Calendar.YEAR);


        OurCalendar firstFuture = new OurCalendar(firstNextYear, firstNextMonth + 1);
        OurCalendar secondFuture = new OurCalendar(secondNextYear, secondNextMonth + 1);
        OurCalendar thirdFuture = new OurCalendar(thirdNextYear, thirdNextMonth + 1);

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

        OurCalendar firstPast = new OurCalendar(firstPreviousYear, firstPreviousMonth + 1);
        OurCalendar secondPast = new OurCalendar(secondPreviousYear, secondPreviousMonth + 1);
        OurCalendar thirdPast = new OurCalendar(thirdPreviousYear, thirdPreviousMonth + 1);

        this.pastCalendar = new ArrayList<>(){
            {
                add(firstPast);
                add(secondPast);
                add(thirdPast);
            }
        };

    }

    /**
     * Return Monthly Calendar
     * When there is no argument, return the current month calendar
     * @return Map of the current month calendar
     */
    public Map<Integer, List<Object>> getMonthlyCalendar(){
        return this.currentCalendar.getCalendarMap();
    }

    /**
     * Return Monthly Calendar for the given month by index
     * @param index 0 represents current,
     *              -1, -2, -3 represents the first, second, third elements of the pastCalendar, respectively
     *              1, 2, 3 represents the first, second, third elements of the futureCalendar, respectively
     *
     *              === Representation Invariant ===
     *              -3 <= index <= 3
     * @return Map of the chosen month calendar
     */
    public Map<Integer, List<Object>> getMonthlyCalendar(int index){
        if (index == 0){
            return this.currentCalendar.getCalendarMap();
        }
        else if (index > 0 && index < 4){
            return this.futureCalendar.get(index - 1).getCalendarMap();
        }
        else {
            return this.pastCalendar.get((-1 * index) - 1).getCalendarMap();
        }
    }

//    public Map<Integer, List<Object>> getWeeklyCalendar(){
//        Date t = new Date();
//        Calendar d = Calendar.getInstance();
//        int day = d.get(Calendar.DATE);
//        int start = day - 1;
//        int end = start + 7;
//
//        return this.currentCalendar.getCalendarMap();
//    }

    /**
     * Observe the future calendars and the current calendar to see if there is any conflict
     * Also check what months contain conflict AND
     * check what are the events that are involved in the conflict
     * @return a list of (boolean, list of Events, list of months) to show
     * if there is a conflict, what events are involved, and what months are involved respectively
     * if first element of the list is false, second and third elements are empty
     */
    public List<Object> notifyConflict() {
        boolean conflictCheck = false;
        ArrayList<Object> conflictEvent = new ArrayList<>();
        ArrayList<Integer> monthCollection = new ArrayList<>();
        for (OurCalendar ourCalendar : this.futureCalendar) {
            if (ourCalendar.isConflict()) {
                conflictCheck = true;
                monthCollection.add(ourCalendar.getDateInfo().get(1));
                for (Object item : ourCalendar.getConflictObject()) {
                    if (!conflictEvent.contains(item)) {
                        conflictEvent.add(item);
                    }
                }
            }
        }
        if (this.currentCalendar.isConflict()) {
            conflictCheck = true;
            monthCollection.add(this.currentCalendar.getDateInfo().get(1));
            for (Object item : this.currentCalendar.getConflictObject()) {
                if (!conflictEvent.contains(item)) {
                    conflictEvent.add(item);
                }
            }
        }
        boolean finalConflictCheck = conflictCheck;
        return new ArrayList<>() {
            {
                add(finalConflictCheck);
                add(conflictEvent);
                add(monthCollection);
            }
        };
    }

    public static void main(String[] args) {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);

        int year = cal.get(Calendar.YEAR);
        System.out.println(year);
        int checkMonth = cal.get(Calendar.MONTH) + 1;
        System.out.println(checkMonth);

        CalendarManager cm = new CalendarManager();
        System.out.println("current month info " + cm.currentCalendar.getDateInfo());
        System.out.println("one month after info " + cm.futureCalendar.get(0).getDateInfo());
        System.out.println("two month after info " + cm.futureCalendar.get(1).getDateInfo());
        System.out.println("three month after info " + cm.futureCalendar.get(2).getDateInfo());
        System.out.println("one month before info " + cm.pastCalendar.get(0).getDateInfo());
        System.out.println("two month before info " + cm.pastCalendar.get(1).getDateInfo());
        System.out.println("three month before info " + cm.pastCalendar.get(2).getDateInfo());
        System.out.println("this shows the current month calendar map " + cm.getMonthlyCalendar());
        System.out.println("this shows the calendar map of one month after " + cm.getMonthlyCalendar(1));
        System.out.println("this shows the calendar map of three month after " + cm.getMonthlyCalendar(3));
        System.out.println("this shows the calendar map of two month before " + cm.getMonthlyCalendar(-2));
    }
}
