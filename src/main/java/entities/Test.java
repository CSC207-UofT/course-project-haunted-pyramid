package entities;

import interfaces.Fluid;

import java.time.LocalDateTime;

public class Test extends Event implements Fluid {
    public Test(int ID, String name, LocalDateTime startTime, LocalDateTime endTime) {
        super(ID, name, startTime, endTime);
    }
    /**
     * a constructor for single day events
     * @param ID the event id
     * @param name name of event
     * @param year year (integer)
     * @param month month (integer)
     * @param day day (integer)
     * @param startHour hour event starts (integer)
     * @param endHour hour event ends (integer)
     * @param startMin minute event starts (integer)
     * @param endMin minute event ends (integer)
     */
    public Test(int ID, String name, int year, int month, int day, int startHour, int endHour, int startMin,
                 int endMin){
        super(ID, name, year, month, day, startHour, endHour, startMin, endMin);
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
