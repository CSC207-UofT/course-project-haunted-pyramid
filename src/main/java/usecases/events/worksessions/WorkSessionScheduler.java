package usecases.events.worksessions;

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
        LocalDateTime deadlineTime = eventManager.getEnd(deadline);
        Integer ID = eventManager.getID(deadline);
        eventManager.addEvent(deadline);
        deadline.setWorkSessions(deadline.pastWorkSessions());
        Long hoursToSchedule = (long) (eventManager.getTotalHoursNeeded(ID) -
                eventManager.totalHours(eventManager.getPastWorkSession(ID)));
        while (!(hoursToSchedule == 0)){

            Long length = eventManager.getEventSessionLength(ID);
            if (hoursToSchedule < eventManager.getEventSessionLength(ID)){
                length = hoursToSchedule;
            }
            hoursToSchedule -= length;

            Map<LocalDate, List<Event>> schedule = eventManager.getRange(LocalDate.now(), deadlineTime.toLocalDate());
            for (LocalDate day: schedule.keySet()){
                for (LocalTime start: this.freeTime.keySet()){
                    schedule.get(day).add(new Event(0, "free time", LocalDateTime.of(day, start),
                            LocalDateTime.of(day, this.freeTime.get(start))));
                }
            }

            List<LocalDate> days = (this.eligibleDays(schedule, length, deadlineTime));

            days = this.leastWorkSessionsOrder(days, deadline.getWorkSessions());

            System.out.println(days);

            Map<LocalDateTime, Long> freeSlots;
            if (days.get(0).isEqual(LocalDate.now())) {
                freeSlots = eventManager.freeSlots(LocalDateTime.now(), schedule.get(days.get(0)),
                        LocalDateTime.of(days.get(0),LocalTime.of(23, 59)));
            } else if (days.get(0).isEqual(deadlineTime.toLocalDate())) {
                LocalDateTime start = LocalDateTime.of(days.get(0),LocalTime.of(0, 0));
                freeSlots = eventManager.freeSlots(start, schedule.get(days.get(0)), deadlineTime);
            } else {
                freeSlots = eventManager.freeSlots(LocalDateTime.of(days.get(0),LocalTime.of(0, 0)),
                        schedule.get(days.get(0)), LocalDateTime.of(days.get(0),LocalTime.of(23, 59)));
            }
            System.out.println(freeSlots);

            List<LocalDateTime> times = this.removeIneligibleTimes(freeSlots, length);
            times = this.smallestSlotOrder(times, freeSlots);

            deadline.addWorkSession(times.get(0), times.get(0).plusHours(length));
        }
        System.out.println("------");

    }

    private List<LocalDate> eligibleDays(Map<LocalDate, List<Event>> schedule, Long length, LocalDateTime deadline){
        EventManager em = new EventManager();
        List<LocalDate> eligible = new ArrayList<>();
        for (LocalDate day: schedule.keySet()){
            LocalTime startTime = LocalTime.of(0, 0);
            LocalTime endTime = LocalTime.of(23, 59);
            if (LocalDate.now().isEqual(day)){
                startTime = LocalTime.now();
            }
            if (deadline.toLocalDate().isEqual(day)){
                endTime = deadline.toLocalTime();
            }

            LocalDateTime start = LocalDateTime.of(day, startTime);
            LocalDateTime end = LocalDateTime.of(day, endTime);
            if (!this.removeIneligibleTimes(em.freeSlots(start, schedule.get(day), end), length).isEmpty()){
                eligible.add(day);
            }
        }
        return eligible;
    }

    private List<LocalDateTime> removeIneligibleTimes(Map<LocalDateTime, Long> slots, Long length){
        List<LocalDateTime> eligible = new ArrayList<>();
        for (LocalDateTime time: slots.keySet()){
            if (slots.get(time) >= length){
                eligible.add(time);
            }else{
                slots.remove(time);
            }
        }
        return eligible;
    }

    private List<LocalDate> leastWorkSessionsOrder(List<LocalDate> days, List<Event> workSessions){
        List<LocalDate> ordered = new ArrayList<>();

        while (!days.isEmpty()){
            LocalDate leastWorkSessions = this.leastWorkSessions(days, workSessions);
            days.remove(leastWorkSessions);
            ordered.add(leastWorkSessions);
        }
        return ordered;
    }

    private LocalDate leastWorkSessions(List<LocalDate> days, List<Event> workSessions){
        EventManager eventManager = new EventManager();
        LocalDate leastWorkSessions = days.get(0);
        int record = workSessions.size();
        for (LocalDate day: days){
            int num = 0;
            for (Event session: workSessions){
                if (eventManager.getEnd(session).toLocalDate().isEqual(day)){
                    num = num + 1;
                }
            }
            if (num <= record){
                leastWorkSessions = day;
                record = num;
            }
        }

        return leastWorkSessions;
    }

    private List<LocalDateTime> smallestSlotOrder(List<LocalDateTime> times, Map<LocalDateTime, Long> slots){
        List<LocalDateTime> ordered = new ArrayList<>();
        while(!times.isEmpty()){
            LocalDateTime smallest = times.get(0);
            for (LocalDateTime time: times){
                if (slots.get(time) < slots.get(smallest)){
                    smallest = time;
                }
            }
            ordered.add(smallest);
            times.remove(smallest);
        }
        return ordered;
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
        Map<LocalTime, LocalTime> freeTime = new HashMap<>();
        freeTime.put(LocalTime.of(21, 0), LocalTime.of(23, 59));
        freeTime.put(LocalTime.of(0, 0), LocalTime.of(9, 0));
        WorkSessionScheduler ws = new WorkSessionScheduler(freeTime, true);
        em.addEvent("hello", LocalDateTime.of(2021, 11, 19, 9,0));
        Event e = new Event(3, "this", LocalDateTime.of(2021,11,17,13,30),
                LocalDateTime.of(2021, 11, 17, 14, 30));
        em.addEvent(e);
        ws.setHoursNeeded(em.get(1), 10L, em);

        System.out.println("---------------");
        System.out.println(em.get(1).getWorkSessions());

    }
}
