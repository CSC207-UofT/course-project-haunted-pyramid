package usecases.EventCollections.Strategies;

import usecases.EventCollections.Strategies.Repetitions.RepeatWeekly;

import java.util.Map;

public class SchedulerFactory {
    private String[] validWeekdays = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    private boolean checkValidWeekdays(String[] days){
        for (String day: days){
            for (String valid: this.validWeekdays){
                if (day.equalsIgnoreCase(valid)){
                    return true;
                }
            }
        }
        return false;
    }


    public Scheduler getScheduler(Map<String, String> specs){
        if (specs.get("type").equalsIgnoreCase("WEEKLY")){
            if(specs.get("days") == null){
                return null;
            }
            if(this.checkValidWeekdays(specs.get("days").split(","))) {
                return (Scheduler) new RepeatWeekly(specs.get("days").split(","));
            }

        }
        return null;
    }


}
