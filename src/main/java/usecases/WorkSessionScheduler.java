package usecases;

import controllers.IOController;
import entities.Event;
import interfaces.EventListObserver;
import usecases.events.EventManager;

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
    private final boolean procrastinate;


    /**
     * A constructor for WorkSessionScheduler
     * @param freeTime Time slots for which user does not want to work
     * @param procrastinate Option for user, allows for a unique way of scheduling work sessions
     */
    public WorkSessionScheduler(Map<LocalTime, LocalTime> freeTime, boolean procrastinate){
        this.freeTime = freeTime;
        this.procrastinate = procrastinate;
    }

    /**
     * A method for setting the length of the session
     * @param deadline An Event
     * @param sessionLength A Long which is the session length
     * @param eventManager An EventManager
     */
    public void setSessionLength(Event deadline, Long sessionLength, EventManager eventManager){
        deadline.setSessionLength(sessionLength);
        this.autoSchedule(deadline.getID(), eventManager);
    }

    public void setHoursNeeded(Event deadline, Long hoursNeeded, EventManager eventManager){
        deadline.setHoursNeeded(hoursNeeded);
        this.autoSchedule(deadline.getID(), eventManager);
    }

    /**
     * A Method which automatically schedules work sessions for an Event which only has a deadline assocaited with it
     * has two options or strategies for scheduling work sessions
     * @param deadline An Event
     * @param eventManager An EventManager
     */
    private void autoSchedule1(Event deadline, EventManager eventManager){

        // past work sessions are now the work sessions associated with the deadline event
        //deadline.resetWorkSessions(deadline.pastWorkSessions());

        // Calculate hours which are required for the deadline event after previous
        // work sessions are taken into account
        Long totalNeeded = (long) (deadline.getHoursNeeded() - eventManager.totalHours(deadline.pastWorkSessions()));

        if (!this.procrastinate) {

            while (totalNeeded > 0) {

                // returns a Map of events incl. work sessions for the dates inputted
                Map<LocalDate, List<Event>> tempSchedule = eventManager.getRange(LocalDate.now(),
                        deadline.getEndTime().toLocalDate());

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
                    deadline.addWorkSession(bestSlot,
                            bestSlot.plusHours(deadline.getSessionLength()));
                    totalNeeded -= deadline.getSessionLength();
                } else {
                    deadline.addWorkSession(bestSlot,
                            bestSlot.plusHours(totalNeeded));
                    totalNeeded = 0L;
                }
            }
        } else{

            // Divide the total hours needed by 3 to get time for each work session
            Long sessionLength = totalNeeded / 3L;

            // Get the three days before the enddate of the deadline event
            LocalDateTime firstDay = deadline.getEndTime().minusDays(3);
            LocalDateTime secondDay = deadline.getEndTime().minusDays(2);
            LocalDateTime thirdDay = deadline.getEndTime().minusDays(1);

            Map<LocalDate, List<Event>> tempSchedule = eventManager.getRange(LocalDate.now(),
                    deadline.getEndTime().toLocalDate());

            ArrayList<LocalDateTime> daysList = new ArrayList<LocalDateTime>(){
                {
                    add(firstDay);
                    add(secondDay);
                    add(thirdDay);

                }
            };
            for (LocalDateTime day: daysList){
                // freeSlots for each day
                Map<LocalDateTime, Long> freeSlots = eventManager.freeSlots(day, tempSchedule.get(day.toLocalDate()), day);

                for (LocalDateTime slot: freeSlots.keySet()){
                    if (freeSlots.get(slot) >= sessionLength){
                        deadline.addWorkSession(slot, slot.plusHours(sessionLength));
                        break;
                    }
                }

            }
        }

    }

    private Map<LocalDateTime, Long> removeSmallSlots(Map<LocalDateTime, Long> allFreeSlots, long sessionLength){
        for (LocalDateTime time: allFreeSlots.keySet()){
            if (allFreeSlots.get(time) < sessionLength){
                allFreeSlots.remove(time);
            }
        }
        return allFreeSlots;
    }

    private Map<LocalDate, List<Event>> removeFullDays(LocalDateTime start, LocalDateTime end, Map<LocalDate, List<Event>> schedule, long sessionLength){
        EventManager em = new EventManager();
        for (LocalDate day: schedule.keySet()){
            if (day.isEqual(start.toLocalDate())){
                if (this.removeSmallSlots(em.freeSlots(start, schedule.get(day), LocalDateTime.of(day, LocalTime.of(23, 59))), sessionLength).isEmpty()){
                    schedule.remove(day);
                }
            } else if (day.isEqual(end.toLocalDate())){
                if (this.removeSmallSlots(em.freeSlots(LocalDateTime.of(day, LocalTime.of(0, 0)), schedule.get(day), end), sessionLength).isEmpty()){
                    schedule.remove(day);
                }
            } else if (day.isAfter(start.toLocalDate()) && day.isBefore(end.toLocalDate())) {
                if (this.removeSmallSlots(em.freeSlots(LocalDateTime.of(day, LocalTime.of(0, 0)), schedule.get(day), LocalDateTime.of(day, LocalTime.of(23, 59))), sessionLength).isEmpty()) {
                    schedule.remove(day);
                }
            }
        }
        return schedule;
    }

    private LocalDate getEmptiestDay(Map<LocalDate, List<Event>> schedule){
        EventManager eventManager = new EventManager();
        LocalDate emptiest = schedule.keySet().toArray(new LocalDate[0])[0];
        for (LocalDate day: schedule.keySet()){
            if (eventManager.totalHours(schedule.get(day)) < eventManager.totalHours(schedule.get(emptiest))){
                emptiest = day;
            }
        }
        return emptiest;
    }

    private LocalDateTime getSmallestSlot(Map<LocalDateTime, Long> freeSlots){
        LocalDateTime smallestSlot = freeSlots.keySet().toArray(new LocalDateTime[0])[0];
        for (LocalDateTime slot: freeSlots.keySet()){
            if (freeSlots.get(slot) < freeSlots.get(smallestSlot)){
                smallestSlot = slot;
            }
        }
        return smallestSlot;
    }

    private void autoSchedule(Integer ID, EventManager eventManager){
        Event deadline = eventManager.get(ID);
        deadline.setWorkSessions(deadline.pastWorkSessions());
        long totalHours = deadline.getHoursNeeded() - (long) eventManager.totalHours(deadline.getWorkSessions());
        while (totalHours > 0){
            long sessionLength = deadline.getSessionLength();
            if (totalHours < deadline.getSessionLength()){
                sessionLength = totalHours;
            }
            Map<LocalDate, List<Event>> schedule = eventManager.getRange(LocalDate.now(), deadline.getEndTime().toLocalDate());
            System.out.println(schedule.values());
            this.removeFullDays(LocalDateTime.now(), deadline.getEndTime(), schedule, deadline.getSessionLength());
            LocalDate day = this.getEmptiestDay(schedule);
            Map<LocalDateTime, Long> freeSlots = eventManager.freeSlots(LocalDateTime.of(day, LocalTime.of(0, 0)), schedule.get(day),
                    LocalDateTime.of(day, LocalTime.of(23, 59)));

            this.removeSmallSlots(freeSlots, sessionLength);
            LocalDateTime bestTime = this.getSmallestSlot(freeSlots);

            deadline.addWorkSession(bestTime, bestTime.plusHours(sessionLength));
            totalHours -= sessionLength;
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
        return "Past sessions --------:\n " + this.sessionOptionsString(eventManager.timeOrder(event.pastWorkSessions()))
                + "\nFuture Sessions -------:\n " +
                this.sessionOptionsString(eventManager.timeOrder(event.futureWorkSessions()));
    }

    /**
     *
     * @param sessions List of Events
     * @return A string which gives the details of the session
     */
    private String sessionOptionsString(List<Event> sessions){
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
        this.autoSchedule(event.getID(), eventManager);
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
        this.autoSchedule(event.getID(), eventManager);
    }


    @Override
    public void update(String addRemoveChange, ArrayList<Event>  changed, EventManager eventManager) {
        for (Event event : changed) {
            this.autoSchedule(event.getID(), eventManager);
        }
    }

    public static void main(String[] args){
        EventManager em = new EventManager();
        WorkSessionScheduler ws = new WorkSessionScheduler(new HashMap<>(), false);
        em.addEvent("hello", LocalDateTime.of(2021, 11, 15, 2,0));
        ws.setHoursNeeded(em.get(1), 3L, em);

        System.out.println(em.freeSlots(LocalDateTime.now(), em.getAllEvents(), em.get(1).getEndTime()));

        System.out.println(em.get(1).getWorkSessions());
        IOController.getAnswer("what");
        System.out.println(ws.sessionsString(em.get(1)));
    }
}
