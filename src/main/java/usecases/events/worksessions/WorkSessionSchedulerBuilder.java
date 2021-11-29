package usecases.events.worksessions;

import com.dropbox.core.v2.team.CustomQuotaError;
import entities.UserPreferences;
import usecases.events.worksessions.strategies.DayOrderer.FewestSessions;
import usecases.events.worksessions.strategies.DayOrderer.Procrastinate;
import usecases.events.worksessions.strategies.DayOrderer.notProcrastinate;
import usecases.events.worksessions.strategies.TimeOrderer.BreaksBetween;
import usecases.events.worksessions.strategies.TimeOrderer.EveningPerson;
import usecases.events.worksessions.strategies.TimeOrderer.MorningPerson;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class WorkSessionSchedulerBuilder {
//
//    private boolean procrastinate;
//
//    private boolean morningPerson;
//
//    private final List<String> validSpacing = List.of("short", "medium", "long");
//    private String spacingSameDay; // short, medium, large, or null
//    private boolean spaceEvenlyDays;
//
//    private final Map<LocalTime, LocalTime> freeTime;

    public WorkSessionScheduler getWorkSessionScheduler (UserPreferences userPreferences){
        WorkSessionScheduler workSessionScheduler = new WorkSessionScheduler(userPreferences.getFreeTime());
        this.procrastinate(userPreferences, workSessionScheduler);
        this.spaceEvenlyDays(userPreferences, workSessionScheduler);
        this.morningPerson(userPreferences, workSessionScheduler);
        this.spacingSameDay(userPreferences, workSessionScheduler);
        return workSessionScheduler;
    }

    private void procrastinate(UserPreferences userPreferences, WorkSessionScheduler workSessionScheduler){
        if (userPreferences.getProcrastinate()){
            workSessionScheduler.addDayOrderer(new Procrastinate());
        } else{
//            workSessionScheduler.addDayOrderer(new notProcrastinate());
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
        if (!(userPreferences.getSpacingSameDay() == null)){
            String spacing = userPreferences.getSpacingSameDay();
            workSessionScheduler.addTimeOrderer(new BreaksBetween(spacing));
        }

    }

    private void spaceEvenlyDays(UserPreferences userPreferences, WorkSessionScheduler workSessionScheduler){
        if (userPreferences.getSpaceEvenlyDays()) {
            workSessionScheduler.addDayOrderer(new FewestSessions());
        }
    }
}
