package usecases;

import entities.Event;
import interfaces.EventListObserver;

import java.sql.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkSessionScheduler implements EventListObserver {
    //specified by saved user information - the time during which the user does not want to work
    private Map<LocalTime, LocalTime> freeTime;
    //preferences for how work sessions should be sorted
    private boolean procrastinate;

    public WorkSessionScheduler(Map<LocalTime, LocalTime> freeTime, boolean procrastinate){
        this.freeTime = freeTime;
        this.procrastinate = procrastinate;
    }

    public void setSessionLength(Event deadline, Long sessionLength, EventManager eventManager){
        deadline.setSessionLength(sessionLength);
        this.autoSchedule(deadline, eventManager);
    }

    public void setHoursNeeded(Event deadline, Long hoursNeeded, EventManager eventManager){
        deadline.setHoursNeeded(hoursNeeded);
        this.autoSchedule(deadline, eventManager);
    }

    private Map<LocalDate, List<Event>> hasEligibleSlot(Map<LocalDate, List<Event>> dayEvents,
                                                              Long sessionLength){
        //TODO returns the days in which this event would fit
        return new HashMap<LocalDate, List<Event>>();

    }

    private List<LocalDate> mostFreeTimeSort(Map<LocalDate, List<Event>> dayEvents){
        //TODO returns the days in order of most free time to least free time
       return new ArrayList<>();
    }

    private Map<List<LocalDateTime>, Long> eligibleSlots(List<Event> day, Long sessionLength){
        //TODO returns only the eligible freeslots in a segment
        return new HashMap<>();
    }

    private List<LocalDateTime> smallestSlotSort(Map<LocalDateTime, Long> freeSlots){
        return new ArrayList<>();
    }

    private List<LocalDate> fewestWorkSessionsAlready(Map<LocalDateTime, List<Event>> schedule){
        return new ArrayList<>();
    }

    private void autoSchedule(Event deadline, EventManager eventManager){
        //TODO this should take an Event that possibly already has some work sessions associated with it,
        // and all the sessions associated with it based on the eventManager, its session length, its total hours,
        // and its past work sessions. Its past work sessions should not be rescheduled. could also use strategies
        // to fulfill user specifications, a few of which are suggested in the attributes at the top of this class
        // helpful methods:
        // deadline.setWorkSessions(List<Event> sessions)
        // deadline.futureWorkSessions()
        // deadline.pastWorkSessions()
        // deadline.getWorkSessions().add(Event session)
        // deadline.getHoursNeeded()
        // -> long , hours
        // deadline.getSessionLength()
        // -> long, hours
        // eventManager.getRange(LocalDate start, LocalDate end)
        // -> returns a Map<LocalDate (Day), List<Event> (all events - including work sessions - in this day)> from start to end dates
        // eventManager.getFreeSlots(List<Event> schedule)
        // -> returns a time ordered Map <LocalDateTime (start of free slot), Long (duration of free slot in seconds)>

    }

    private Long maximum(Long[] durations){
        Long max = 0L;
        for (Long dur: durations){
            if (dur > max){
                max = dur;
            }
        }
        return max;
    }

    public String sessionsString(Event event){
        EventManager eventManager = new EventManager();
        return "Past sessions --------:\n " + this.sessionOptionsString(eventManager.timeOrder(event.pastWorkSessions())) + "\nFuture Sessions -------:\n " +
                this.sessionOptionsString(eventManager.timeOrder(event.futureWorkSessions()));
    }

    private String sessionOptionsString(List<Event> sessions){
        EventManager eventManager = new EventManager();
        StringBuilder options = new StringBuilder();
        int i=0;
        for (Event session: sessions) {
            options.append("session ").append(i).append("-------\n").append(session.toString()).append("\n");
            i += 1;
        }
        return options.toString();
    }

    public void markComplete(Event event, String session, EventManager eventManager){
        eventManager.timeOrder(event.getWorkSessions());
        event.setHoursNeeded((long) (event.getHoursNeeded() -
                event.getWorkSessions().get(Integer.parseInt(session.split(" ")[2])).getLength()));
        event.getWorkSessions().remove(event.getWorkSessions().get(Integer.parseInt(session.split(" ")[2])));
        this.autoSchedule(event, eventManager);
    }
    public void markInComplete(Event event, String session, EventManager eventManager){
        eventManager.timeOrder(event.getWorkSessions());
        event.getWorkSessions().remove(event.getWorkSessions().get(Integer.parseInt(session.split(" ")[2])));
        this.autoSchedule(event, eventManager);
    }


    @Override
    public void update(String addRemoveChange, ArrayList<Event>  changed, EventManager eventManager) {
        for (Event event : changed) {
            this.autoSchedule(event, eventManager);
        }
    }
}
