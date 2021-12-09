package usecases.events.worksessions;

import entities.Event;
import entities.UserPreferences;
import org.junit.Before;
import org.junit.Test;
import usecases.events.EventManager;
import usecases.events.worksessions.strategies.TimeGetters.DefaultTimeGetter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

public class WorkSessionSchedulerTest {
    EventManager eventManager = new EventManager(new ArrayList<>());
    WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
    UserPreferences userPreferences = new UserPreferences();
    DefaultTimeGetter defaultTimeGetter = new DefaultTimeGetter(userPreferences.getFreeTime());
    UUID deadline;
    static LocalDateTime end1 = LocalDateTime.of(LocalDate.now().plusDays(20), LocalTime.of(9, 11));
    static LocalDateTime end2 = LocalDateTime.of(LocalDate.now().plusDays(21), LocalTime.of(20, 21));


    @Before
    public void setup(){
        eventManager.addEvent("event 1", end1);
        eventManager.addEvent("event 2", end2);
        for(Event event: eventManager.getDefaultEventInfoGetter().getAllEvents()){
            event.setStartTime(event.getEndTime().plusHours(3));
            deadline = event.getID();
        }
        WorkSessionSchedulerBuilder workSessionSchedulerBuilder = new WorkSessionSchedulerBuilder();
        workSessionSchedulerBuilder.getWorkSessionScheduler(userPreferences).setHoursNeeded(deadline, 5L,
                eventManager);
    }

    @Test
    public void testSetHoursNeeded(){
        assert(workSessionManager.getWorkSessions(deadline).size() == 5);
    }

    @Test
    public void testSetSessionLength(){
        WorkSessionSchedulerBuilder workSessionSchedulerBuilder = new WorkSessionSchedulerBuilder();
        workSessionSchedulerBuilder.getWorkSessionScheduler(userPreferences).setSessionLength(deadline, 3L,
                eventManager);
        assert(workSessionManager.getWorkSessions(deadline).size() == 2);
    }

    @Test
    public void testChangeStartWorking(){
        WorkSessionSchedulerBuilder workSessionSchedulerBuilder = new WorkSessionSchedulerBuilder();
        workSessionSchedulerBuilder.getWorkSessionScheduler(userPreferences).changeStartWorking(deadline, LocalDate.now().
                plusDays(18), eventManager);
        assert(workSessionManager.getWorkSessions(deadline).size() == 4);
    }
}
