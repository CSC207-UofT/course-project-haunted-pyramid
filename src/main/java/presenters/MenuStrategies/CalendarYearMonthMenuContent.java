package presenters.MenuStrategies;

import helpers.Constants;
import helpers.DateInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarYearMonthMenuContent implements MenuContent {

    @Override
    public List<String> getContent(){
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        List<String> menuList = new ArrayList<>();
        List<String> optionList = compileAllYearMonth(cal, today);
        for (int i = 1; i <= 7; i++){
            menuList.add(i + ". " + optionList.get(i - 1));
        }
        return menuList;
    }

    private List<String> compileAllYearMonth(Calendar cal, Date today){
        List<String> optionList = new ArrayList<>();
        for (int i = 3; i >= -3; i--){
            DateInfo dateInfo = new DateInfo();
            List<Integer> tempList = dateInfo.getDateInfo(i);
            optionList.add(tempList.get(0) + " " + stringMonth(tempList.get(1)));
        }
        return optionList;
    }


    private String stringMonth(int month){
        List<String> monthList = new ArrayList<>(){{
            add("JANUARY");
            add("FEBRUARY");
            add("MARCH");
            add("APRIL");
            add("MAY");
            add("JUNE");
            add("JULY");
            add("AUGUST");
            add("SEPTEMBER");
            add("OCTOBER");
            add("NOVEMBER");
            add("DECEMBER");
        }};
        return monthList.get(month - 1);
    }
}
