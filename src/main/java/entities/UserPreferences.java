package entities;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPreferences {
    private boolean procrastinate;

    private boolean morningPerson;

    private final List<String> validSpacing = List.of("none", "short", "medium", "long");
    private String spacingSameDay; // short, medium, large, or null
    private boolean cram;

    private final Map<LocalTime, LocalTime> freeTime;

    public UserPreferences() {
        this.freeTime = new HashMap<>();
        this.freeTime.put(LocalTime.of(21, 0), LocalTime.of(23, 59));
        this.freeTime.put(LocalTime.of(0, 0), LocalTime.of(9, 0));
        this.procrastinate = false;
        this.morningPerson = true;
        this.cram = false;
        this.spacingSameDay = "none";
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

    public void setMorningPerson(boolean morningPerson){
        this.morningPerson = morningPerson;
    }
    public boolean getMorningPerson(){return this.morningPerson;}

    public void setSpacingSameDay(String spacing){
        if (validSpacing.contains(spacing)){
            this.spacingSameDay = spacing;
        }
    }
    public String getSpacingSameDay(){return this.spacingSameDay;}

    public void setCram(boolean evenSpacing){
        this.cram = evenSpacing;
    }
    public boolean getCram(){return this.cram;}
}
