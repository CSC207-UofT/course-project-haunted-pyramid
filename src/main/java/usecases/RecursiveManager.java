package usecases;

import entities.Event;
import entities.EventCollection;
import entities.Recursion;
import interfaces.EventListObserver;

public class RecursiveManager {
    public EventManager eventManager;
    public RecursiveManager(EventManager eventManager){
        this.eventManager = eventManager;
    }
    public Recursion recurseOnInit(Event event, String freq, String end){
        String start = event.getStartString();
        Recursion recursion = new Recursion(new String[] {"START="+start, "FREQ="+freq,
                "END="+end});
        this.eventManager.addObserver(recursion);
        //event.setCollection((EventCollection) recursion);
        return recursion;
    }

    private void fill(Recursion recursion){
        for(Integer ID : recursion.getCollection()){
            //TODO find unassigned counts and uncounted ID's- > create event for each and add to EventManager, or
            // delete uncounted event from EventManager
        }
    }
}
