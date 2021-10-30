package presenters;

import entities.Event;
import helpers.CalendarFrame;
import usecases.CalendarManager;
import usecases.EventManager;
import usecases.WeeklyCalendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DisplayWeeklyCalendar extends DisplayCalendar {
    private final int year;
    private final int month;
    private final int date;
    private final Map<Integer, List<Event>> calendarMap;
    private List<String> timeLine = new ArrayList<>();
    private EventManager eventManager = new EventManager();
    public DisplayWeeklyCalendar(CalendarManager cm, int year, int month, int date) {
        super(cm);
        this.year = year;
        this.month = month;
        this.date = date;
        WeeklyCalendar wc = new WeeklyCalendar();
        this.calendarMap = wc.getCalendar(cm, year, month, date);
        for (int i = 0; i < 10; i++){
            this.timeLine.add("0" + i + ":00");
        }
        for (int j = 10; j < 25; j++){
            this.timeLine.add(j+ ":00");
        }

    }

    @Override
    public String displayCalendar(){
        LocalDate localDate = LocalDate.of(year, month, date);
        DayOfWeek dayOfWeek = DayOfWeek.from(localDate);
        CalendarFrame cf = new CalendarFrame(this.year, this.month);
        StringBuilder result = new StringBuilder();
        int startingDayOfWeek = dayOfWeek.getValue();
        setUpCalendar(startingDayOfWeek, result, cf);
        for (int i = 0; i < 25; i++){
            for (int j = 0; j < 7; j++){
                addTimeLine(result, i);
                int tempDiv = addContent(result, i, j);
                addSpace(result, tempDiv, startingDayOfWeek + j);
            }
            result.append("|").append("\n");
        }
        result.append(cf.endFrame(lengthDecider()));
        return result.toString();
    }

    private void setUpCalendar(int startingDayOfWeek, StringBuilder result, CalendarFrame cf){
        if (startingDayOfWeek == 1){
            result.append(cf.startFrame("MONDAY", lengthDecider()));
        }
        else if (startingDayOfWeek == 2){
            result.append(cf.startFrame("TUESDAY", lengthDecider()));
        }
        else if (startingDayOfWeek == 3){
            result.append(cf.startFrame("WEDNESDAY", lengthDecider()));
        }
        else if (startingDayOfWeek == 4){
            result.append(cf.startFrame("THURSDAY", lengthDecider()));
        }
        else if (startingDayOfWeek == 5){
            result.append(cf.startFrame("FRIDAY", lengthDecider()));
        }
        else if (startingDayOfWeek == 6){
            result.append(cf.startFrame("SATURDAY", lengthDecider()));
        }
        else if (startingDayOfWeek == 7){
            result.append(cf.startFrame("SUNDAY", lengthDecider()));
        }
    }

    private void addTimeLine(StringBuilder result, int index){
        result.append("|").append(" ");
        result.append(timeLine.get(index)).append(" |");
    }

    private void addSpace(StringBuilder result, int length, int dayOfWeek){
        int spacer = 0;
        if (dayOfWeek > 7){
            dayOfWeek -= 7;
        }
        switch (dayOfWeek) {
            case 1:
            case 7:
            case 5:
                spacer = 6;
                break;
            case 2:
                spacer = 7;
                break;
            case 3:
                spacer = 9;
                break;
            case 4:
            case 6:
                spacer = 8;
                break;
        }
        String tempDiv = " ".repeat(spacer + 16 + lengthDecider()  - length);
        result.append(tempDiv);
    }

    private Integer addContent(StringBuilder result, int time, int index){
        List<Integer> keyList = getKeys();
        int temp = 0;
        for (Event event: calendarMap.get(keyList.get(index))){
            if (convertTimeToInt(timeLine.get(time)) >= convertTimeToInt(eventManager.getStartTime(event))
                && convertTimeToInt(timeLine.get(time)) <= convertTimeToInt(eventManager.getEndTime(event))){
                if (eventManager.getName(event).length() < 10){
                    result.append(" ").append(eventManager.getName(event)).append(";");
                    temp += eventManager.getName(event).length() + 2;
                }
                else {
                    result.append(" ").append(eventManager.getName(event), 0, 7).append("...").append(";");
                    temp += 12;}
            }
        }
        return temp;
    }

    private List<Integer> getKeys(){
        List<Integer> lst = new ArrayList<>(calendarMap.keySet());
        Collections.sort(lst);
        List<Integer> tempOne = new ArrayList<>();
        List<Integer> tempTwo = new ArrayList<>();
        for (Integer items: lst){
            if (0 < items && items < 10){
                tempOne.add(items);
            }
            else if (20 < items){
                tempTwo.add(items);
            }
        }
        if (tempOne.size() != 0 && tempTwo.size() != 0){
            lst = new ArrayList<>();
            lst.addAll(tempTwo);
            lst.addAll(tempOne);
        }
        return lst;
    }

    private Integer convertTimeToInt(String time){
        String temp = time.substring(0, 2) + time.substring(3, 5);
        return Integer.parseInt(temp);
    }

    private int lengthDecider(){
        List<Integer> keyList = getKeys();
        int temp = 0;
        for (Integer number: keyList){
            List<Event> sorted = eventManager.timeOrder(calendarMap.get(number));
            calendarMap.get(number).addAll(sorted);
            for (int i = 0; i < sorted.size(); i++){
                int count = 0;
                for (int j = i + 1; j < sorted.size(); j++){
                    if (convertTimeToInt(eventManager.getStartTime(sorted.get(i)))
                            <= convertTimeToInt(eventManager.getStartTime(sorted.get(j)))
                            && convertTimeToInt(eventManager.getStartTime(sorted.get(i))) + 100
                            > convertTimeToInt(eventManager.getStartTime(sorted.get(j)))){
                        count += 1;
                    }
                }
                if (temp < count){
                    temp = count;
                }
            }
        }
        temp = temp * 10;
        if (temp > 15){
            temp = temp - 14;
        }
        else {
            temp = 0;
        }
        return temp;
    }

    public static void main(String[] args) {
        CalendarManager cm = new CalendarManager();
        EventManager em = new EventManager();
        Event event = new Event(1, "TEST", 2021, 10, 30, 3, 5, 0, 0);
        Event event1 = new Event(2, "SEANSEANSEANSEANSEAN", 2021, 10, 30, 3, 5, 0, 0);
        Event event2 = new Event(3, "SEANSEANSEANSEANSEANSEANSEAN", 2021, 10, 30, 3, 5, 0, 0);
        Event event3 = new Event(4, "SEANSEANSEANSEAN SEAN", 2021, 11, 1, 15, 19, 0,0);
        WeeklyCalendar wc = new WeeklyCalendar();
        cm.addToCalendar(event);
        cm.addToCalendar(event1);
        cm.addToCalendar(event2);
        cm.addToCalendar(event3);
        DisplayWeeklyCalendar dwc = new DisplayWeeklyCalendar(cm, 2021, 10, 28);
        System.out.println(dwc.displayCalendar());
    }
}
