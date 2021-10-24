package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;

import interfaces.Fluid;

public class Assignment extends Event implements Fluid{
    //TODO properly implement these - change constructor so due date different - override conflict
    /**
     * constructor sets the ID, name, start and end times of the Event
     *
     * @param ID        the id for this event and events related to it (repetition etc)
     * @param name      the name of the event
     * @param dueDate   start time of the event
     */
    public Assignment(int ID, String name, LocalDateTime dueDate) {
        super(ID, name, dueDate, dueDate);
    }
    public Assignment(int ID, String name, int year, int month, int day, int dueHour, int dueMin){
        super(ID, name, year, month, day, dueHour, dueHour, dueMin, dueMin);
    }

    @Override
    public float getFluidHours() {
        return 0;
    }

    @Override
    public float getSessionsLength() {
        return 0;
    }

    @Override
    public float setSessionLength(float hours) {
        return 0;
    }

    @Override
    public float setFluidHours(float hours) {
        return 0;
    }
}
