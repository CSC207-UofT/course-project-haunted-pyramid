import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Duration;

public class Event{
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int ID;
    private String name;
    //private Course course; TODO courses are a thing

    /**
     * constructor sets the ID, name, start and end times of the Event
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
     * @return LocalDateTime endTime
     */
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

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
    public float getLength(){
        Duration duration = Duration.between(this.startTime, this.endTime);
        float whole_hours = duration.toHoursPart();
        float partHour =  duration.toMinutesPart();
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

    public static void main(String[] Args){
        Event event = new Event(1, "My Event", LocalDateTime.of(2020, 1, 1, 1, 0,
                0), LocalDateTime.of(2020, 1, 1, 3, 30, 0));
        Event event2 = new Event(1, "My Event", LocalDateTime.of(2020, 1, 1, 1, 0,
                0), LocalDateTime.of(2020, 1, 1, 3, 30, 0));
        System.out.println(event.getLength());
        System.out.println(event.conflicts(event2));
    }

}
