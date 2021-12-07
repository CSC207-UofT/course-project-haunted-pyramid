package usecases.events.worksessions;

import entities.Event;
import interfaces.EventListObserver;
import usecases.events.worksessions.strategies.DayOrderer.FewestSessions;
import usecases.events.worksessions.strategies.TimeGetters.DefaultTimeGetter;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;
import usecases.events.worksessions.strategies.DayOrderer.DayOrderer;
import usecases.events.worksessions.strategies.TimeOrderer.BreaksBetween;
import usecases.events.worksessions.strategies.TimeOrderer.EveningPerson;
import usecases.events.worksessions.strategies.TimeOrderer.MorningPerson;
import usecases.events.worksessions.strategies.TimeOrderer.TimeOrderer;

import usecases.events.EventManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class WorkSessionScheduler {
    private final WorkSessionManager workSessionManager;
    private final TimeGetter timeGetter;
    private final List<DayOrderer> dayOrderers;
    private final List<TimeOrderer> timeOrderers;

    public WorkSessionScheduler(Map<LocalTime, LocalTime> freeTime) {
        this.workSessionManager = new WorkSessionManager();
        this.timeGetter = new DefaultTimeGetter(freeTime);
        this.dayOrderers = new ArrayList<>();
        this.timeOrderers = new ArrayList<>();
    }

    /**
     * Method which marks incomplete a session for the Event
     *
     * @param event        An Event
     * @param session      A String with details of session
     * @param eventManager An EventManager
     */
    public void markInComplete(UUID event, String session, EventManager eventManager) {
        List<Event> workSessions = workSessionManager.getWorkSessions(eventManager, event);
        workSessions = eventManager.timeOrder(workSessions);
        workSessions.remove(workSessions.get(Integer.parseInt(session)));
        this.autoSchedule(event, eventManager);
    }

    /**
     * Method which marks complete a session for the Event
     *
     * @param event        An Event
     * @param session      A String which is the session
     * @param eventManager An EventManager
     */
    public void markComplete(UUID event, String session, EventManager eventManager) {
        List<Event> workSessions = eventManager.timeOrder(workSessionManager.getWorkSessions(eventManager, event));
        workSessionManager.setHoursNeeded(eventManager, event, (long) (workSessionManager.getHoursNeeded(eventManager, event) -
                eventManager.getLength(workSessions.get(Integer.parseInt(session)))));
        workSessions.remove(workSessions.get(Integer.parseInt(session)));
        this.autoSchedule(event, eventManager);
    }

    /**
     * @param deadline     the deadline event
     * @param hoursNeeded  whole long number of hours of this event to be scheduled
     * @param eventManager the eventManager to autoSchedule around
     */
    public void setHoursNeeded(UUID deadline, Long hoursNeeded, EventManager eventManager) {
        workSessionManager.setHoursNeeded(eventManager, deadline, hoursNeeded);
        this.autoSchedule(deadline, eventManager);
    }

    /**
     * A method for setting the length of the session
     *
     * @param deadline      An Event
     * @param sessionLength A Long which is the session length
     * @param eventManager  An EventManager
     */
    public void setSessionLength(UUID deadline, Long sessionLength, EventManager eventManager) {
        workSessionManager.setSessionLength(eventManager, deadline, sessionLength);
        this.autoSchedule(deadline, eventManager);
    }

    public void addDayOrderer(DayOrderer dayOrderer) {
        this.dayOrderers.add(dayOrderer);
    }

    public void addTimeOrderer(TimeOrderer timeOrderer) {
        this.timeOrderers.add(timeOrderer);
    }

    public void autoSchedule(UUID deadline, EventManager eventManager) {
        long totalHours = eventManager.getTotalHoursNeeded(deadline)- (long)
                (eventManager.totalHours(workSessionManager.getPastSessions(eventManager, deadline)));

        workSessionManager.setWorkSessions(eventManager, deadline, workSessionManager.getPastSessions(eventManager, deadline));

        while (totalHours > 0) {
            //Step one: determine the length the work session should be by default
            Long length = this.getLength(deadline, totalHours, eventManager);
            //step two: get a list of eligible times the event could take place according to
            // timeGetters analysis of scheduleGetters list of interfering events
            List<LocalDateTime> times = this.timeGetter.getStartTimes(deadline, eventManager, length);
            //step three: determines the ideal start time
            LocalDateTime idealStartTime = this.bestTime(deadline, length, eventManager, times);

            if(!(idealStartTime == null)){
                //step four: adds the work sessions and merges adjacent work sessions
                workSessionManager.addWorkSession(eventManager, deadline, idealStartTime, idealStartTime.plusHours(length));
                this.mergeSessions(deadline, eventManager, workSessionManager.getWorkSessions(eventManager, deadline)
                        .get(workSessionManager.getWorkSessions(eventManager, deadline).size()-1));
            }
            totalHours -= length;
        }
    }

    //private methods and helpers


    //checks if session intersects with or flows into other work session for this event - if it does, merge them into
    //one event
    private void mergeSessions(UUID deadline, EventManager eventManager, Event newSession) {
        Event toMerge = timeGetter.sessionAdjacent(deadline, eventManager, newSession);
        if (toMerge != null) {
            workSessionManager.removeWorkSession(eventManager, deadline, newSession);
            workSessionManager.removeWorkSession(eventManager, deadline, toMerge);
            if (eventManager.getStart(newSession).isBefore(eventManager.getStart(toMerge))) {
                workSessionManager.addWorkSession(eventManager, deadline, eventManager.getStart(newSession), eventManager.getEnd(toMerge));
            } else {
                workSessionManager.addWorkSession(eventManager, deadline, eventManager.getStart(toMerge), eventManager.getEnd(newSession));
            }
            mergeSessions(deadline, eventManager, workSessionManager.getWorkSessions(eventManager, deadline).get(workSessionManager.getWorkSessions(eventManager, deadline).size()-1));
        }
    }

    private Long getLength(UUID deadline, Long totalHours, EventManager eventManager) {
        Long length = eventManager.getEventSessionLength(deadline);
        if (eventManager.getEventSessionLength(deadline) > totalHours) {
            length = totalHours;
        }
        return length;
    }

    private List<LocalDate> filterDays(UUID deadline, List<LocalDateTime> times, EventManager eventManager) {
        List<LocalDate> eligibleDates = this.getDates(times);
        if (!this.dayOrderers.isEmpty()) {
            for (DayOrderer dayOrderer : this.dayOrderers) {
                dayOrderer.order(deadline, eventManager, eligibleDates, timeGetter.getDaySchedule(eventManager, deadline));
            }
        }
        return eligibleDates;
    }

    private LocalDateTime bestTime(UUID deadline, Long length, EventManager eventManager, List<LocalDateTime> times) {
        if (!this.timeOrderers.isEmpty()) {
            for (TimeOrderer timeOrderer : this.timeOrderers) {
                timeOrderer.order(deadline, eventManager, length, this.filterDays(deadline, times, eventManager), times,
                        timeGetter);
            }
        }
        if (times.isEmpty()){
            return null;
        }
        return times.get(0);
    }

    private List<LocalDate> getDates(List<LocalDateTime> times) {
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDateTime time : times) {
            if (!dates.contains(time.toLocalDate())) {
                dates.add(time.toLocalDate());
            }
        }
        return dates;
    }

    public void changeStartWorking(UUID eventID, LocalDate date, EventManager eventManager) {
        eventManager.changeStartWorking(eventID, date);
        autoSchedule(eventID, eventManager);
    }
}
