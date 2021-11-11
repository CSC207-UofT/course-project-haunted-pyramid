package usecases;

import entities.Event;
import interfaces.EventListObserver;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A scheduler that schedules work sessions for events that have no start-time
 * @author Shahzada Muhammad Shameel Farooq
 * @author Taite Cullen
 * @version %I%, %G%
 *
 */
public class WorkSessionScheduler implements EventListObserver {
    //specified by saved user information - the time during which the user does not want to work
    private Map<LocalTime, LocalTime> freeTime;
    //preferences for how work sessions should be sorted
    private boolean procrastinate;
    private boolean notProcrastinate;


    /**
     * A constructor for WorkSessionScheduler
     * @param freeTime Time slots for which user does not want to work
     * @param procrastinate Option for user, allows for a unique way of scheduling work sessions
     * @param notProcrastinate Option for user, allows for a unique way of scheduling work sessions
     */
    public WorkSessionScheduler(Map<LocalTime, LocalTime> freeTime, boolean procrastinate, boolean notProcrastinate){
        this.freeTime = freeTime;
        this.procrastinate = procrastinate;
        this.notProcrastinate = notProcrastinate;
    }

    /**
     * A method for setting the length of the session
     * @param deadline An Event
     * @param sessionLength A Long which is the session length
     * @param eventManager An EventManager
     */
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

    /**
     * A Method which automatically schedules work sessions for an Event which only has a deadline assocaited with it
     * has two options or strategies for scheduling work sessions
     * @param deadline An Event
     * @param eventManager An EventManager
     */
    private void autoSchedule(Event deadline, EventManager eventManager){

        // past work sessions are now the work sessions associated with the deadline event
        //deadline.resetWorkSessions(deadline.pastWorkSessions());

        // Calculate hours which are required for the deadline event after previous
        // work sessions are taken into account
        Long totalNeeded = (long) (deadline.getHoursNeeded() - eventManager.totalHours(deadline.pastWorkSessions()));

        if (this.notProcrastinate) {

            while (totalNeeded > 0) {

                // returns a Map of events incl. work sessions for the dates inputted
                Map<LocalDate, List<Event>> tempSchedule = eventManager.getRange(LocalDate.now(),
                        deadline.getStartTime().toLocalDate());

                LocalDate mostFreeTime = LocalDate.now();
                LocalDateTime bestSlot = LocalDateTime.now();

                for (LocalDate day : tempSchedule.keySet()) {
                    if (eventManager.totalHours(tempSchedule.get(day)) <
                            eventManager.totalHours(tempSchedule.get(mostFreeTime))) {

                        Map<LocalDateTime, Long> freeSlots = eventManager.freeSlots(LocalDateTime.now(),
                                tempSchedule.get(day),
                                deadline.getStartTime());

                        if (this.maximum(freeSlots.values().toArray(new Long[0])) < deadline.getSessionLength()) {
                            mostFreeTime = day;
                            for (LocalDateTime slot : freeSlots.keySet()) {
                                if (deadline.getSessionLength() < freeSlots.get(slot) && freeSlots.get(slot) <
                                        freeSlots.get(bestSlot)) {
                                    bestSlot = slot;
                                }
                            }
                        }
                    }
                }
                if (totalNeeded > deadline.getSessionLength()) {
                    deadline.getWorkSessions().add(new Event(deadline.getID(), deadline.getName() + "session", bestSlot,
                            bestSlot.plusHours(deadline.getSessionLength())));
                    totalNeeded -= deadline.getSessionLength();
                } else {
                    deadline.getWorkSessions().add(new Event(deadline.getID(), deadline.getName() + "session", bestSlot,
                            bestSlot.plusHours(totalNeeded)));
                    totalNeeded = 0L;
                }
            }
        }

        else if (procrastinate){

            // Divide the total hours needed by 3 to get time for each work session
            Long sessionLength = totalNeeded / 3L;

            // Get the three days before the enddate of the deadline event
            LocalDateTime firstDay = deadline.getEndTime().minusDays(3);
            LocalDateTime secondDay = deadline.getEndTime().minusDays(2);
            LocalDateTime thirdDay = deadline.getEndTime().minusDays(1);

            Map<LocalDate, List<Event>> tempSchedule = eventManager.getRange(LocalDate.now(),
                    deadline.getStartTime().toLocalDate());

            ArrayList<LocalDateTime> daysList = new ArrayList<LocalDateTime>(){
                {
                    add(firstDay);
                    add(secondDay);
                    add(thirdDay);

                }
            };
            for (LocalDateTime day: daysList){

                // freeSlots for each day
                Map<LocalDateTime, Long> freeSlots = eventManager.freeSlots(day, tempSchedule.get(day), day);

                for (LocalDateTime slot: freeSlots.keySet()){
                    if (freeSlots.get(slot) >= sessionLength){
                        deadline.getWorkSessions().add(new Event(deadline.getID(), deadline.getName() + "session", slot,
                                slot.plusHours(sessionLength)));
                        break;
                    }
                }

            }
        }

    }

    /**
     *
     * @param durations Array of Long type values
     * @return the maximum Long type value from the array
     */
    private Long maximum(Long[] durations){
        Long max = 0L;
        for (Long dur: durations){
            if (dur > max){
                max = dur;
            }
        }
        return max;
    }

    /**
     *
     * @param event An event
     * @return A string which details the past sessions scheduled and the Future sessions scheduled
     */
    public String sessionsString(Event event){
        EventManager eventManager = new EventManager();
        return "Past sessions --------:\n " + this.sessionOptionsString(eventManager.timeOrder(event.pastWorkSessions())) + "\nFuture Sessions -------:\n " +
                this.sessionOptionsString(eventManager.timeOrder(event.futureWorkSessions()));
    }

    /**
     *
     * @param sessions List of Events
     * @return A string which gives the details of the session
     */
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

    /**
     * Method which marks complete a session for the Event
     * @param event An Event
     * @param session A String which is the session
     * @param eventManager An EventManager
     */
    public void markComplete(Event event, String session, EventManager eventManager){
        eventManager.timeOrder(event.getWorkSessions());
        event.setHoursNeeded((long) (event.getHoursNeeded() -
                event.getWorkSessions().get(Integer.parseInt(session.split(" ")[2])).getLength()));
        event.getWorkSessions().remove(event.getWorkSessions().get(Integer.parseInt(session.split(" ")[2])));
        this.autoSchedule(event, eventManager);
    }

    /**
     * Method which marks incomplete a session for the Event
     * @param event An Event
     * @param session A String with details of session
     * @param eventManager An EventManager
     */
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
