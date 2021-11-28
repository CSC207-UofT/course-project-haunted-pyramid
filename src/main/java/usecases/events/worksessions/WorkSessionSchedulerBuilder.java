package usecases.events.worksessions;

import entities.UserPreferences;

public class WorkSessionSchedulerBuilder {

    public WorkSessionScheduler getWorkSessionScheduler (UserPreferences userPreferences){
        WorkSessionScheduler workSessionScheduler = new WorkSessionScheduler(userPreferences.getFreeTime());
        return workSessionScheduler;
    }
}
