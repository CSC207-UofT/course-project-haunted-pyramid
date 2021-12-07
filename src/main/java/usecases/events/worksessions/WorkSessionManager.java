package usecases.events.worksessions;

import entities.Event;
import interfaces.WorkSessionInfoGetter;
import usecases.events.EventManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkSessionManager implements WorkSessionInfoGetter {
    private final EventManager eventManager;

    public WorkSessionManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void setWorkSessions(UUID ID, List<Event> sessions){
        eventManager.get(ID).setWorkSessions(sessions);
    }

    public List<Event> getPastSessions(UUID ID){
        return eventManager.get(ID).pastWorkSessions();
    }

    public List<Event> getWorkSessions(UUID ID){
        return eventManager.get(ID).getWorkSessions();
    }

    public void removeWorkSession(UUID id, Event session) {
        getWorkSessions(id).remove(session);
    }

    public void addWorkSession(UUID ID, LocalDateTime start, LocalDateTime end){
        eventManager.get(ID).addWorkSession(start, end);
    }

    public void setSessionLength(UUID ID, Long sessionLength) {
        eventManager.get(ID).setSessionLength(sessionLength);
    }

    public void setHoursNeeded(UUID deadline, Long hoursNeeded) {
        eventManager.get(deadline).setHoursNeeded(hoursNeeded);
    }

    public double getHoursNeeded(UUID event) {
        return eventManager.get(event).getHoursNeeded();
    }

    /**
     * Return the events' total work session list
     * @param eventID ID of the event
     * @return list of the total work session
     */
    public List<Event> getTotalWorkSession(UUID eventID) {
        if (eventManager.containsID(eventID)) {
            return eventManager.timeOrder(eventManager.get(eventID).getWorkSessions());
        }
        return null;
    }

    /**
     * Return the list of the past work sessions of the event
     * @param id ID of the event
     * @return list of the past work session
     */
    public List<Event> getPastWorkSession(UUID id) {
        if (eventManager.containsID(id)) {
            List<Event> totalWorkSession = eventManager.get(id).getWorkSessions();
            List<Event> pastWorkSession = new ArrayList<>();
            for (Event event : totalWorkSession) {
                if (event.getEndTime().isBefore(LocalDateTime.now())) {
                    pastWorkSession.add(event);
                }
            }
            return eventManager.timeOrder(pastWorkSession);
        }
        else {
            return null;
        }
    }

    /**
     * Return the list of the future work sessions of the event
     * @param id ID of the event
     * @return the list of the future work sessions of the event
     */
    public List<Event> getFutureWorkSession(UUID id) {
        if (eventManager.containsID(id)) {
            List<Event> totalWorkSession = eventManager.get(id).getWorkSessions();
            List<Event> futureWorkSession = new ArrayList<>();
            for (Event event : totalWorkSession) {
                if (event.getEndTime().isAfter(LocalDateTime.now())) {
                    futureWorkSession.add(event);
                }
            }
            return eventManager.timeOrder(futureWorkSession);
        }
        else {
            return null;
        }
    }

    public String getPastSessionsString(UUID id){
        StringBuilder options = new StringBuilder();
        for (Event session: this.getPastWorkSession(id)){
            options.append("\n").append(this.getTotalWorkSession(id).indexOf(session)).append(" ----\n ").append(session);
        }
        return options.toString();
    }

    public String getFutureSessionsString(UUID id){
        StringBuilder options = new StringBuilder();
        for (Event session: this.getFutureWorkSession(id)){
            options.append("\n").append(this.getTotalWorkSession(id).indexOf(session)).append(" ----\n ").append(session);
        }
        return options.toString();
    }

    /**
     * Return the total session hours of the event
     * @param id ID of the event
     * @return the total session hours set by the event
     */
    @Override
    public Long getTotalHoursNeeded(UUID id) {
        if (eventManager.containsID(id)) {
            return eventManager.get(id).getHoursNeeded();
        }
        else {
            return null;
        }
    }

    /**
     * Return the session length of the event given by the ID
     * @param eventID ID of the event
     * @return session length of the event
     */
    @Override
    public Long getEventSessionLength(UUID eventID) {
        if (eventManager.containsID(eventID)) {
            return eventManager.get(eventID).getSessionLength();
        }
        else {
            return null;
        }
    }
}
