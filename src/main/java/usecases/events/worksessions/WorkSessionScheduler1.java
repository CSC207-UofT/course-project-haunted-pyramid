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
public class WorkSessionScheduler1 implements EventListObserver {
    //specified by saved user information - the time during which the user does not want to work
    private Map<LocalTime, LocalTime> freeTime;

//    /**
//     * A Method which automatically schedules work sessions for an Event which only has a deadline associated with it
//     * has two options or strategies for scheduling work sessions - only one implemented so far
//     *
//     * @param deadline     An Event
//     * @param eventManager An EventManager
//     */
//    private void autoSchedule(UUID deadline, EventManager eventManager) {
//        this.autoScheduleNoProcrastinate(deadline, eventManager);
//    }

//    /**
//     * autoScheduling if set to not procrastinate. creates work sessions on days with the fewest work session
//     * starting as soon as possible and ending the day before the deadline
//     *
//     * @param deadline     the deadline event ID
//     * @param eventManager the eventManager to be scheduled around
//     */
//    private void autoScheduleNoProcrastinate(UUID deadline, EventManager eventManager) {
//        LocalDateTime deadlineTime = eventManager.getEnd(deadline);
//        eventManager.setWorkSessions(deadline, eventManager.getPastSessions(deadline));
//        Long hoursToSchedule = (long) (eventManager.getTotalHoursNeeded(deadline) -
//                eventManager.totalHours(eventManager.getPastWorkSession(deadline)));
//        while (!(hoursToSchedule == 0)) {
////            Long length = eventManager.getEventSessionLength(deadline);
////            if (hoursToSchedule < eventManager.getEventSessionLength(deadline)) {
////                length = hoursToSchedule;
////            }
////            hoursToSchedule -= length;
//            Map<LocalDate, List<Event>> schedule = updateSchedule(eventManager, deadlineTime);
//            List<LocalDate> days = (this.eligibleDays(schedule, length, deadlineTime));
//            days = this.leastWorkSessionsOrder(days, eventManager.getWorkSessions(deadline));
//            Map<LocalDateTime, Long> freeSlots;
//            if (days.isEmpty()) {
//                return;
//            }
//            freeSlots = getFreeSlots(deadlineTime, schedule, days.get(0));
//            List<LocalDateTime> times = this.eligibleTimes(freeSlots, length);
//            times = this.smallestSlotOrder(times, freeSlots);
//            LocalDateTime time = times.get(0);
//            if (this.sessionAdjacent(time, length, eventManager.getTotalWorkSession(deadline))){
//                time = time.plusHours((freeSlots.get(time) - length)/2);
//            }
//            eventManager.addWorkSession(deadline, time, time.plusHours(length));
//        }
//    }




//    /**
//     * schedules assuming procrastinate. creates work sessions close to the deadline
//     *
//     * @param deadline     the deadline event
//     * @param eventManager the event manager to schedule around
//     */
//    private void autoScheduleProcrastinate(Event deadline, EventManager eventManager) {
//        // TODO implement this and other strategy method for autoScheduling for phase 2 - partially implemented
//        long totalNeeded = (long) (deadline.getHoursNeeded() - eventManager.totalHours(deadline.pastWorkSessions()));
//        // Divide the total hours needed by 3 to get time for each work session
//        Long sessionLength = totalNeeded / 3L;
//        deadline.setWorkSessions(deadline.pastWorkSessions());
//
//        // Get the three days before the end date of the deadline event
//        LocalDateTime firstDay = deadline.getEndTime().minusDays(3);
//        LocalDateTime secondDay = deadline.getEndTime().minusDays(2);
//        LocalDateTime thirdDay = deadline.getEndTime().minusDays(1);
//
//        Map<LocalDate, List<Event>> tempSchedule = eventManager.getRange(firstDay.toLocalDate(),
//                thirdDay.toLocalDate());
//
//        ArrayList<LocalDateTime> daysList = new ArrayList<>() {
//            {
//                add(firstDay);
//                add(secondDay);
//                add(thirdDay);
//
//            }
//        };
//        for (LocalDateTime day : daysList) {
//            if (tempSchedule.get(day.toLocalDate()).size() == 0) {
//                LocalDate infoDay = day.toLocalDate();
//                LocalDateTime startTimeForDay = infoDay.atTime(12, 0);
//                deadline.addWorkSession(startTimeForDay, startTimeForDay.plusHours(sessionLength));
//            } else {
//                // freeSlots for each day
//                Map<LocalDateTime, Long> freeSlots = eventManager.freeSlots(day.toLocalDate().atTime(9, 0),
//                        tempSchedule.get(day.toLocalDate()), day.toLocalDate().atTime(23, 59));
//
//                for (LocalDateTime slot : freeSlots.keySet()) {
//                    if (freeSlots.get(slot) >= sessionLength) {
//                        deadline.addWorkSession(slot, slot.plusHours(sessionLength));
//                        break;
//                    }
//                }
//
//            }
//        }
//    }

//    /**
//     * A helper method for determining which days in a map have free slots that are usable
//     *
//     * @param schedule the map of days to lists of events
//     * @param length   the length of the sessions to fit
//     * @param deadline the deadline event
//     * @return a list of eligible days
//     */
//    private List<LocalDate> eligibleDays(Map<LocalDate, List<Event>> schedule, Long length, LocalDateTime
//            deadline) {
//        EventManager em = new EventManager(new ArrayList<>());
//        List<LocalDate> eligible = new ArrayList<>();
//        for (LocalDate day : schedule.keySet()) {
//            LocalTime startTime = LocalTime.of(0, 0);
//            LocalTime endTime = LocalTime.of(23, 59);
//            if (LocalDate.now().isEqual(day)) {
//                startTime = LocalTime.now();
//            }
//            if (deadline.toLocalDate().isEqual(day)) {
//                endTime = deadline.toLocalTime();
//            }
//
//            LocalDateTime start = LocalDateTime.of(day, startTime);
//            LocalDateTime end = LocalDateTime.of(day, endTime);
//            if (!this.eligibleTimes(em.freeSlots(start, schedule.get(day), end), length).isEmpty()) {
//                eligible.add(day);
//            }
//        }
//        return eligible;
//    }

//    /**
//     * returns the times in which a length will fit
//     *
//     * @param slots  Map of days to durations of free slot
//     * @param length the length of the session to fit
//     * @return a list of eligible times for this freeSlots map
//     * @see EventManager#freeSlots(LocalDateTime, List, LocalDateTime)
//     */
//    private List<LocalDateTime> eligibleTimes(Map<LocalDateTime, Long> slots, Long length) {
//        List<LocalDateTime> eligible = new ArrayList<>();
//        for (LocalDateTime time : slots.keySet()) {
//            if (slots.get(time) >= length) {
//                eligible.add(time);
//            }
//        }
//        return eligible;
//    }

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
        EventManager eventManager = new EventManager(new ArrayList<> ());
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

    @Override
    public void update(String addRemoveChange, Event changed, EventManager eventManager) {

    }
}
