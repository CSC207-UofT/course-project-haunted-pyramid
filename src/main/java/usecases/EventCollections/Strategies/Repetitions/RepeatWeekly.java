package usecases.EventCollections.Strategies.Repetitions;

import usecases.EventCollections.Strategies.Scheduler;

import java.time.LocalDateTime;

public class RepeatWeekly implements Scheduler{
    //TODO make this actually take into account days
    private String[] days;

    public RepeatWeekly(String[] days){
        this.days = days;
    }

    @Override
    public Integer[] getInstanceSchedule(Integer[] initial, Integer[][] except, Integer[][] also, Integer count) {
        LocalDateTime firstDateStart = LocalDateTime.of(initial[0], initial[1], initial[2], initial[3],
                initial[4]);
        LocalDateTime firstDateEnd = LocalDateTime.of(initial[5], initial[6], initial[7], initial[8],
                initial[9]);
        LocalDateTime nextDateStart = firstDateStart.plusDays(count* 7L);
        LocalDateTime nextDateEnd = firstDateEnd.plusDays(count* 7L);
        return new Integer[]{nextDateStart.getYear(), nextDateStart.getMonthValue(), nextDateStart.getDayOfMonth(),
                nextDateStart.getHour(), nextDateStart.getMinute(), nextDateEnd.getYear(), nextDateEnd.getMonthValue(),
                nextDateEnd.getDayOfMonth(), nextDateEnd.getHour(), nextDateEnd.getMinute()};
    }
}
