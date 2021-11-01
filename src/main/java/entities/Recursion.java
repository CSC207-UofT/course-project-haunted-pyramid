package entities;

import helpers.Scheduler;

import java.util.*;
import java.util.HashMap;
import java.util.Map;

public class Recursion extends EventCollection {
    //"FREQ"
    // ->"DAY=MON TUE WED THU FRI"
    // ->"EVERY=99 HOUR/DAY/WEEK/MONTH/YEAR"
    // ->"DAILY"
    // ->"WEEKLY"
    // ->"MONTHLY"
    //"END"
    // ->"YYYY-MM-DD"
    //"EXCEPT"
    // ->"YYYY-MM-DD TT:TT"
    //"AND"
    // ->"YYYY-MM-DD TT:TT YYYY-MM-DD TT:TT" <- * start and end time included

    public Recursion(String[] rules){
        this.rules = new HashMap<>();
        this.collection = new Integer[this.getSize()];
    }

    @Override
    protected void changeRules(String[] newRules) {
        for (String rule: newRules){
            String[] parts = rule.split("=");
            this.rules.put(parts[0], this.rules.getOrDefault(parts[0], "") + parts[1]);
        }
    }

    @Override
    protected Integer[] getStart(Integer count) {
        Integer[] countDateTime = this.scheduler.getSchedule(this.rules)[count];
        return new Integer[] {countDateTime[0], countDateTime[1], countDateTime[2], countDateTime[3], countDateTime[4]};
    }

    @Override
    protected Integer[] getEnd(Integer count) {
        Integer[] countDateTime = this.scheduler.getSchedule(this.rules)[count];
        return new Integer[] {countDateTime[0], countDateTime[1], countDateTime[2], countDateTime[5], countDateTime[6]};
    }

    @Override
    protected Integer getSize() {
        return this.scheduler.getSchedule(this.rules).length;
    }

    @Override
    public Integer[] getCollection() {
        return this.collection;
    }

    @Override
    protected void addToCollection(Integer ID) {
        ArrayList<Integer> inter = new ArrayList<>(List.of(this.collection));
        inter.add(ID);
        this.collection = inter.toArray(new Integer[0]);
    }

    @Override
    public void update(String addRemoveChange, Map<Integer, Event> changed) {
        for (Event event: changed.values()){
            for (Integer ID: this.collection){
                if (event.getID() == ID){
                    this.changeRules(new String[]{"EXCEPT=" + event.getStartString()});
                    if (addRemoveChange.equalsIgnoreCase("change")){
                        this.changeRules(new String[]{"AND=" + event.getStartString() + " " + event.getEndString()});
                    }
                }
            }
        }
    }
}
