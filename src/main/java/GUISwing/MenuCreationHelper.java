package GUISwing;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

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

    public static Integer[] dateList(YearMonth month, boolean canBeNull){
        int days = month.lengthOfMonth();
        List<Integer> dayList = new ArrayList<>();
        if (canBeNull) {
            int index = 1;
            while (index <= days) {
                dayList.add(index);
                index += 1;
            }
            dayList.add(null);
        } else {
            for (int index = 1; index <= days; index += 1){
                dayList.add(index);
            }
        }
        Integer[] arr = new Integer[dayList.size()];

        return dayList.toArray(arr);
    }

    public static JComboBox<YearMonth> monthComboBox(){
        ArrayList<YearMonth> months = new ArrayList<>();
        months.add(YearMonth.of(LocalDate.now().minusMonths(3).getYear(), LocalDate.now().minusMonths(3).getMonth()));
        months.add(YearMonth.of(LocalDate.now().minusMonths(2).getYear(), LocalDate.now().minusMonths(2).getMonth()));
        months.add(YearMonth.of(LocalDate.now().minusMonths(1).getYear(), LocalDate.now().minusMonths(1).getMonth()));
        months.add(YearMonth.of(LocalDate.now().getYear(), LocalDate.now().getMonth()));
        months.add(YearMonth.of(LocalDate.now().plusMonths(1).getYear(), LocalDate.now().plusMonths(1).getMonth()));
        months.add(YearMonth.of(LocalDate.now().plusMonths(2).getYear(), LocalDate.now().plusMonths(2).getMonth()));
        months.add(YearMonth.of(LocalDate.now().plusMonths(3).getYear(), LocalDate.now().plusMonths(3).getMonth()));

        return new JComboBox<>(months.toArray(new YearMonth[7]));
    }
}
