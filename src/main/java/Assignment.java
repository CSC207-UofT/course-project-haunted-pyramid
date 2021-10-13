import java.time.LocalDateTime;
import java.util.ArrayList;

public class Assignment extends Event implements Fluid{
    //TODO properly implement these - change constructor so due date different - override conflict
    /**
     * constructor sets the ID, name, start and end times of the Event
     *
     * @param ID        the id for this event and events related to it (repetition etc)
     * @param name      the name of the event
     * @param startTime start time of the event
     * @param endTime   end time of the event
     */
    public Assignment(int ID, String name, LocalDateTime startTime, LocalDateTime endTime) {
        super(ID, name, startTime, endTime);
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

    @Override
    public ArrayList<AutoSchedule> getFluidSessions() {
        return null;
    }
}
