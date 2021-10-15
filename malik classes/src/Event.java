import java.util.Date;
import java.util.Optional;

public abstract class Event {

    private String name;
    private Optional<String> location;
    private Date startTime;
    private Date endTime;
    private String additionalInfo;


    public Event(String name, Optional<String> location, Date startTime, Date endTime) {
        this.name = name;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.additionalInfo = "";
    }

    public String getName(){return this.name;}

    public Optional<String> getLocation(){return this.location;}

    public Date getStartTime(){return this.startTime;}

    public Date getEndTime(){return this.endTime;}

    public void setAdditionalInfo(String info){this.additionalInfo = info;}

    public void setName(String name){this.name = name;}

    public void setLocation(Optional<String> location){this.location = location;}

    public void setStartTime(Date startTime){this.startTime = startTime;}

    public void setEndTime(Date endTime){this.endTime = endTime;}


    @Override
    public String toString() {
        if (this.location.isPresent()) {
            return this.name + " is at " + this.location + " from " +
                    this.startTime + " to " + this.endTime + ". Remenber: " + this.additionalInfo;
        }
        else{
            return this.name + " is from " +
                    this.startTime + " to " + this.endTime + ". Remenber: " + this.additionalInfo;
        }
    }


}

