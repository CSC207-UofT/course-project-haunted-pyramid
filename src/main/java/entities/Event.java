package entities;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.Serializable;
import java.sql.Array;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {
    private LocalDateTime startTime;
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
     * constructor sets the ID, name, start and end times of the entities.Event
     * @param ID the id for this event and events related to it (repetition etc)
     * @param name the name of the event
     * @param startTime start time of the event
     * @param endTime end time of the event
     */
    public Event(int ID, String name, LocalDateTime startTime, LocalDateTime endTime) {
        this.ID = ID;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hoursNeeded = 0L;
        this.sessionLength = 1L;
        this.workSessions = new ArrayList<>();
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
    public Event(int ID, String name, int year, int month, int day, int startHour, int endHour, int startMin,
                 int endMin){
        this.name = name;
        this.ID = ID;
        this.startTime = LocalDateTime.of(year, month, day, startHour, startMin , 0);
        this.endTime = LocalDateTime.of(year, month, day, endHour, endMin , 0);
        this.hoursNeeded = 0L;
        this.sessionLength = 1L;
        this.workSessions = new ArrayList<>();
    }

    public void setDescription(String description){
        this.description = description;
    }
    public void setRecursiveId(int recursiveId) {this.recursiveId = recursiveId;}
    public void setName(String name){
        this.name = name;
    }
    public void setType(String type) {this.type = type;}

    public String getDescription(){
        return description;
    }
    public String getType() {return type;}
    public String getCategoryName() {return categoryName;}

    /**
     *
     * @param object the object event is being compared to
     * @return whether it is equal to this object
     */
    @Override
    public boolean equals(Object object){
        if (object == null){
            return false;
        }
        if (object.getClass() != this.getClass()){
            return false;
        }
        Event obj = (Event) object;
        return this.startTime.isEqual(obj.getStartTime()) && this.endTime.isEqual(obj.getEndTime()) &&
                this.name.equals(obj.getName());
    }

    /**
     *
     * @return LocalDateTime startTime
     */
    public LocalDateTime getStartTime(){
        return this.startTime;
    }

    /**
     *
     * @return String of start date in form YYYY-MM-DD TT:TT
     */
    public String getStartString(){return this.startTime.toLocalDate().toString() + " " +
            this.startTime.toLocalTime().toString();}

    /**
     *
     * @return the hour of the start time in 100's plus the minutes
     */
    public double startTimeDouble()
    {return this.startTime.getHour()*100 + ((float)this.startTime.getMinute() * 100/60) ;}
    /**
     *
     * @return LocalDateTime endTime
     */
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    /**
     *
     * @return String of end date in form YYYY-MM-DD TT:TT
     */
    public String getEndString(){return this.endTime.toLocalDate().toString() + " " +
            this.endTime.toLocalTime().toString();}

    /**
     *
     * @return the hour of the end time in 100's plus the minutes
     */
    public double endTimeDouble()
    {return this.endTime.getHour()*100 + ((float)this.endTime.getMinute() * 100/60);}
    /**
     *
     * @return int name
     */
    public String getName(){
        return this.name;
    }

    /**
     *
     * @return ID
     */
    public int getID(){
        return this.ID;
    }

    /**
     * change the start time
     * @param startTime the start time
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * change the end time
     * @param endTime the end time
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     *
     * @return a LocalDate object for the day of the start of the event
     */
    public LocalDate getDay(){
        return LocalDate.of(this.startTime.getYear(), this.startTime.getMonthValue(), this.startTime.getDayOfMonth());
    }

    /**
     *
     * @return the length of the event in hours (as a float)
     */
    public double getLength(){
        Duration duration = Duration.between(this.startTime, this.endTime);
        double whole_hours = duration.toHoursPart();
        double partHour =  duration.toMinutesPart();
        partHour = partHour/60;
        return whole_hours + partHour;
    }

    /**
     * determines whether this event conflicts with another event - not useful for due dates, only
     * events with length
     * TODO change implementation of conflicts for events that are due dates
     * @param other an event to compare to
     * @return true if their times overlap, false if they don't
     */
    public boolean conflicts(Event other){
        return (this.startTime.isBefore(other.getStartTime()) && this.endTime.isAfter(other.getStartTime())) ||
                (this.startTime.isEqual(other.getStartTime())) ||
                (this.startTime.isAfter(other.getStartTime()) && this.startTime.isBefore(other.getEndTime()));
    }

    public Long getHoursNeeded() {
        return this.hoursNeeded;
    }
    public Long getSessionLength(){
        return this.sessionLength;
    }
    public List<Event> getWorkSessions(){
        return this.workSessions;
    }

    public void setHoursNeeded(Long hoursNeeded){
        this.hoursNeeded = hoursNeeded;
    }

    public void setSessionLength(Long sessionLength){
        this.sessionLength = sessionLength;
    }

    public List<Event> pastWorkSessions(){
        List<Event> past = new ArrayList<>();
        for (Event event: this.getWorkSessions()){
            if (event.getEndTime().isBefore(LocalDateTime.now())){
                past.add(event);
            }
        }
        return past;
    }

    public List<Event> futureWorkSessions(){
        List<Event> future = new ArrayList<>();
        for (Event event: this.getWorkSessions()){
            if (event.getStartTime().isAfter(LocalDateTime.now())){
                future.add(event);
            }
        }
        return future;
    }

    public void completeSession(Event event) {
        if (this.getWorkSessions().contains(event)){
            this.getWorkSessions().remove(event);
            this.hoursNeeded -= (long) (event.getLength());
        }
    }

    public void resetWorkSessions(List<Event> toKeep){
        this.workSessions = toKeep;
    }

    public void addWorkSession(LocalDateTime start, LocalDateTime end){
        this.workSessions.add(new Event(this.getID(), this.getName(), start, end));
    }

    @Override
    public String toString(){
        return this.getID() + "\nname: " + this.getName() + "\nstart: " +
                this.getStartString() + "\nend: " + this.getEndString() + "\ndescription: " + this.getDescription();
    }

}
