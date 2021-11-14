package usecases;

import entities.Event;
import interfaces.EventListObserver;
import usecases.events.EventManager;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.LocalTime;
import java.util.*;


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
        this.autoSchedule(deadline, eventManager);
    }

    public void setHoursNeeded(Event deadline, Long hoursNeeded, EventManager eventManager){
        deadline.setHoursNeeded(hoursNeeded);
        this.autoSchedule(deadline, eventManager);
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
        long totalNeeded = (long) (deadline.getHoursNeeded() - eventManager.totalHours(deadline.pastWorkSessions()));

        if (!this.procrastinate) {

            while (totalNeeded > 0) {

                // returns a Map of events incl. work sessions for the dates inputted
                Map<LocalDate, List<Event>> tempSchedule = eventManager.getRange(LocalDate.now(),
                        deadline.getEndTime().toLocalDate());

                tempSchedule.remove(deadline.getEndTime().toLocalDate());


                for (LocalDate day : tempSchedule.keySet()) {

                    LocalDateTime bestSlot = day.atTime(9, 0);

                    if (tempSchedule.get(day).size() == 0){

                        deadline.addWorkSession(bestSlot, bestSlot.plusHours(deadline.getSessionLength()));
                        totalNeeded = totalNeeded - deadline.getSessionLength();
                    }

                    else{

                        // Get freeSlots for that day
                        Map<LocalDateTime, Long> freeSlots = eventManager.freeSlots(day.atTime(9, 0),
                                tempSchedule.get(day),
                                day.atTime(23, 59));

                        for (LocalDateTime slot: freeSlots.keySet()){
                            if (freeSlots.get(slot) >= deadline.getSessionLength()){
                                deadline.addWorkSession(slot, slot.plusHours(deadline.getSessionLength()));
                                totalNeeded = totalNeeded - deadline.getSessionLength();
                                break;
                            }
                        }

                    }
                }
            }
        } else{

            // Divide the total hours needed by 3 to get time for each work session
            Long sessionLength = totalNeeded / 3L;

            // Get the three days before the enddate of the deadline event
            LocalDateTime firstDay = deadline.getEndTime().minusDays(3);
            LocalDateTime secondDay = deadline.getEndTime().minusDays(2);
            LocalDateTime thirdDay = deadline.getEndTime().minusDays(1);

            Map<LocalDate, List<Event>> tempSchedule = eventManager.getRange(firstDay.toLocalDate(),
                    thirdDay.toLocalDate());

            ArrayList<LocalDateTime> daysList = new ArrayList<>(){
                {
                    add(firstDay);
                    add(secondDay);
                    add(thirdDay);

                }
            };

            for (LocalDateTime day: daysList){
                if (tempSchedule.get(day.toLocalDate()).size() == 0){
                    LocalDate infoDay = day.toLocalDate();
                    LocalDateTime startTimeForDay = infoDay.atTime(12, 0);
                    deadline.addWorkSession(startTimeForDay, startTimeForDay.plusHours(sessionLength));
                }
                else{
                    // freeSlots for each day
                    Map<LocalDateTime, Long> freeSlots = eventManager.freeSlots(day.toLocalDate().atTime(9,0),
                            tempSchedule.get(day.toLocalDate()), day.toLocalDate().atTime(23, 59));

                    for (LocalDateTime slot: freeSlots.keySet()){
                        if (freeSlots.get(slot) >= sessionLength){
                            deadline.addWorkSession(slot, slot.plusHours(sessionLength));
                            break;
                    }
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

    public static void main(String[] args){
        EventManager em = new EventManager();
        WorkSessionScheduler ws = new WorkSessionScheduler(new HashMap<>(), true);
        em.addEvent("hello", LocalDateTime.of(2021, 11, 19, 2,0));
        Event e = new Event(3, "this", LocalDateTime.of(2021,11,17,13,30),
                LocalDateTime.of(2021, 11, 17, 14, 30));
        em.addEvent(e);
        em.get(1).setHoursNeeded(10L);
        ws.autoSchedule(em.get(1), em);

        for (Event event: em.getAllEventsFlatSplit()){
            System.out.println(em.displayEvent(event));
            System.out.println(event.getLength());
            System.out.println(event.getWorkSessions().size());
            System.out.println(event.getWorkSessions().isEmpty());
        }
        System.out.println("---------------");
        System.out.println(em.get(1).getWorkSessions());
    }
}
