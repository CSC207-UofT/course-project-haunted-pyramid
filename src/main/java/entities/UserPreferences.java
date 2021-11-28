package entities;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class UserPreferences {
    private boolean procrastinate;
    private Map<LocalTime, LocalTime> freeTime;

    public UserPreferences(){
        this.freeTime = new HashMap<>();
        this.freeTime.put(LocalTime.of(21, 0), LocalTime.of(23, 59));
        this.freeTime.put(LocalTime.of(0, 0), LocalTime.of(9, 0));
    }

    public boolean getProcrastinate() {
        return this.procrastinate;
    }
    public Map<LocalTime, LocalTime> getFreeTime() {
        return this.freeTime;
    }
    public void setProcrastinate(boolean procrastinate) {
        this.procrastinate = procrastinate;
    }

    public void setFreeTime(LocalTime start, LocalTime end) {
        this.freeTime.put(start, end);
    }

    public void removeFreeTime(LocalTime start) {
        this.freeTime.remove(start);
    }
}
