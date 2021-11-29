package usecases.events.worksessions;

import entities.Event;
import interfaces.EventListObserver;
import usecases.events.worksessions.strategies.DayOrderer.FewestSessions;
import usecases.events.worksessions.strategies.TimeGetters.DefaultTimeGetter;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;
import usecases.events.worksessions.strategies.DayOrderer.DayOrderer;
import usecases.events.worksessions.strategies.TimeOrderer.MorningPerson;
import usecases.events.worksessions.strategies.TimeOrderer.TimeOrderer;

import usecases.events.EventManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class WorkSessionScheduler implements EventListObserver {
    private TimeGetter timeGetter;
    private List<DayOrderer> dayOrderers = null;
    private List<TimeOrderer> timeOrderers = null;

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
        eventManager.timeOrder(eventManager.getWorkSessions(event));
        eventManager.getWorkSessions(event).remove(eventManager.getWorkSessions(event).get(Integer.parseInt(session)));
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
        eventManager.timeOrder(eventManager.getWorkSessions(event));
        eventManager.setHoursNeeded(event, (long) (eventManager.getHoursNeeded(event) -
                eventManager.getLength(eventManager.getWorkSessions(event).get(Integer.parseInt(session)))));
        eventManager.getWorkSessions(event).remove(eventManager.getWorkSessions(event).get(Integer.parseInt(session)));
        this.autoSchedule(event, eventManager);
    }

    /**
     * @param deadline     the deadline event
     * @param hoursNeeded  whole long number of hours of this event to be scheduled
     * @param eventManager the eventManager to autoSchedule around
     */
    public void setHoursNeeded(UUID deadline, Long hoursNeeded, EventManager eventManager) {
        eventManager.setHoursNeeded(deadline, hoursNeeded);
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
        eventManager.setSessionLength(deadline, sessionLength);
        this.autoSchedule(deadline, eventManager);
    }

    /**
     * @param addRemoveChange what to do with the events in the map, 'add', 'remove' or 'change'
     *                        * where 'change' indicates a change of date
     * @param changed         map of days to events, where days are the
     * @param eventManager    the eventManager that was updated
     */
    @Override
    public void update(String addRemoveChange, Event changed, EventManager eventManager) {
        for (Event event : eventManager.getAllEvents()) {
            this.autoSchedule(eventManager.getID(event), eventManager);
        }
    }

    public void setDayOrderers(List<DayOrderer> dayOrderers) {
        this.dayOrderers = dayOrderers;
    }

    public void addDayOrderer(DayOrderer dayOrderer) {
        this.dayOrderers.add(dayOrderer);
    }

    public void setTimeOrderers(List<TimeOrderer> timeOrderers) {
        this.timeOrderers = timeOrderers;
    }

    public void addTimeOrderer(TimeOrderer timeOrderer) {
        this.timeOrderers.add(timeOrderer);
    }

    public void setTimeGetter(TimeGetter timeGetter) {
        this.timeGetter = timeGetter;
    }

    public void autoSchedule(UUID deadline, EventManager eventManager) {
        long totalHours = eventManager.getTotalHoursNeeded(deadline) - (long)
                (eventManager.totalHours(eventManager.getPastSessions(deadline)));

        eventManager.setWorkSessions(deadline, eventManager.getPastSessions(deadline));

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
                eventManager.addWorkSession(deadline, idealStartTime, idealStartTime.plusHours(length));
                this.mergeSessions(deadline, eventManager, idealStartTime, length, eventManager.getWorkSessions(deadline)
                        .get(eventManager.getWorkSessions(deadline).size() - 1));
                totalHours -= length;
            }

        }
    }


    //private methods and helpers


    //checks if session intersects with or flows into other work session for this event - if it does, merge them into
    //one event
    private void mergeSessions(UUID deadline, EventManager eventManager, LocalDateTime idealStartTime, Long length, Event newSession) {
        Event toMerge = timeGetter.sessionAdjacent(idealStartTime, length, deadline, eventManager);
        if (toMerge != null) {
            eventManager.removeWorkSession(deadline, newSession);
            eventManager.removeWorkSession(deadline, toMerge);
            if (eventManager.getStart(newSession).isBefore(eventManager.getStart(toMerge))) {
                eventManager.addWorkSession(deadline, eventManager.getStart(newSession), eventManager.getEnd(toMerge));
            } else {
                eventManager.addWorkSession(deadline, eventManager.getStart(toMerge), eventManager.getEnd(newSession));
            }
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
        if (this.dayOrderers != null) {
            for (DayOrderer dayOrderer : this.dayOrderers) {
                dayOrderer.order(deadline, eventManager, eligibleDates, timeGetter.getDaySchedule(eventManager, deadline));
            }
        }
        return eligibleDates;
    }

    private LocalDateTime bestTime(UUID deadline, Long length, EventManager eventManager, List<LocalDateTime> times) {
        if (this.timeOrderers != null) {
            for (TimeOrderer timeOrderer : this.timeOrderers) {
                timeOrderer.order(deadline, eventManager, length, this.filterDays(deadline, times, eventManager), times,
                        timeGetter);
            }
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


    public static void main(String[] args) {
        HashMap<LocalTime, LocalTime> freeTime = new HashMap<>();
        freeTime.put(LocalTime.of(21, 0), LocalTime.of(11, 59));
        freeTime.put(LocalTime.of(0, 0), LocalTime.of(9, 0));
        WorkSessionScheduler workSessionScheduler = new WorkSessionScheduler(freeTime);
        EventManager eventManager = new EventManager(new ArrayList<>());
        UUID deadline = eventManager.getID(eventManager.addEvent("deadline", LocalDateTime.of(2021, 12, 05, 2, 30)));
        workSessionScheduler.setDayOrderers(List.of(new FewestSessions()));
        workSessionScheduler.setTimeOrderers(List.of(new MorningPerson()));
        workSessionScheduler.setHoursNeeded(deadline, 10L, eventManager);
        System.out.println(eventManager.getWorkSessions(deadline));

        workSessionScheduler.autoSchedule(eventManager.getID(eventManager.getWorkSessions(deadline).get(0)),
                eventManager);
    }
}
