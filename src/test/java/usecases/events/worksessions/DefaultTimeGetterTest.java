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

public class DefaultTimeGetterTest {
    EventManager eventManager;
    UserPreferences userPreferences = new UserPreferences();
    DefaultTimeGetter defaultTimeGetter = new DefaultTimeGetter(userPreferences.getFreeTime());
    UUID deadline;
    static LocalDateTime end1 = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(9, 11));
    static LocalDateTime end2 = LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(20, 21));


    @Before
    public void setup(){
        eventManager = new EventManager(new ArrayList<>());
        eventManager.addEvent("event 1", end1);
        eventManager.addEvent("event 2", end2);
        for(Event event: eventManager.getDefaultEventInfoGetter().getAllEvents()){
            event.setStartTime(event.getEndTime().plusHours(3));
            deadline = event.getID();
        }
    }

    @Test
    public void testGetTimes() {
        assert(defaultTimeGetter.getTimes(deadline, eventManager, 3L).size() == 2);
    }

    @Test
    public void getListSchedule(){
        assert(defaultTimeGetter.getListSchedule(eventManager, LocalDate.now(), deadline).size() == 9);
    }
}
