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
 *
 * @author Shahzada Muhammad Shameel Farooq
 * @author Taite Cullen
 * @version %I%, %G%
 */
public class WorkSessionScheduler implements EventListObserver {
    //specified by saved user information - the time during which the user does not want to work
    private Map<LocalTime, LocalTime> freeTime;
    private boolean procrastinate;


    /**
     * A constructor for WorkSessionScheduler
     *
     * @param freeTime      Time slots for which user does not want to work
     * @param procrastinate Option for user, allows for a unique way of scheduling work sessions
     */
    public WorkSessionScheduler(Map<LocalTime, LocalTime> freeTime, boolean procrastinate) {
        this.freeTime = freeTime;
        this.procrastinate = procrastinate;
    }

    /**
     * A method for setting the length of the session
     *
     * @param deadline      An Event
     * @param sessionLength A Long which is the session length
     * @param eventManager  An EventManager
     */
    public void setSessionLength(Event deadline, Long sessionLength, EventManager eventManager) {
        deadline.setSessionLength(sessionLength);
        this.autoSchedule(deadline, eventManager);
    }

    public void setProcrastinate(boolean procrastinate) {
        this.procrastinate = procrastinate;
    }

    /**
     * @param deadline     the deadline event
     * @param hoursNeeded  whole long number of hours of this event to be scheduled
     * @param eventManager the eventManager to autoSchedule around
     */
    public void setHoursNeeded(Event deadline, Long hoursNeeded, EventManager eventManager) {
        deadline.setHoursNeeded(hoursNeeded);
        this.autoSchedule(deadline, eventManager);
    }

    /**
     * @param deadlineTime the deadline event - end of last freeSlot
     * @param schedule     the schedule to get free slots between events
     * @param day          the day to get the freeSlots from
     * @return freeSlots over the whole day if it is not today or deadline day, otherwise start or end free
     * slots at now or deadline time
     */
    private Map<LocalDateTime, Long> getFreeSlots(LocalDateTime deadlineTime, Map<LocalDate, List<Event>> schedule, LocalDate day) {
        EventManager eventManager = new EventManager();
        Map<LocalDateTime, Long> freeSlots;
        if (day.isEqual(LocalDate.now())) {
            freeSlots = eventManager.freeSlots(LocalDateTime.now(), schedule.get(day),
                    LocalDateTime.of(day, LocalTime.of(23, 59)));
        } else if (day.isEqual(deadlineTime.toLocalDate())) {
            LocalDateTime start = LocalDateTime.of(day, LocalTime.of(0, 0));
            freeSlots = eventManager.freeSlots(start, schedule.get(day), deadlineTime);
        } else {
            freeSlots = eventManager.freeSlots(LocalDateTime.of(day, LocalTime.of(0, 0)),
                    schedule.get(day), LocalDateTime.of(day, LocalTime.of(23, 59)));
        }
        return freeSlots;
    }

    /**
     * gets a schedule from EventManager and adds free time to each day
     *
     * @param eventManager the eventManager from which to get the events to schedule around
     * @param deadlineTime the LocalDateTime deadline (end time for range)
     * @return the Map with key=day, value=list of events on that day
     */
    private Map<LocalDate, List<Event>> updateSchedule(EventManager eventManager, LocalDateTime deadlineTime) {
        Map<LocalDate, List<Event>> schedule = eventManager.getRange(LocalDate.now(), deadlineTime.toLocalDate());
        for (LocalDate day : schedule.keySet()) {
            for (LocalTime start : this.freeTime.keySet()) {
                schedule.get(day).add(new Event(0, "free time", LocalDateTime.of(day, start),
                        LocalDateTime.of(day, this.freeTime.get(start))));
            }
        }
        return schedule;
    }

    /**
     * A Method which automatically schedules work sessions for an Event which only has a deadline associated with it
     * has two options or strategies for scheduling work sessions - only one implemented so far
     *
     * @param deadline     An Event
     * @param eventManager An EventManager
     */
    private void autoSchedule(Event deadline, EventManager eventManager) {
        this.autoScheduleNoProcrastinate(deadline, eventManager);
    }

    /**
     * autoScheduling if set to not procrastinate. creates work sessions on days with the fewest work session
     * starting as soon as possible and ending the day before the deadline
     *
     * @param deadline     the deadline event
     * @param eventManager the eventManager to be scheduled around
     */
    private void autoScheduleNoProcrastinate(Event deadline, EventManager eventManager) {
        LocalDateTime deadlineTime = eventManager.getEnd(deadline);
        Integer ID = eventManager.getID(deadline);
        deadline.setWorkSessions(deadline.pastWorkSessions());
        Long hoursToSchedule = (long) (eventManager.getTotalHoursNeeded(ID) -
                eventManager.totalHours(eventManager.getPastWorkSession(ID)));
        while (!(hoursToSchedule == 0)) {
            Long length = eventManager.getEventSessionLength(ID);
            if (hoursToSchedule < eventManager.getEventSessionLength(ID)) {
                length = hoursToSchedule;
            }
            hoursToSchedule -= length;
            Map<LocalDate, List<Event>> schedule = updateSchedule(eventManager, deadlineTime);
            List<LocalDate> days = (this.eligibleDays(schedule, length, deadlineTime));
            days = this.leastWorkSessionsOrder(days, deadline.getWorkSessions());
            Map<LocalDateTime, Long> freeSlots;
            if (days.isEmpty()) {
                return;
            }
            freeSlots = getFreeSlots(deadlineTime, schedule, days.get(0));
            List<LocalDateTime> times = this.eligibleTimes(freeSlots, length);
            times = this.smallestSlotOrder(times, freeSlots);
            LocalDateTime time = times.get(0);
            if (this.sessionAdjacent(time, length, eventManager.getTotalWorkSession(ID))){
                time = time.plusHours((freeSlots.get(time) - length)/2);
            }
            deadline.addWorkSession(time, time.plusHours(length));
        }

    }

    private boolean sessionAdjacent(LocalDateTime time, Long length, List<Event> sessions) {
        for (Event session: sessions){
            if (session.getEndTime().isEqual(time) || (session.hasStart() &&
                    session.getStartTime().isEqual(time.plusHours(length)))){
                return true;
            }
        }
        return false;
    }


    /**
     * schedules assuming procrastinate. creates work sessions close to the deadline
     *
     * @param deadline     the deadline event
     * @param eventManager the event manager to schedule around
     */
    private void autoScheduleProcrastinate(Event deadline, EventManager eventManager) {
        // TODO implement this and other strategy method for autoScheduling for phase 2 - partially implemented
        long totalNeeded = (long) (deadline.getHoursNeeded() - eventManager.totalHours(deadline.pastWorkSessions()));
        // Divide the total hours needed by 3 to get time for each work session
        Long sessionLength = totalNeeded / 3L;
        deadline.setWorkSessions(deadline.pastWorkSessions());

        // Get the three days before the end date of the deadline event
        LocalDateTime firstDay = deadline.getEndTime().minusDays(3);
        LocalDateTime secondDay = deadline.getEndTime().minusDays(2);
        LocalDateTime thirdDay = deadline.getEndTime().minusDays(1);

        Map<LocalDate, List<Event>> tempSchedule = eventManager.getRange(firstDay.toLocalDate(),
                thirdDay.toLocalDate());

        ArrayList<LocalDateTime> daysList = new ArrayList<>() {
            {
                add(firstDay);
                add(secondDay);
                add(thirdDay);

            }
        };
        for (LocalDateTime day : daysList) {
            if (tempSchedule.get(day.toLocalDate()).size() == 0) {
                LocalDate infoDay = day.toLocalDate();
                LocalDateTime startTimeForDay = infoDay.atTime(12, 0);
                deadline.addWorkSession(startTimeForDay, startTimeForDay.plusHours(sessionLength));
            } else {
                // freeSlots for each day
                Map<LocalDateTime, Long> freeSlots = eventManager.freeSlots(day.toLocalDate().atTime(9, 0),
                        tempSchedule.get(day.toLocalDate()), day.toLocalDate().atTime(23, 59));

                for (LocalDateTime slot : freeSlots.keySet()) {
                    if (freeSlots.get(slot) >= sessionLength) {
                        deadline.addWorkSession(slot, slot.plusHours(sessionLength));
                        break;
                    }
                }

            }
        }
    }

    /**
     * A helper method for determining which days in a map have free slots that are usable
     *
     * @param schedule the map of days to lists of events
     * @param length   the length of the sessions to fit
     * @param deadline the deadline event
     * @return a list of eligible days
     */
    private List<LocalDate> eligibleDays(Map<LocalDate, List<Event>> schedule, Long length, LocalDateTime
            deadline) {
        EventManager em = new EventManager();
        List<LocalDate> eligible = new ArrayList<>();
        for (LocalDate day : schedule.keySet()) {
            LocalTime startTime = LocalTime.of(0, 0);
            LocalTime endTime = LocalTime.of(23, 59);
            if (LocalDate.now().isEqual(day)) {
                startTime = LocalTime.now();
            }
            if (deadline.toLocalDate().isEqual(day)) {
                endTime = deadline.toLocalTime();
            }

            LocalDateTime start = LocalDateTime.of(day, startTime);
            LocalDateTime end = LocalDateTime.of(day, endTime);
            if (!this.eligibleTimes(em.freeSlots(start, schedule.get(day), end), length).isEmpty()) {
                eligible.add(day);
            }
        }
        return eligible;
    }

    /**
     * returns the times in which a length will fit
     *
     * @param slots  Map of days to durations of free slot
     * @param length the length of the session to fit
     * @return a list of eligible times for this freeSlots map
     * @see EventManager#freeSlots(LocalDateTime, List, LocalDateTime)
     */
    private List<LocalDateTime> eligibleTimes(Map<LocalDateTime, Long> slots, Long length) {
        List<LocalDateTime> eligible = new ArrayList<>();
        for (LocalDateTime time : slots.keySet()) {
            if (slots.get(time) >= length) {
                eligible.add(time);
            }
        }
        return eligible;
    }

    /**
     * orders a list of days by how many work sessions occur on them, smallest to largest
     *
     * @param days         the days to order
     * @param workSessions the work sessions to count
     * @return ordered list of days, the least work sessions to most work sessions
     */
    private List<LocalDate> leastWorkSessionsOrder(List<LocalDate> days, List<Event> workSessions) {
        List<LocalDate> ordered = new ArrayList<>();

        while (!days.isEmpty()) {
            LocalDate leastWorkSessions = this.leastWorkSessions(days, workSessions);
            days.remove(leastWorkSessions);
            ordered.add(leastWorkSessions);
        }
        return ordered;
    }

    /**
     * computes the day with the least work sessions, helper for leastWorkSessionsOrder
     *
     * @param days         the days to be checked
     * @param workSessions the workSessions to count
     * @return the day with the fewest workSessions (from days)
     */
    private LocalDate leastWorkSessions(List<LocalDate> days, List<Event> workSessions) {
        EventManager eventManager = new EventManager();
        LocalDate leastWorkSessions = days.get(0);
        int record = workSessions.size();
        for (LocalDate day : days) {
            int num = 0;
            for (Event session : workSessions) {
                if (eventManager.getEnd(session).toLocalDate().isEqual(day)) {
                    num = num + 1;
                }
            }
            if (num <= record) {
                leastWorkSessions = day;
                record = num;
            }
        }

        return leastWorkSessions;
    }

    /**
     * returns a list of LocalDateTimes in a map in order from the smallest key to the largest key
     *
     * @param times a list of keys for slots
     * @param slots a map with key=LocalDateTime, value=long to find the minimum value key pair in
     * @return the LocalDateTime associated with the smallest value
     */
    private List<LocalDateTime> smallestSlotOrder(List<LocalDateTime> times, Map<LocalDateTime, Long> slots) {
        List<LocalDateTime> ordered = new ArrayList<>();
        while (!times.isEmpty()) {
            LocalDateTime smallest = times.get(0);
            for (LocalDateTime time : times) {
                if (slots.get(time) < slots.get(smallest)) {
                    smallest = time;
                }
            }
            ordered.add(smallest);
            times.remove(smallest);
        }
        return ordered;
    }

    /**
     * Method which marks complete a session for the Event
     *
     * @param event        An Event
     * @param session      A String which is the session
     * @param eventManager An EventManager
     */
    public void markComplete(Event event, String session, EventManager eventManager) {
        eventManager.timeOrder(event.getWorkSessions());
        event.setHoursNeeded((long) (event.getHoursNeeded() -
                event.getWorkSessions().get(Integer.parseInt(session)).getLength()));
        event.getWorkSessions().remove(event.getWorkSessions().get(Integer.parseInt(session)));
        this.autoSchedule(event, eventManager);
    }

    /**
     * Method which marks incomplete a session for the Event
     *
     * @param event        An Event
     * @param session      A String with details of session
     * @param eventManager An EventManager
     */
    public void markInComplete(Event event, String session, EventManager eventManager) {
        eventManager.timeOrder(event.getWorkSessions());
        event.getWorkSessions().remove(event.getWorkSessions().get(Integer.parseInt(session)));
        this.autoSchedule(event, eventManager);
    }


    /**
     * @param addRemoveChange what to do with the events in the map, 'add', 'remove' or 'change'
     *                        * where 'change' indicates a change of date
     * @param changed         map of days to events, where days are the
     * @param eventManager    the eventManager that was updated
     */
    @Override
    public void update(String addRemoveChange, Event changed, EventManager eventManager) {
        for (Event event : eventManager.getAllEvents()) {
            this.autoSchedule(event, eventManager);
        }
        System.out.println("updated");
    }

    public void setFreeTime(Map<LocalTime, LocalTime> currentFreeTime) {
        this.freeTime = currentFreeTime;
    }
}
