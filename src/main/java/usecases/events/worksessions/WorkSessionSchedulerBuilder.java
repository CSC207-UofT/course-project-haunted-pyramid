package usecases.events.worksessions;

import entities.UserPreferences;
import usecases.events.worksessions.strategies.DayOrderer.*;
import usecases.events.worksessions.strategies.TimeOrderer.*;

public class WorkSessionSchedulerBuilder {

    public WorkSessionScheduler getWorkSessionScheduler (UserPreferences userPreferences){
        WorkSessionScheduler workSessionScheduler = new WorkSessionScheduler(userPreferences.getFreeTime());
        this.procrastinate(userPreferences, workSessionScheduler);
        this.cram(userPreferences, workSessionScheduler);
        this.morningPerson(userPreferences, workSessionScheduler);
        this.spacingSameDay(userPreferences, workSessionScheduler);

        return workSessionScheduler;
    }

    private void procrastinate(UserPreferences userPreferences, WorkSessionScheduler workSessionScheduler){
        if (userPreferences.getProcrastinate()){
            workSessionScheduler.addDayOrderer(new Procrastinate());
        } else{
             workSessionScheduler.addDayOrderer(new notProcrastinate());
        }
    }

    private void morningPerson(UserPreferences userPreferences, WorkSessionScheduler workSessionScheduler){
        if (userPreferences.getMorningPerson()){
            workSessionScheduler.addTimeOrderer(new MorningPerson());
        } else{
            workSessionScheduler.addTimeOrderer(new EveningPerson());
        }
    }

    private void spacingSameDay(UserPreferences userPreferences, WorkSessionScheduler workSessionScheduler){
        if (!(userPreferences.getSpacingSameDay().equals("none"))){
            String spacing = userPreferences.getSpacingSameDay();
            workSessionScheduler.addTimeOrderer(new BreaksBetween(spacing));
        }
    }

    private void cram(UserPreferences userPreferences, WorkSessionScheduler workSessionScheduler){
        if (!userPreferences.getCram()) {
            workSessionScheduler.addDayOrderer(new FewestSessions());
        }
    }
}
