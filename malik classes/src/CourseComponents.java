import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CourseComponents extends Event{

    private String type;
    // type refers to whether it is a lecture, tutorial or lab (or a variation of those).
    private HashMap<Event, Date> tasks;

    public CourseComponents(String name, String location, Date startTime, Date endTime, String type) {
        super(name, Optional.ofNullable(location), startTime, endTime);
        this.type = type;
    }

    public void setTasks(Event event) {
        this.tasks = new HashMap<>();
        this.tasks.put(event, event.getEndTime());
    }

    public HashMap<Event, Date> getTasks() {
        return this.tasks;
    }

    public String getType() {
        return type;
    }


}

