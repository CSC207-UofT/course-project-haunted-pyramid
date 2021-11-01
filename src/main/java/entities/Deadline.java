package entities;

import java.util.Map;

public class Deadline extends EventCollection{
    //TODO define rules list for deadline collections. Implement 'update' and other methods
    @Override
    protected void changeRules(String[] newRules) {

    }

    @Override
    protected Integer[] getStart(Integer count) {
        return new Integer[0];
    }

    @Override
    protected Integer[] getEnd(Integer count) {
        return new Integer[0];
    }

    @Override
    protected Integer getSize() {
        return null;
    }

    @Override
    protected Integer[] getCollection() {
        return new Integer[0];
    }

    @Override
    protected void addToCollection(Integer ID) {

    }

    @Override
    public void update(String addRemoveChange, Map<Integer, Event> changed) {

    }
}
