package presenters;

import entities.Event;
import helpers.DisplayCalendarHelper;
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
    private final List<String> defaultTimeLine = new ArrayList<>();
    private final EventManager eventManager = new EventManager();
    private final DisplayCalendarHelper cf;
    private int NAME_LIMIT = 14;

    public DisplayWeeklyCalendar(CalendarManager cm, int year, int month, int date) {
        super(cm);
        this.year = year;
        this.month = month;
        this.date = date;
        WeeklyCalendar wc = new WeeklyCalendar();
        this.calendarMap = wc.getCalendar(cm, year, month, date);
        for (int i = 0; i < 10; i++){
            this.defaultTimeLine.add("0" + i + ":00");
        }
        for (int j = 10; j < 25; j++){
            this.defaultTimeLine.add(j+ ":00");
        }
        this.cf = new DisplayCalendarHelper(this.year, this.month);

    }

    @Override
    public String displayCalendar(){
        LocalDate localDate = LocalDate.of(year, month, date);
        DayOfWeek dayOfWeek = DayOfWeek.from(localDate);
        cf.eventSorter(calendarMap);
        StringBuilder result = new StringBuilder();
        int startingDayOfWeek = dayOfWeek.getValue();
        setUpCalendar(startingDayOfWeek, result, cf);
        addDate(result, startingDayOfWeek);
        for (int i = 0; i < getLongestTimeLine(); i++){
            for (int j = 0; j < 7; j++){
                List<String> newTimeLine = cf.updateTimeList(this.defaultTimeLine, gatherTimeLine(j));
                if (newTimeLine.size() > i){
                    addTimeLine(result, newTimeLine, i);
                    int tempDiv = addContent(result, newTimeLine, i, j);
                    addSpace(result, tempDiv, startingDayOfWeek + j);
                }
                else {
                    result.append("|");
                    addSpace(result, -8, startingDayOfWeek + j);
                }
            }
            result.append("|").append("\n");
        }
        result.append(cf.endFrame(lengthDecider()));
        return result.toString();
    }

    private int getLongestTimeLine(){
        int length = this.defaultTimeLine.size();
        for (int i = 0; i < this.calendarMap.size(); i++){
            List<String> newTimeList = cf.updateTimeList(this.defaultTimeLine, gatherTimeLine(i));
            if (newTimeList.size() > length){
                length = newTimeList.size();
            }
        }
        return length;
    }
    private List<String> gatherTimeLine(int index){
        List<String> temp = new ArrayList<>();
        List<Integer> keyList = getKeys();
        for (Event event: calendarMap.get(keyList.get(index))){
            temp.add(eventManager.getStartTime(event));
            temp.add(eventManager.getEndTime(event));
        }
        return temp;
    }


    private void setUpCalendar(int startingDayOfWeek, StringBuilder result, DisplayCalendarHelper cf){
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

    private void addDate(StringBuilder result, int dayOfWeek){
        result.append("|");
        List<Integer> keyList = getKeys();
        int month = this.month;
        for (int i = 0; i < keyList.size(); i++){
            int spacer = getSpacer(dayOfWeek + i);
            if (i != 0 && keyList.get(i) == 1){
                month = this.month + 1;
            }
            int tempSpacer = spacer/2;
            if (spacer % 2 == 1){
                tempSpacer = spacer/2 + 1;
            }
            String tempDiv = " ".repeat(tempSpacer + lengthDecider()/2 + 9);
            String preDiv = " ".repeat(9 + spacer/2 + lengthDecider()/2);
            result.append(preDiv);
            if (keyList.get(i) < 10 && month < 10){
                result.append(" 0").append(month).append("/").append
                        ("0").append(keyList.get(i)).append(tempDiv).append("|");
            }
            else if (keyList.get(i) < 10 && month >= 10){
            result.append(" ").append(month).append("/").append
                    ("0").append(keyList.get(i)).append(tempDiv).append("|");
            }
            else if (keyList.get(i) >= 10 && month < 10){
                result.append(" 0").append(month).append
                        ("/").append(keyList.get(i)).append(tempDiv).append("|");
            }
            else{
                result.append(" ").append(month).append
                        ("/").append(keyList.get(i)).append(tempDiv).append("|");
            }
        }

        result.append("\n");
    }


    private void addTimeLine(StringBuilder result, List<String> timeLine, int index){
        result.append("|").append(" ");
        result.append(timeLine.get(index)).append(" |");
    }

    private void addSpace(StringBuilder result, int length, int dayOfWeek){
        int spacer = getSpacer(dayOfWeek);
        String tempDiv = " ".repeat(spacer + 16 + lengthDecider()  - length);
        result.append(tempDiv);
    }

    private int getSpacer(int dayOfWeek) {
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
        return spacer;
    }

    private Integer addContent(StringBuilder result, List<String> timeLine, int time, int index){
        List<Integer> keyList = getKeys();
        int temp = 0;
        for (Event event: calendarMap.get(keyList.get(index))){
            if (convertTimeToInt(timeLine.get(time)) >= convertTimeToInt(eventManager.getStartTime(event))
                && convertTimeToInt(timeLine.get(time)) <= convertTimeToInt(eventManager.getEndTime(event))){
                if (eventManager.getName(event).length() < NAME_LIMIT){
                    result.append(" ").append(eventManager.getName(event)).append(";");
                    temp += eventManager.getName(event).length() + 2;
                }
                else {
                    result.append(" ").append(eventManager.getName(event), 0, 10).append("...").append(";");
                    temp += 15;}
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
            List<Event> eventList = calendarMap.get(number);
            for (int i = 0; i < eventList.size(); i++){
                int totalLength = Math.min(eventManager.getName(eventList.get(i)).length(), NAME_LIMIT);
                for (int j = i + 1; j < eventList.size(); j++){
                    if (convertTimeToInt(eventManager.getStartTime(eventList.get(i)))
                            <= convertTimeToInt(eventManager.getStartTime(eventList.get(j)))
                            && convertTimeToInt(eventManager.getEndTime(eventList.get(i)))
                            > convertTimeToInt(eventManager.getStartTime(eventList.get(j)))){
                        totalLength += Math.min(eventManager.getName(eventList.get(j)).length(), NAME_LIMIT);
                    }
                }
                if (temp < totalLength){
                    temp = totalLength;
                }
            }
        }
        if (temp > 14){
            temp = temp - NAME_LIMIT;
        }
        else {
            temp = 0;
        }
        if (temp % 2 == 1){
            temp += 1;
        }
        return temp;
    }
    public static void main(String[] args) {
        CalendarManager cm = new CalendarManager();
        Event event = new Event(1, "TEST1", 2021, 10, 31, 3, 5, 30, 30);
        Event event1 = new Event(2, "TEST2", 2021, 10, 31, 3, 5, 0, 0);
        Event event2 = new Event(3, "TEST3", 2021, 10, 31, 1, 5, 30, 30);
        Event event3 = new Event(4, "REALLY", 2021, 11, 1, 15, 19, 0,0);
        cm.addToCalendar(event);
        cm.addToCalendar(event1);
        cm.addToCalendar(event2);
        cm.addToCalendar(event3);
        DisplayWeeklyCalendar dwc = new DisplayWeeklyCalendar(cm, 2021, 10, 28);
        System.out.println(dwc.displayCalendar());
    }
}
