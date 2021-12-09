package usecases.events.worksessions;

import entities.Event;
import interfaces.WorkSessionInfoGetter;
import usecases.events.EventManager;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Use Case level management of work session related attributes of Events in an EventManager
 *
 * @author Sean Yi
 * @author Taite Cullen
 */
public class WorkSessionManager implements WorkSessionInfoGetter {
    private final EventManager eventManager;

    /**
     * constructor for a WorkSessionManager - stores an EventManager to access events by ID
     *
     * @param eventManager eventManager stores events to be accessed and modified by UUID
     */
    public WorkSessionManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * sets Event.workSessions of event in eventManager with specified ID
     *
     * @param ID       UUID of Event
     * @param sessions list of work sessions to be stored in event
     */
    public void setWorkSessions(UUID ID, List<Event> sessions) {
        eventManager.get(ID).setWorkSessions(sessions);
    }

    /**
     * @param ID UUID of Event
     * @return list of work sessions whose start times are before now
     */
    public List<Event> getPastSessions(UUID ID) {
        return eventManager.get(ID).pastWorkSessions();
    }

    /**
     * @param ID UUID of Event
     * @return lise of all work sessions in event with UUID
     */
    public List<Event> getWorkSessions(UUID ID) {
        return eventManager.get(ID).getWorkSessions();
    }

    /**
     * @param id      UUID of event,
     * @param session work session
     */
    public void removeWorkSession(UUID id, Event session) {
        getWorkSessions(id).remove(session);
    }

    /**
     * @param ID    UUID of event
     * @param start LocalDateTime the start time of the work session
     * @param end   LocalDateTime the end time of the work session
     */
    public void addWorkSession(UUID ID, LocalDateTime start, LocalDateTime end) {
        eventManager.get(ID).addWorkSession(start, end);
    }

    /**
     * @param ID            UUID of event
     * @param sessionLength Long new preferred length of work sessions for event
     */
    public void setSessionLength(UUID ID, Long sessionLength) {
        eventManager.get(ID).setSessionLength(sessionLength);
    }

    /**
     * @param deadline    UUID
     * @param hoursNeeded Long
     */
    public void setHoursNeeded(UUID deadline, Long hoursNeeded) {
        eventManager.get(deadline).setHoursNeeded(hoursNeeded);
    }

    public double getHoursNeeded(UUID event) {
        return eventManager.get(event).getHoursNeeded();
    }

    /**
     * Return the events' total work session list
     *
     * @param eventID ID of the event
     * @return list of the total work session Events
     */
    public List<Event> getTotalWorkSession(UUID eventID) {
        if (eventManager.containsID(eventID)) {
            return eventManager.eventHelper.timeOrder(eventManager.get(eventID).getWorkSessions());
        }
        return null;
    }

    /**
     * @param eventID UUID of Event
     * @return Long number of days before deadline to start working
     */
    public Long getStartWorking(UUID eventID) {
        if (eventManager.containsID(eventID)) {
            return eventManager.get(eventID).getStartWorking();
        }
        return null;
    }

    /**
     * Return the list of the past work sessions of the event
     *
     * @param id ID of the event
     * @return list of the past work session
     */
    public List<UUID> getPastWorkSession(UUID id) {
        if (eventManager.containsID(id)) {
            List<Event> totalWorkSession = eventManager.get(id).getWorkSessions();
            eventManager.eventHelper.timeOrder(totalWorkSession);
            List<UUID> pastWorkSession = new ArrayList<>();
            for (Event event : totalWorkSession) {
                if (event.getEndTime().isBefore(LocalDateTime.now())) {
                    pastWorkSession.add(eventManager.getDefaultEventInfoGetter().getID(event));
                }
            }
            return pastWorkSession;
        } else {
            return null;
        }
    }

    /**
     * Return the list of the future work sessions of the event
     *
     * @param id ID of the event
     * @return the list of the future work sessions of the event
     */
    public List<UUID> getFutureWorkSession(UUID id) {
        if (eventManager.containsID(id)) {
            List<Event> totalWorkSession = eventManager.get(id).getWorkSessions();
            eventManager.eventHelper.timeOrder(totalWorkSession);
            List<UUID> futureWorkSession = new ArrayList<>();
            for (Event event : totalWorkSession) {
                if (event.getEndTime().isAfter(LocalDateTime.now())) {
                    futureWorkSession.add(eventManager.getDefaultEventInfoGetter().getID(event));
                }
            }
            return futureWorkSession;
        } else {
            return null;
        }
    }

    /**
     * @param id UUID of event
     * @return a string representation of the past work sessions of event with id
     */
    public String getPastSessionsString(UUID id) {
        StringBuilder options = new StringBuilder();
        for (UUID session : this.getPastWorkSession(id)) {
            options.append("\n").append(this.getTotalWorkSession(id).indexOf(eventManager.get(session))).append(" ----\n ").append(eventManager.get(session));
        }
        return options.toString();
    }

    /**
     * @param id id of Event
     * @return a string representation of the future work sessions of event with id
     */
    public String getFutureSessionsString(UUID id) {
        StringBuilder options = new StringBuilder();
        for (UUID session : this.getFutureWorkSession(id)) {
            options.append("\n").append(this.getTotalWorkSession(id).indexOf(eventManager.get(session))).append(" ----\n ").append(eventManager.get(session));
        }
        return options.toString();
    }

    /**
     * Return the total session hours of the event
     *
     * @param id ID of the event
     * @return the total session hours set by the event
     */
    @Override
    public Long getTotalHoursNeeded(UUID id) {
        if (eventManager.containsID(id)) {
            return eventManager.get(id).getHoursNeeded();
        } else {
            return null;
        }
    }

    /**
     * Return the session length of the event given by the ID
     *
     * @param eventID ID of the event
     * @return session length of the event
     */
    @Override
    public Long getEventSessionLength(UUID eventID) {
        if (eventManager.containsID(eventID)) {
            return eventManager.get(eventID).getSessionLength();
        } else {
            return null;
        }
    }

    /**
     * changes the event startWorking Long to the days between input date and event end date
     *
     * @param event UUID of event
     * @param start new LocalDate startWorking
     */
    public void changeStartWorking(UUID event, LocalDate start) {
        eventManager.get(event).setStartWorking(Duration.between(LocalDateTime.of(start, LocalTime.of(0, 0)),
                LocalDateTime.of(eventManager.getDefaultEventInfoGetter().getEndDate(event), LocalTime.of(0, 0))).toDays());
    }

    /**
     * changes the event startWorking Long to input start Long
     *
     * @param event UUID of event
     * @param date  new Long start
     */
    public void changeStartWorking(UUID event, Long date) {
        eventManager.get(event).setStartWorking(date);
    }

    @Override
    public List<UUID> getPastWorkSessions(UUID ID) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        return workSessionManager.getPastWorkSession(ID);
    }

    @Override
    public List<UUID> getFutureWorkSessions(UUID ID) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        return workSessionManager.getFutureWorkSession(ID);
    }
}
