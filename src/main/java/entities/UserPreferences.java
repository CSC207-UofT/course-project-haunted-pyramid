package entities;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Taite Cullen
 */
public class UserPreferences implements Serializable {
    private boolean procrastinate;

    private boolean morningPerson;

    private final List<String> validSpacing = List.of("none", "short", "medium", "long");
    private String spacingSameDay; // short, medium, large, or null
    private boolean cram;

    private final Map<LocalTime, LocalTime> freeTime;

    /**
     * Constructs a UserPreferences object set to all default preferences
     */
    public UserPreferences() {
        this.freeTime = new HashMap<>();
        this.freeTime.put(LocalTime.of(21, 0), LocalTime.of(23, 59));
        this.freeTime.put(LocalTime.of(0, 0), LocalTime.of(9, 0));
        this.procrastinate = false;
        this.morningPerson = true;
        this.cram = false;
        this.spacingSameDay = "none";
    }

    /**
     * returns truth value of procrastinate
     *
     * @return boolean procrastinate
     */
    public boolean getProcrastinate() {
        return this.procrastinate;
    }

    /**
     * returns map with key: start time to value: end time of free time
     *
     * @return Map free time
     */
    public Map<LocalTime, LocalTime> getFreeTime() {
        return this.freeTime;
    }

    /**
     * changes truth value of procrastinate to input boolean
     *
     * @param procrastinate boolean
     */
    public void setProcrastinate(boolean procrastinate) {
        this.procrastinate = procrastinate;
    }

    /**
     * adds a start and end to freeTime
     *
     * @param start LocalTime start time
     * @param end   LocalTime end time
     */
    public void setFreeTime(LocalTime start, LocalTime end) {
        this.freeTime.put(start, end);
    }

    /**
     * removes entry of this start time from freeTime
     *
     * @param start LocalTime start (key in freeTime)
     */
    public void removeFreeTime(LocalTime start) {
        this.freeTime.remove(start);
    }

    /**
     * changes truth value of morningPerson to input boolean
     *
     * @param morningPerson boolean
     */
    public void setMorningPerson(boolean morningPerson) {
        this.morningPerson = morningPerson;
    }

    /**
     * @return truth value of morningPerson
     */
    public boolean getMorningPerson() {
        return this.morningPerson;
    }

    public void setSpacingSameDay(String spacing) {
        if (validSpacing.contains(spacing)) {
            this.spacingSameDay = spacing;
        }
    }

    /**
     * returns String of prefered spacingSameDay -> "none", "small", "medium", "large"
     *
     * @return String spacing
     */
    public String getSpacingSameDay() {
        return this.spacingSameDay;
    }

    /**
     * changes truth value of cram to input boolean
     *
     * @param evenSpacing boolean
     */
    public void setCram(boolean evenSpacing) {
        this.cram = evenSpacing;
    }

    /**
     * @return truth value of cram
     */
    public boolean getCram() {
        return this.cram;
    }
}
