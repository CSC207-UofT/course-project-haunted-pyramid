package usecases.events.worksessions;

import entities.Event;
import usecases.events.EventManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class WorkSessionManager {
    public void setWorkSessions(EventManager eventManager, UUID ID, List<Event> sessions){
        eventManager.get(ID).setWorkSessions(sessions);
    }

    public List<Event> getPastSessions(EventManager eventManager, UUID ID){
        return eventManager.get(ID).pastWorkSessions();
    }

    public List<Event> getWorkSessions(EventManager eventManager, UUID ID){
        return eventManager.get(ID).getWorkSessions();
    }

    public void removeWorkSession(EventManager eventManager, UUID id, Event session) {
        getWorkSessions(eventManager, id).remove(session);
    }

    public void addWorkSession(EventManager eventManager, UUID ID, LocalDateTime start, LocalDateTime end){
        eventManager.get(ID).addWorkSession(start, end);
    }

    public void setSessionLength(EventManager eventManager, UUID ID, Long sessionLength) {
        eventManager.get(ID).setSessionLength(sessionLength);
    }

    public void setHoursNeeded(EventManager eventManager, UUID deadline, Long hoursNeeded) {
        eventManager.get(deadline).setHoursNeeded(hoursNeeded);
    }

    public double getHoursNeeded(EventManager eventManager, UUID event) {
        return eventManager.get(event).getHoursNeeded();
    }
}
