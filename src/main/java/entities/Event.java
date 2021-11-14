package entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * A time-containing object that also references a list of other Events, is identified by an id, and contains
 * descriptive Strings of information such as name and description
 *
 * @author Taite Cullen
 * @author Malik Lahlou
 * @version %I%, %G%
 */
public class Event implements Serializable {
    private LocalDateTime startTime = null;
    private LocalDateTime endTime;
    private final int ID;
    private String name;
    private String type;
    private String description = null;
    private List<Event> workSessions;
    private Long hoursNeeded;
    private Long sessionLength;
    private Integer recursiveId;
    private boolean inCategory;
    private String categoryName;

    /**
     * constructor sets the ID, name and end time of the entities.Event, default sessionLength to 1L
     * default hours needed to 0L, default work sessions empty ArrayList
     *
     * @param ID      the id for this event and events related to it (repetition etc)
     * @param name    the name of the event
     * @param endTime end time of the event
     */
    public Event(int ID, String name, LocalDateTime endTime) {
        this.ID = ID;
        this.name = name;
        this.endTime = endTime;
        this.hoursNeeded = 0L;
        this.sessionLength = 1L;
        this.workSessions = new ArrayList<>();
    }

    /**
     * constructor sets the ID, name, start and end times of the entities.Event, default sessionLength to 1L
     * default hours needed to 0L, default work sessions empty ArrayList
     *
     * @param ID      the id for this event and events related to it (repetition etc)
     * @param name    the name of the event
     * @param endTime end time of the event
     */
    public Event(int ID, String name, LocalDateTime startTime, LocalDateTime endTime) {
        this.ID = ID;
        this.name = name;
        this.endTime = endTime;
        this.startTime = startTime;
        this.hoursNeeded = 0L;
        this.sessionLength = 1L;
        this.workSessions = new ArrayList<>();
    }

    /**
     * instantiates an event based on integer input date time
     *
     * @param ID        the id of the event
     * @param name      the name of the event
     * @param year      year
     * @param month     month
     * @param day       day
     * @param startHour the start hour
     * @param endHour   the end hour
     * @param startMin  the start minute
     * @param endMin    the end minute
     */
    public Event(int ID, String name, int year, int month, int day, int startHour, int endHour, int startMin, int endMin) {
        this.name = name;
        this.ID = ID;
        this.endTime = LocalDateTime.of(year, month, day, endHour, endMin);
        this.startTime = LocalDateTime.of(year, month, day, startHour, startMin);
        this.hoursNeeded = 0L;
        this.sessionLength = 1L;
        this.workSessions = new ArrayList<>();
    }

    /**
     * changes the description of the Event to new input value - used simply for changing notes on Event
     * for User benefit
     *
     * @param description any String of any format, purpose to describe the nature of the Event - notes on the Event
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return true if this event is not a deadline, i.e. startTime is not null
     */
    public boolean hasStart() {
        return !(this.startTime == null);
    }

    /**
     * @param recursiveId the new integer recursiveID of the event
     */
    public void setRecursiveId(int recursiveId) {
        this.recursiveId = recursiveId;
    }

    public Integer getRecursiveId() {
        return this.recursiveId;
    }

    /**
     * name of the Event, like the description, is for User benefit - a visual reference to identify an Event
     *
     * @param name new name of the Event (string) for user benefit
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets <code>this.type</code>
     *
     * @param type the new type of the event
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * returns a String with a description of the EVent as set by User - not required to be meaningful, no
     * standard format
     *
     * @return the description of the Event as set by the User, or an empty string if none has been set
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return <code>this.type</code>
     */
    public String getType() {
        return type;
    }

    /**
     * @return <code>this.categoryName</code>
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * To Events are considered equal if all attributes have the same value
     *
     * @param object the object event is being compared to
     * @return whether it is equal to this object
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object.getClass() != this.getClass()) {
            return false;
        }
        Event obj = (Event) object;
        if (this.hasStart() && obj.hasStart()) {
            return this.startTime.isEqual(obj.getStartTime()) && this.endTime.isEqual(obj.getEndTime()) &&
                    this.name.equals(obj.getName()) && this.workSessions.equals(obj.getWorkSessions())
                    && this.ID == obj.getID() && this.hoursNeeded.equals(obj.getHoursNeeded()) &&
                    this.sessionLength.equals(obj.getSessionLength());
        } else if ((!this.hasStart() && obj.hasStart()) || (this.hasStart() && !obj.hasStart())) {
            return false;
        } else {
            return this.endTime.isEqual(obj.getEndTime()) && this.name.equals(obj.getName()) &&
                    this.workSessions.equals(obj.getWorkSessions()) && this.ID == obj.getID() &&
                    this.hoursNeeded.equals(obj.getHoursNeeded()) &&
                    this.sessionLength.equals(obj.getSessionLength());
        }
    }

    /**
     * returns LocalDateTime form of what is considered the start time of the Event - can be after end time
     *
     * @return LocalDateTime startTime
     */
    public LocalDateTime getStartTime() {
        if (this.hasStart()) {
            return this.startTime;
        } else {
            return null;
        }

    }

    /**
     * returns LocalDateTime form of what is considered the end time of the Event - can be after end time
     *
     * @return LocalDateTime endTime
     */
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    /**
     * gets the name of the Event - String can be any format, informative as to nature of Event, usually set by user
     *
     * @return String name
     */
    public String getName() {
        return this.name;
    }

    /**
     * gets ID of the Event as set in construction - useful as unique reference, independent to name and description
     *
     * @return int ID
     */
    public int getID() {
        return this.ID;
    }

    /**
     * changes the start time to input LocalDateTime startTime
     *
     * @param startTime the start time
     */
    public void setStartTime(LocalDateTime startTime) {
        if (startTime.isAfter(this.getEndTime())) {
            this.startTime = this.getEndTime();
            this.endTime = startTime;
        } else {
            this.startTime = startTime;
        }
    }

    /**
     * changes the end time to input LocalDateTime endTime
     *
     * @param endTime the end time
     */
    public void setEndTime(LocalDateTime endTime) {
        if (endTime.isBefore(this.getStartTime())) {
            this.endTime = this.getStartTime();
            this.startTime = endTime;
        } else {
            this.endTime = endTime;
        }
    }

    /**
     * returns LocalDate for the day
     *
     * @return a LocalDate object for the day of the start of the event
     */
    public LocalDate getDay() {
        return this.getEndTime().toLocalDate();
    }

    /**
     * takes the start time and end time and computes the distance between them using Duration.between
     *
     * @return the length of the event in hours (as a float)
     */
    public double getLength() {
        if (this.hasStart()) {
            Duration duration = Duration.between(this.startTime, this.endTime);
            double whole_hours = duration.toHours();
            double partHour = duration.toMinutes();
            partHour = partHour / 60 - whole_hours;
            return whole_hours + partHour;
        } else {
            return 0;
        }
    }

    /**
     * determines whether this event conflicts with another event - if one or both has no duration, there is
     * no conflict and will return false
     *
     * @param other an event to compare to
     * @return true if their times overlap, false if they don't
     */
    public boolean conflicts(Event other) {
        if (other.hasStart() && this.hasStart())
            return (this.startTime.isBefore(other.getStartTime()) && this.endTime.isAfter(other.getStartTime())) ||
                    (this.startTime.isEqual(other.getStartTime())) ||
                    (this.startTime.isAfter(other.getStartTime()) && this.startTime.isBefore(other.getEndTime()));
        else {
            return false;
        }
    }

    /**
     * by default hoursNeeded is 0L for an event
     *
     * @return hours needed (long)
     */
    public Long getHoursNeeded() {
        return this.hoursNeeded;
    }

    /**
     * by default session length is 1L for an event
     *
     * @return session length (long)
     */
    public Long getSessionLength() {
        return this.sessionLength;
    }

    /**
     * a list of the work sessions contained in this event - no restrictions
     *
     * @return workSessions as a list of events
     */
    public List<Event> getWorkSessions() {
        return this.workSessions;
    }

    /**
     * sets a new value for hours needed - default is 0L, no restrictions - for information on preferences only
     *
     * @param hoursNeeded Long - a new value for hoursNeeded
     */
    public void setHoursNeeded(Long hoursNeeded) {
        this.hoursNeeded = hoursNeeded;
    }

    /**
     * sets a new value for session length - default is 1L, no restrictions - for information on default session
     * length only, does not limit length of work sessions
     *
     * @param sessionLength <code>Long</code> - new value for <code>sessionLength</code>
     */
    public void setSessionLength(Long sessionLength) {
        this.sessionLength = sessionLength;
    }

    /**
     * returns all work sessions contained in this <code>Event</code> whose end time is before the current
     * <code>LocalDateTime</code>
     *
     * @return <code>List<Event></code> - unordered sublist of <code>workSessions</code>
     */
    public List<Event> pastWorkSessions() {
        List<Event> past = new ArrayList<>();
        for (Event event : this.getWorkSessions()) {
            if (event.getEndTime().isBefore(LocalDateTime.now())) {
                past.add(event);
            }
        }
        return past;
    }

    /**
     * returns all work sessions contained in this <code>Event</code> whose start time is after the current
     * <code>LocalDateTime</code>
     *
     * @return <code>List<Event></code> - unordered sublist of <code>workSessions</code>
     */
    public List<Event> futureWorkSessions() {
        List<Event> future = new ArrayList<>();
        for (Event event : this.getWorkSessions()) {
            if (event.getEndTime().isAfter(LocalDateTime.now())) {
                future.add(event);
            }
        }
        return future;
    }

    /**
     * if event is contained in <code>this.workSessions</code>, it is removed from
     * <code>this.workSessions</code> and <code>this.hoursNeeded</code> is decreased by the length of the event
     *
     * @param event <code>Event</code> object that may be a work session in this <code>Event</code>
     */
    public void completeSession(Event event) {
        if (this.getWorkSessions().contains(event)) {
            this.getWorkSessions().remove(event);
            this.hoursNeeded -= (long) (event.getLength());
        }
    }

    /**
     * sets <code>this.workSessions</code> to new <code>workSessions</code>
     *
     * @param workSessions new <code>List<Event></code> to be set as workSessions
     */
    public void setWorkSessions(List<Event> workSessions) {
        this.workSessions = workSessions;
    }

    /**
     * Creates and adds a work session to <code>this.workSessions</code>. work sessions must have duration
     * (start and end time).
     *
     * @param start LocalDateTime start time
     * @param end   LocalDateTime end time
     */
    public void addWorkSession(LocalDateTime start, LocalDateTime end) {
        this.workSessions.add(new Event(this.getID(), this.getName() + " session", start, end));
    }

    /**
     * @return String in the form:
     * <code>
     * #ID
     * name: name
     * start date: YYYY-MM-DD / null
     * start time: HH:MM / null
     * end date: YYYY-MM-DD
     * end time: HH:MM
     * description: description / null
     * </code>
     */
    @Override
    public String toString() {
        if (this.hasStart()) {
            return this.getID() + "\nname: " + this.getName() + "\nstart date: " + this.getStartTime().toLocalDate().toString()
                    + "\nstart time: " + this.getStartTime().toLocalTime().toString() + "\nend date: " +
                    this.getEndTime().toLocalDate().toString() + "\nend time: " + this.getEndTime().toLocalTime().toString()
                    + "\ndescription: " + this.getDescription();
        } else {
            return this.getID() + "\nname: " + this.getName() + "\nstart date: " + null
                    + "\nstart time: " + null + "\nend date: " +
                    this.getEndTime().toLocalDate().toString() + "\nend time: " + this.getEndTime().toLocalTime().toString()
                    + "\ndescription: " + this.getDescription();
        }

    }

}
