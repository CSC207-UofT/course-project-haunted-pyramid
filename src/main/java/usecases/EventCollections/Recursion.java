package usecases.EventCollections;

import entities.Event;
import usecases.EventCollections.Strategies.Scheduler;
import usecases.EventManager;

import java.util.*;
import java.util.Map;

public class Recursion extends EventCollection {
    //"FREQ"
    // ->"DAY=MON TUE WED THU FRI"
    // ->"EVERY=99 HOUR/DAY/WEEK/MONTH/YEAR"
    // ->"DAILY"
    // ->"WEEKLY"
    // ->"MONTHLY"
    // ->"NUMBER=#"
    //"END"
    // ->YYYY-MM-DD
    //"EXCEPT"
    // ->YYYY, MM, DD, TT, TT, YYYY, MM, DD, TT, TT
    //"AND"
    // ->YYYY, MM, DD, TT, TT, YYYY, MM, DD, TT, TT <- * start and end time included

    public Integer[] end;
    public static Integer nextID;

    public Recursion(String name, Integer[] initial, Integer[] end, Scheduler frequency){
        this.also = new Integer[][]{};
        this.collection = new HashMap<Integer, Event>();
        this.except = new Integer[][]{};
        this.scheduler = frequency;
        this.initial = initial;
        this.end = end;
        this.name = name;
        this.ID = nextID;
        nextID ++;
        Deadline.nextID++;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected void addExcept(Integer[] newExcept) {
        ArrayList<Integer[]> inter = new ArrayList<Integer[]>(List.of(this.except));
        inter.add(newExcept);
        this.except = inter.toArray(new Integer[0][]);
    }

    @Override
    protected void addAlso(Integer[] newAlso) {
        ArrayList<Integer[]> inter = new ArrayList<Integer[]>(List.of(this.also));
        inter.add(newAlso);
        this.also = inter.toArray(new Integer[0][]);
    }

    @Override
    public Event[] reschedule(Scheduler newScheduler) {
        this.scheduler = newScheduler;
        EventManager eventManager = new EventManager();
        ArrayList<Event> toRemove = new ArrayList<>();
        int count = 1;
        for (Event event: this.collection.values()){
            Integer[] nextTime = this.scheduler.getInstanceSchedule(this.initial, this.except, this.also, count);
            if (nextTime == null){
                toRemove.add(event);
            }else {
                eventManager.setStartEnd(event, this.scheduler.getInstanceSchedule(this.initial, this.except, this.also,
                        count));
            }
            count ++;
        }
        return toRemove.toArray(new Event[0]);
    }

    public Integer[][] toAdd(){
        Integer count = this.collection.size();
        ArrayList<Integer[]> toAdd = new ArrayList<>();
        while (this.scheduler.getInstanceSchedule(this.initial, this.except, this.also, count) != null){
            toAdd.add(this.scheduler.getInstanceSchedule(this.initial, this.except, this.also, count));
            count ++;
        }
        return toAdd.toArray(new Integer[0][]);
    }

    @Override
    public void addToCollection(Event event) {
        EventManager em = new EventManager();
        this.collection.put(em.getID(event), event);
    }

    @Override
    public Integer getID() {
        return this.ID;
    }

    @Override
    public void update(String addRemoveChange, Map<Integer, Event> changed) {
        EventManager em = new EventManager();
        if (!addRemoveChange.equalsIgnoreCase("add")){
            for (Event event: changed.values()){
                if (this.collection.containsValue(event)){
                    this.addExcept(em.getStartEndInt(event));
                }
                if (addRemoveChange.equalsIgnoreCase("change")){
                    this.addAlso(em.getStartEndInt(event));
                }
            }
        }

    }
}
