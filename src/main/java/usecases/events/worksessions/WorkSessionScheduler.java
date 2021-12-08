package usecases.events.worksessions;

import entities.Event;

import usecases.events.worksessions.strategies.TimeGetters.DefaultTimeGetter;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;
import usecases.events.worksessions.strategies.DayOrderer.DayOrderer;

import usecases.events.worksessions.strategies.TimeOrderer.TimeOrderer;

import usecases.events.EventManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * manager for automatically scheduling events
 *
 * @author Taite Cullen
 */
public class WorkSessionScheduler {
    private final TimeGetter timeGetter;
    private final List<DayOrderer> dayOrderers;
    private final List<TimeOrderer> timeOrderers;

    /**
     * initiates a workSessionScheduler with default time getter and no day orderer
     * or time orderers
     *
     * @param freeTime map of key: start time to value: end time
     */
    public WorkSessionScheduler(Map<LocalTime, LocalTime> freeTime) {
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
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        eventManager.eventHelper.timeOrder(workSessionManager.getWorkSessions(event));
        this.markInComplete(event, eventManager.getDefaultEventInfoGetter().getID(workSessionManager.getWorkSessions(event).get(Integer.parseInt(session))), eventManager);
    }

    /**
     * Method which marks incomplete a session for the Event
     *
     * @param event        An Event
     * @param session      UUID of the session
     * @param eventManager An EventManager
     */
    public void markInComplete(UUID event, UUID session, EventManager eventManager) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        workSessionManager.getWorkSessions(event).remove(eventManager.get(session));
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
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        this.markComplete(event, eventManager.getDefaultEventInfoGetter().getID(workSessionManager.getWorkSessions(event).get(Integer.parseInt(session))), eventManager);
    }

    /**
     * Method which marks complete a session for the Event
     *
     * @param event        An Event
     * @param session      A String which is the session
     * @param eventManager An EventManager
     */
    public void markComplete(UUID event, UUID session, EventManager eventManager) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        workSessionManager.setHoursNeeded(event, (long) (workSessionManager.getHoursNeeded(event) -
                eventManager.getDefaultEventInfoGetter().getLength(eventManager.get(session))));
        workSessionManager.getWorkSessions(event).remove(eventManager.get(session));

        this.autoSchedule(event, eventManager);
    }

    /**
     * @param deadline     the deadline event
     * @param hoursNeeded  whole long number of hours of this event to be scheduled
     * @param eventManager the eventManager to autoSchedule around
     */
    public void setHoursNeeded(UUID deadline, Long hoursNeeded, EventManager eventManager) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        workSessionManager.setHoursNeeded(deadline, hoursNeeded);
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
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        workSessionManager.setSessionLength(deadline, sessionLength);
        this.autoSchedule(deadline, eventManager);
    }

    /**
     * appends a DayOrderer to the end of the list of DayOrderers
     *
     * @param dayOrderer DayOrderer to add
     */
    public void addDayOrderer(DayOrderer dayOrderer) {
        this.dayOrderers.add(dayOrderer);
    }

    /**
     * appends a TimeOrderer to the end of the list of TimeOrderers
     *
     * @param timeOrderer TimeOrderer to add
     */
    public void addTimeOrderer(TimeOrderer timeOrderer) {
        this.timeOrderers.add(timeOrderer);
    }

    /**
     * 1. deletes all future work sessions
     * 2. calculates total hours of session to schedule
     * 3. automatically schedules work sessions according to a) time getter, b) day orderers in order c) time
     * orderers in order
     *
     * @param deadline     the UUID of the event to schedule workSessions for
     * @param eventManager the EventManager deadline belongs to schedule workSessions in
     */
    public void autoSchedule(UUID deadline, EventManager eventManager) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        long totalHours = workSessionManager.getTotalHoursNeeded(deadline) - (long)
                (eventManager.totalHours(workSessionManager.getPastSessions(deadline)));

        workSessionManager.setWorkSessions(deadline, workSessionManager.getPastSessions(deadline));

        while (totalHours > 0) {
            //Step one: determine the length the work session should be by default
            Long length = this.getLength(deadline, totalHours, eventManager);
            //step two: get a list of eligible times the event could take place according to
            // timeGetters analysis of scheduleGetters list of interfering events
            List<LocalDateTime> times = this.timeGetter.getStartTimes(deadline, eventManager, length);
            //step three: determines the ideal start time
            LocalDateTime idealStartTime = this.bestTime(deadline, length, eventManager, times);

            if (!(idealStartTime == null)) {
                //step four: adds the work sessions and merges adjacent work sessions
                workSessionManager.addWorkSession(deadline, idealStartTime, idealStartTime.plusHours(length));
                this.mergeSessions(deadline, eventManager, workSessionManager.getWorkSessions(deadline)
                        .get(workSessionManager.getWorkSessions(deadline).size() - 1));
            }
            totalHours -= length;
        }
    }

    //private methods and helpers


    /**
     * checks if newSession intersects with or flows into other work session for this event - if it does, merge them into
     * one event
     *
     * @param deadline     UUID of deadline Event
     * @param eventManager eventManager that workSessions are scheduled into
     * @param newSession   a session being added
     */
    private void mergeSessions(UUID deadline, EventManager eventManager, Event newSession) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        Event toMerge = sessionAdjacent(deadline, eventManager, newSession);
        if (toMerge != null) {
            workSessionManager.removeWorkSession(deadline, newSession);
            workSessionManager.removeWorkSession(deadline, toMerge);
            if (eventManager.getDefaultEventInfoGetter().getStart(newSession).isBefore(eventManager.getDefaultEventInfoGetter().getStart(toMerge))) {
                workSessionManager.addWorkSession(deadline, eventManager.getDefaultEventInfoGetter().getStart(newSession), eventManager.getDefaultEventInfoGetter().getEnd(toMerge));
            } else {
                workSessionManager.addWorkSession(deadline, eventManager.getDefaultEventInfoGetter().getStart(toMerge), eventManager.getDefaultEventInfoGetter().getEnd(newSession));
            }
            mergeSessions(deadline, eventManager, workSessionManager.getWorkSessions(deadline).get(workSessionManager.getWorkSessions(deadline).size() - 1));
        }
    }

    /**
     * calculates the length of a workSession by taking the maximum between the preferred length and total hours needed
     * to be scheduled
     *
     * @param deadline     UUID of deadline Event
     * @param totalHours   total hours needed
     * @param eventManager EventManager
     * @return maximum between totalHours and preferred length of deadline
     */
    private Long getLength(UUID deadline, Long totalHours, EventManager eventManager) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        Long length = workSessionManager.getEventSessionLength(deadline);
        if (workSessionManager.getEventSessionLength(deadline) > totalHours) {
            length = totalHours;
        }
        return length;
    }

    /**
     * gets a list of all dates in input times, then sorts in according to
     * each DayOrderers in index order
     *
     * @param deadline     UUID of deadline event
     * @param times        a list of LocalDateTimes
     * @param eventManager EventManager
     * @return ordered list of dates
     */
    private List<LocalDate> filterDays(UUID deadline, List<LocalDateTime> times, EventManager eventManager) {
        List<LocalDate> eligibleDates = this.getDates(times);
        if (!this.dayOrderers.isEmpty()) {
            for (DayOrderer dayOrderer : this.dayOrderers) {
                dayOrderer.order(deadline, eventManager, eligibleDates, timeGetter.getDaySchedule(eventManager, deadline));
            }
        }
        return eligibleDates;
    }

    /**
     * returns the absolute optimal LocalDateTime from a list of eligible times according to all Time Orderers and idealDates
     *
     * @param deadline     UUID of deadline event
     * @param length       preferred session length
     * @param eventManager EventManager
     * @param times        a list of eligible LocalDateTimes
     * @return a LocalDateTime - the first element in times after sorting
     */
    private LocalDateTime bestTime(UUID deadline, Long length, EventManager eventManager, List<LocalDateTime> times) {
        if (!this.timeOrderers.isEmpty()) {
            for (TimeOrderer timeOrderer : this.timeOrderers) {
                timeOrderer.order(deadline, eventManager, length, this.filterDays(deadline, times, eventManager), times,
                        timeGetter);
            }
        }
        if (times.isEmpty()) {
            return null;
        }
        return times.get(0);
    }

    /**
     * returns a list of dates that occur over input times
     *
     * @param times LocalDateTimes
     * @return List of LocalDates (unordered and non-repeating)
     */
    private List<LocalDate> getDates(List<LocalDateTime> times) {
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDateTime time : times) {
            if (!dates.contains(time.toLocalDate())) {
                dates.add(time.toLocalDate());
            }
        }
        return dates;
    }

    /**
     * changes startWorking of deadline event by setting it to a difference between deadline end and input date
     *
     * @param eventID      UUID of Event
     * @param date         LocalDate the new date to start working (with current end time)
     * @param eventManager EventManager
     */
    public void changeStartWorking(UUID eventID, LocalDate date, EventManager eventManager) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        workSessionManager.changeStartWorking(eventID, date);
        autoSchedule(eventID, eventManager);
    }

    /**
     * changes startWorking of deadline event
     *
     * @param eventID      UUID of Event
     * @param date         the new number of days between starting work sessions and the deadline end time
     * @param eventManager EventManager
     */
    public void changeStartWorking(UUID eventID, Long date, EventManager eventManager) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        workSessionManager.changeStartWorking(eventID, date);
        autoSchedule(eventID, eventManager);
    }

    private Event sessionAdjacent(UUID deadline, EventManager eventManager, Event newSession) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        for (Event session : workSessionManager.getWorkSessions(deadline)) {
            if (eventManager.getDefaultEventInfoGetter().getEnd(session).isEqual(eventManager.getDefaultEventInfoGetter().getStart(newSession)) || eventManager.getDefaultEventInfoGetter().getStart(
                    session).isEqual(eventManager.getDefaultEventInfoGetter().getEnd(newSession))) {
                return session;
            }
        }
        return null;
    }
}
