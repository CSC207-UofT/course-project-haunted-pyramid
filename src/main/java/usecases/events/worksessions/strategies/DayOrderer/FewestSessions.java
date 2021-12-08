package usecases.events.worksessions.strategies.DayOrderer;

import entities.Event;
import usecases.events.EventManager;
import usecases.events.worksessions.WorkSessionManager;

import java.time.LocalDate;
import java.util.*;

/**
 * @see DayOrderer
 * @author Taite Cullen
 */
public class FewestSessions implements DayOrderer {

    /**
     * orders eligibleDates in order of which days deadline event has the fewest hours of work sessions on
     *
     * @param deadline      the UUID of deadline event - refers to end time
     * @param eventManager  EventManager containing deadline
     * @param eligibleDates list of dates with eligible times
     * @param schedule      time ordered list of interfering events
     */
    @Override
    public void order(UUID deadline, EventManager eventManager, List<LocalDate> eligibleDates,
                      Map<LocalDate, List<Event>> schedule) {
        List<LocalDate> ordered = new ArrayList<>();
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        while (!eligibleDates.isEmpty()) {
            LocalDate leastWorkSessions = this.leastSessions(eligibleDates, workSessionManager.getWorkSessions(deadline), eventManager);
            eligibleDates.remove(leastWorkSessions);
            ordered.add(leastWorkSessions);
        }
        eligibleDates.addAll(ordered);
    }

    /**
     * finds the LocalDate in eligibleDates with the fewest hours of deadline work sessions on it, removes that date
     * from eligibleDates and returns it
     *
     * @param workSessions  specific list of workSessions to determine the days with the least of on
     * @param eventManager  EventManager containing deadline
     * @param eligibleDates list of dates with eligible times
     * @return the date from eligibleDates with the fewest work sessions
     */
    private LocalDate leastSessions(List<LocalDate> eligibleDates, List<Event> workSessions, EventManager eventManager) {
        LocalDate leastWorkSessions = eligibleDates.get(0);
        float record = workSessions.size();
        for (LocalDate day : eligibleDates) {
            List<Event> sessions = new ArrayList<>();
            for (Event session : workSessions) {
                if (eventManager.getDefaultEventInfoGetter().getEnd(session).toLocalDate().isEqual(day)) {
                    sessions.add(session);
                }
            }
            if (eventManager.totalHours(sessions) < record) {
                leastWorkSessions = day;
                record = eventManager.totalHours(sessions);
            }
        }
        return leastWorkSessions;
    }
}
