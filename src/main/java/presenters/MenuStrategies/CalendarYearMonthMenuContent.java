package presenters.MenuStrategies;

import helpers.DateInfo;
import interfaces.MenuContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Calendar Date Selection menu content
 * @author Seo Won Yi
 */
public class CalendarYearMonthMenuContent implements MenuContent {

    @Override
    public List<String> getContent(){
        List<String> menuList = new ArrayList<>();
        List<String> optionList = compileAllYearMonth();
        for (int i = 1; i <= 7; i++){
            menuList.add(i + ". " + optionList.get(i - 1));
        }
        return menuList;
    }

    /**
     * Using DateInfo object, access all the past 3-month, current and future 3-month calendar's year/month
     * date information. Convert the month (integer) to appropriate string and add to the list
     * @return list of string that has year/month information
     */
    private List<String> compileAllYearMonth(){
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
