package entities;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Duration;

public class Event {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private final int ID;
    private String type;
    private final String name;
    private boolean recurring;
    private boolean inCategory;
    private String categoryType; // a category is a super class of course. For example, if the users wish to create
                                // a test, they can choose to add the test to a category, which can be for example a
                                // course. I made a Category class (not only a course class) for extensibility purposes.
    private String otherInformation;

    /**
     * constructor sets the ID, name, and end times of the entities.Event
     * @param ID the id for this event and events related to it (repetition etc)
     * @param name the name of the event
     * @param endTime end time of the event
     * @param recurring if the event repeats
     */
    public Event(int ID, String name, String type, LocalDateTime endTime, boolean recurring,
                 String categoryName, String otherInformation){
        this.ID = ID;
        this.name = name;
        this.type = type;
        this.endTime = endTime;
        this.recurring = recurring;
        this.categoryType = categoryName;
        this.inCategory = !categoryName.equals("");
        this.otherInformation = otherInformation;
    }

    /**
     * a constructor for single day events
     * @param ID the event id
     * @param name name of event
     * @param year year (integer)
     * @param month month (integer)
     * @param day day (integer)
     * @param endHour hour event ends (integer)
     * @param endMin minute event ends (integer)
     */
    public Event(int ID, String name, String type, int year, int month, int day, int endHour, int endMin,
                 boolean recurring, String categoryName, String otherInformation){
        this.name = name;
        this.ID = ID;
        this.type = type;
        this.endTime = LocalDateTime.of(year, month, day, endHour, endMin);
        this.recurring = recurring;
        this.categoryType = categoryName;
        this.inCategory = !categoryName.equals("");
        this.otherInformation = otherInformation;
    }

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
        if (this.startTime != null){
            return this.startTime.isEqual(obj.getStartTime()) && this.endTime.isEqual(obj.getEndTime()) &&
                    this.name.equals(obj.getName());
        }
        else{
            return this.endTime.isEqual(obj.getEndTime()) && this.name.equals(obj.getName());
        }
    }

    /**
     *
     * getter methods.
     */
    public LocalDateTime getStartTime(){return this.startTime;}
    public LocalDateTime getEndTime() {return this.endTime;}
    public int getID(){return this.ID;}
    public String getType() {return type;}
    public String getName(){return this.name;}
    public boolean isRecurring() {return recurring;}
    public boolean getInCategory(){return inCategory;}
    public String getCategoryType() {return categoryType;}
    public String getOtherInformation() {return otherInformation;}

    /**
     *
     * setter methods
     */
    public void setStartTime(LocalDateTime startTime) {this.startTime = startTime;}
    public void setEndTime(LocalDateTime endTime) {this.endTime = endTime;}
    public void setType(String type) {this.type = type;}
    public void setRecurring(boolean recurring) {this.recurring = recurring;}
    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
        this.inCategory = true;}
    public void setOtherInformation(String otherInformation) {this.otherInformation = otherInformation;}


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
     * @param other an event to compare to
     * @return true if their times overlap, false if they don't
     */
    public boolean conflicts(Event other){
        if (this.startTime != null && other.getStartTime() != null){
            return (this.startTime.isBefore(other.getStartTime()) && this.endTime.isAfter(other.getStartTime())) ||
                (this.startTime.isEqual(other.getStartTime())) ||
                (this.startTime.isAfter(other.getStartTime()) && this.startTime.isBefore(other.getEndTime()));
        }
        else {
            return false;
        }
    }

}


