package GUISwing;

import usecases.calendar.CalendarManager;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;

public class MenuCreationHelper {
    public static JComboBox<LocalTime> timeComboBox() {
        LocalTime[] timeList = new LocalTime[25];
        LocalTime first = LocalTime.of(0, 0);
        for (int index = 0; index < 24; index += 1) {
            timeList[index] = first;
            first = first.plusHours(1);
        }
        timeList[24] = LocalTime.of(23, 59);
        return new JComboBox<>(timeList);
    }

    public static JComboBox<Integer> dateJComboBox(YearMonth month){
        int days = month.lengthOfMonth();
        Integer[] daysList = new Integer[days];
        for (int index = 0; index < days; index += 1){
            daysList[index] = index + 1;
        }
        return new JComboBox<>(daysList);
    }

    public static JComboBox<YearMonth> monthComboBox(){
        ArrayList<YearMonth> months = new ArrayList<>();
        months.add(YearMonth.of(LocalDate.now().getYear(), LocalDate.now().getMonth()));
        months.add(YearMonth.of(LocalDate.now().minusMonths(3).getYear(), LocalDate.now().minusMonths(3).getMonth()));
        months.add(YearMonth.of(LocalDate.now().minusMonths(2).getYear(), LocalDate.now().minusMonths(2).getMonth()));
        months.add(YearMonth.of(LocalDate.now().minusMonths(1).getYear(), LocalDate.now().minusMonths(1).getMonth()));
        months.add(YearMonth.of(LocalDate.now().plusMonths(3).getYear(), LocalDate.now().plusMonths(3).getMonth()));
        months.add(YearMonth.of(LocalDate.now().plusMonths(2).getYear(), LocalDate.now().plusMonths(2).getMonth()));
        months.add(YearMonth.of(LocalDate.now().plusMonths(1).getYear(), LocalDate.now().plusMonths(1).getMonth()));
        return new JComboBox<>(months.toArray(new YearMonth[7]));
    }
}
