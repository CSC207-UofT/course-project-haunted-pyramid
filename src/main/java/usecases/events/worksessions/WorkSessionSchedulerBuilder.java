package usecases.events.worksessions;

import entities.UserPreferences;
import usecases.events.worksessions.strategies.DayOrderer.*;
import usecases.events.worksessions.strategies.TimeOrderer.*;

/**
 * Class for directing the building of a WorkSessionScheduler based on UserPreferences Object
 *
 * @author Taite Cullen
 */
public class WorkSessionSchedulerBuilder {

    /**
     * Builds a WorkSessionScheduler based on UserPreferences
     *
     * @param userPreferences UserPreferences
     * @return workSessionScheduler with strategies added according to user preferences
     */
    public WorkSessionScheduler getWorkSessionScheduler(UserPreferences userPreferences) {
        WorkSessionScheduler workSessionScheduler = new WorkSessionScheduler(userPreferences.getFreeTime());
        this.procrastinate(userPreferences, workSessionScheduler);
        this.cram(userPreferences, workSessionScheduler);
        this.morningPerson(userPreferences, workSessionScheduler);
        this.spacingSameDay(userPreferences, workSessionScheduler);

        return workSessionScheduler;
    }

    /**
     * determine whether to add Procrastinate or NotProcrastinate DayOrderer
     *
     * @param userPreferences      UserPreferences Object
     * @param workSessionScheduler new workSessionScheduler being built
     */
    private void procrastinate(UserPreferences userPreferences, WorkSessionScheduler workSessionScheduler) {
        if (userPreferences.getProcrastinate()) {
            workSessionScheduler.addDayOrderer(new Procrastinate());
        } else {
            workSessionScheduler.addDayOrderer(new notProcrastinate());
        }
    }

    /**
     * determine whether to add MorningPErson or EveningPerson TimeOrderer
     *
     * @param userPreferences      UserPreferences Object
     * @param workSessionScheduler new workSessionScheduler being built
     */
    private void morningPerson(UserPreferences userPreferences, WorkSessionScheduler workSessionScheduler) {
        if (userPreferences.getMorningPerson()) {
            workSessionScheduler.addTimeOrderer(new MorningPerson());
        } else {
            workSessionScheduler.addTimeOrderer(new EveningPerson());
        }
    }

    /**
     * add a BreaksBetween TimeOrderer with parameter for spacing size given by UserPreferences
     *
     * @param userPreferences      UserPreferences Object
     * @param workSessionScheduler new workSessionScheduler being built
     */
    private void spacingSameDay(UserPreferences userPreferences, WorkSessionScheduler workSessionScheduler) {
        if (!(userPreferences.getSpacingSameDay().equals("none"))) {
            String spacing = userPreferences.getSpacingSameDay();
            workSessionScheduler.addTimeOrderer(new BreaksBetween(spacing));
        }
    }

    /**
     * determine whether to add FewestSessions DayOrderer
     *
     * @param userPreferences      UserPreferences Object
     * @param workSessionScheduler new workSessionScheduler being built
     */
    private void cram(UserPreferences userPreferences, WorkSessionScheduler workSessionScheduler) {
        if (!userPreferences.getCram()) {
            workSessionScheduler.addDayOrderer(new FewestSessions());
        }
    }
}
