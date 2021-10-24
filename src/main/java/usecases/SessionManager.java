package usecases;
import entities.Event;
import interfaces.Fluid;

import java.util.ArrayList;
import java.util.*;

public class SessionManager {
    private ArrayList<Fluid> sessions;
    public SessionManager(ArrayList<Fluid> sessions){
        this.sessions = sessions;
    }
    public SessionManager(){
        this.sessions = new ArrayList<Fluid>();
    }
    /**
     *
     */
    public <T extends Event> Set<Event> schedule(T event){
        /**
         *
         */
        Set<Event> changes = new HashSet<Event>();
        return changes;
    }
}
