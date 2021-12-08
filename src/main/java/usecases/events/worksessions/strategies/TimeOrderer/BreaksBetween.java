package usecases.events.worksessions.strategies.TimeOrderer;

import entities.Event;
import usecases.events.EventManager;
import usecases.events.worksessions.WorkSessionManager;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Breaks Between is a TimeOrderer class that modifies a list of times based on free time and surrounding scheduler so that
 * a session of input length scheduled at an ideal time in the list will not be directly before or after another session
 * of the same event
 *
 * @author Taite Cullen
 */
public class BreaksBetween implements TimeOrderer {
    private final String spacing;

    /**
     * constructs breaks between object with valid spacing
     *
     * @param spacing String "short", "medium" or "large"
     */
    public BreaksBetween(String spacing) {
        this.spacing = spacing;
    }

    /**
     * modifies a list of times based on free time and surrounding scheduler so that
     * a session of input length scheduled at an ideal time in the list will not be directly before or after another session
     * of the same event
     *
     * @param deadline     the event that all times must fall before
     * @param eventManager the eventManager containing the deadline
     * @param length       the duration that will be placed at the ideal start time
     * @param idealDates   an ordered list of ideal dates
     * @param times        a list of eligible times that will be modified
     * @param timeGetter   the timeGetter used to get the eligible times
     */
    @Override
    public void order(UUID deadline, EventManager eventManager, Long length, List<LocalDate> idealDates,
                      List<LocalDateTime> times, TimeGetter timeGetter) {
        List<LocalDateTime> ordered = new ArrayList<>();
        for (LocalDateTime time : times) {
            if (!sessionBefore(eventManager, deadline, time) && !sessionAfter(eventManager, deadline, time,
                    length)) {
                ordered.add(time);
            }
        }
        times.removeAll(ordered);
        switch (spacing) {
            case "short":
                smallSpacing(deadline, eventManager, length, times, ordered, timeGetter);
                break;
            case "medium":
                mediumSpacing(deadline, eventManager, length, times, ordered, timeGetter);
                break;
            case "large":
                largeSpacing(deadline, eventManager, length, times, ordered, timeGetter);
                break;
        }
        this.mediumSpacing(deadline, eventManager, length, times, ordered, timeGetter);
        times.clear();
        times.addAll(ordered);
        this.dateOrder(idealDates, times);
    }

    /**
     * modifies the list to have preferred two hour breaks between sessions
     *
     * @param deadline     the event that all times must fall before
     * @param eventManager the eventManager containing the deadline
     * @param length       the duration that will be placed at the ideal start time
     * @param times        a list of eligible times that will be modified
     * @param timeGetter   the timeGetter used to get the eligible times
     */
    private void mediumSpacing(UUID deadline, EventManager eventManager, Long length,
                               List<LocalDateTime> times, List<LocalDateTime> ordered, TimeGetter timeGetter) {
        checkSlots(deadline, eventManager, length, times, ordered, timeGetter);
        this.smallSpacing(deadline, eventManager, length, times, ordered, timeGetter);
    }

    /**
     * orders times so that longest slots are first
     *
     * @param deadline     the event that all times must fall before
     * @param eventManager the eventManager containing the deadline
     * @param length       the duration that will be placed at the ideal start time
     * @param times        a list of eligible times that will be modified
     * @param ordered      a list to add ordered events to
     * @param timeGetter   the timeGetter used to get the eligible times
     */
    private void checkSlots(UUID deadline, EventManager eventManager, Long length, List<LocalDateTime> times, List<LocalDateTime> ordered, TimeGetter timeGetter) {
        timeGetter.freeSlots(LocalDateTime.now(), eventManager.getDefaultEventInfoGetter().getEnd(deadline), eventManager, deadline);
        List<LocalDateTime> toRemove = new ArrayList<>();
        for (LocalDateTime time : times) {
            if (sessionBefore(eventManager, deadline, time)) {
                Long timeAfter = timeGetter.freeSlots(time, eventManager.getDefaultEventInfoGetter().getEnd(deadline), eventManager, deadline).get(time);
                if (timeAfter >= 4L) {
                    ordered.add(time.plusHours(2));
                    toRemove.add(time);
                }
            }
            if (sessionAfter(eventManager, deadline, time, length)) {
                if (timeGetter.freeSlots(time.minusHours(4), time, eventManager, deadline).size() == 1 && timeGetter.freeSlots(time.minusHours(4), time, eventManager, deadline).containsKey(time.minusHours(4))) {
                    ordered.add(time.minusHours(1));
                    toRemove.add(time);
                }
            }
        }
        times.removeAll(toRemove);
    }

    /**
     * modifies the list to have preferred three hour breaks between sessions
     *
     * @param deadline     the event that all times must fall before
     * @param eventManager the eventManager containing the deadline
     * @param length       the duration that will be placed at the ideal start time
     * @param times        a list of eligible times that will be modified
     * @param timeGetter   the timeGetter used to get the eligible times
     */
    private void largeSpacing(UUID deadline, EventManager eventManager, Long length,
                              List<LocalDateTime> times, List<LocalDateTime> ordered, TimeGetter timeGetter) {
        checkSlots(deadline, eventManager, length, times, ordered, timeGetter);
        this.mediumSpacing(deadline, eventManager, length, times, ordered, timeGetter);
    }

    /**
     * modifies the list to have preferred one hour breaks between sessions
     *
     * @param deadline     the event that all times must fall before
     * @param eventManager the eventManager containing the deadline
     * @param length       the duration that will be placed at the ideal start time
     * @param times        a list of eligible times that will be modified
     * @param timeGetter   the timeGetter used to get the eligible times
     */
    private void smallSpacing(UUID deadline, EventManager eventManager, Long length,
                              List<LocalDateTime> times, List<LocalDateTime> ordered, TimeGetter timeGetter) {
        checkSlots(deadline, eventManager, length, times, ordered, timeGetter);
        ordered.addAll(times);
    }

    /**
     * checks if deadline has a work session that ends directly before the given start time
     *
     * @param deadline     the event that all times must fall before
     * @param eventManager the eventManager containing the deadline
     * @param start        LocalDateTime
     * @return returns true if there is a session that ends at this start time
     */
    private boolean sessionBefore(EventManager eventManager, UUID deadline, LocalDateTime start) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        for (Event session : workSessionManager.getWorkSessions(deadline)) {
            if (eventManager.getDefaultEventInfoGetter().getEnd(session).equals(start)) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks if deadline has a work session that starts directly after an event at this start time with the given length
     * would end
     *
     * @param eventManager the eventMAnager containing the deadline
     * @param deadline     the event that all times must fall before
     * @param start        LocalDateTime
     * @param length       the proposed length of the session
     * @return returns true if there is a session directly after a session at this start time
     */
    private boolean sessionAfter(EventManager eventManager, UUID deadline, LocalDateTime start, Long length) {
        WorkSessionManager workSessionManager = new WorkSessionManager(eventManager);
        for (Event session : workSessionManager.getWorkSessions(deadline)) {
            if (eventManager.getDefaultEventInfoGetter().getStart(session).equals(start.plusHours(length))) {
                return true;
            }
        }
        return false;
    }
}
