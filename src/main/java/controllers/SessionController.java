package controllers;

import entities.Event;
import usecases.SessionManager;

import java.util.*;

public class SessionController {
    private final SessionManager sessionManager;
    public SessionController(SessionManager sessionManager){
        this.sessionManager = sessionManager;
    }

    public Set<Event> schedule(Event event) {
        Set<Event> changes = new HashSet<Event>();
        return changes;
    }
}
