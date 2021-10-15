package entities;

import interfaces.AutoSchedule;
import java.time.LocalDateTime;
import entities.Event;
import interfaces.AutoSchedule;

public class Session extends Event implements AutoSchedule{
    /**
     * TODO implement these - override certain methods in Event (for if unscheduled)
     *
     */


    /**
     * constructor sets the ID, name, start and end times of the Event
     *
     * @param ID        the id for this event and events related to it (repetition etc)
     * @param name      the name of the event
     * @param startTime start time of the event
     * @param endTime   end time of the event
     */
    public Session(int ID, String name, LocalDateTime startTime, LocalDateTime endTime) {
        super(ID, name, startTime, endTime);
    }

    @Override
    public boolean scheduled() {
        return true;
    }

    @Override
    public void schedule(LocalDateTime startTime, LocalDateTime endTime) {

    }
}
