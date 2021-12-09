package GUISwing;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Taite Cullen
 */
public class MenuCreationHelper {

    public JComboBox<LocalTime> timeComboBox() {
        List<Integer> hourList = new ArrayList<>();
        List<Integer> minList = new ArrayList<>();
        List<LocalTime> timeList = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            hourList.add(i);
        }
        minList.add(0);
        minList.add(30);
        for (int hour : hourList) {
            for (int minute : minList) {
                LocalTime newTime = LocalTime.of(hour, minute);
                timeList.add(newTime);
            }
        }
        timeList.add(LocalTime.of(23, 59));
        LocalTime[] arr = new LocalTime[timeList.size()];
        return new JComboBox<>(timeList.toArray(arr));
    }

    public Integer[] dateList(YearMonth month, boolean canBeNull){
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

    public JComboBox<YearMonth> monthComboBox(){
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
