package usecases.events.worksessions.strategies.TimeOrderer;

import entities.Event;
import usecases.events.EventManager;
import usecases.events.worksessions.WorkSessionManager;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BreaksBetween implements TimeOrderer {
    private String spacing;
    public BreaksBetween(String spacing){
        this.spacing = spacing;
    }
    @Override
    public void order(UUID deadline, EventManager eventManager, Long length, List<LocalDate> idealDates,
                      List<LocalDateTime> times, TimeGetter timeGetter) {
        List<LocalDateTime> ordered = new ArrayList<>();
        for (LocalDateTime time: times){
            if (!sessionBefore(eventManager, deadline, time) && !sessionAfter(eventManager, deadline, time,
                    length)){
                ordered.add(time);
            }
        }
        times.removeAll(ordered);
        switch (spacing){
            case "short":
                smallSpacing(deadline, eventManager, length, idealDates, times, ordered, timeGetter);
                break;
            case "medium":
                mediumSpacing(deadline, eventManager, length, idealDates, times, ordered, timeGetter);
                break;
            case "large":
                largeSpacing(deadline, eventManager, length, idealDates, times, ordered, timeGetter);
                break;
        }
        this.mediumSpacing(deadline, eventManager, length, idealDates, times, ordered, timeGetter);
        times.clear();
        times.addAll(ordered);
        this.dateOrder(idealDates, times);
    }

    private void mediumSpacing(UUID deadline, EventManager eventManager, Long length, List<LocalDate> idealDates,
                              List<LocalDateTime> times, List<LocalDateTime> ordered, TimeGetter timeGetter){
        timeGetter.freeSlots(LocalDateTime.now(), eventManager.getEnd(deadline), eventManager, deadline);
        List<LocalDateTime> toRemove = new ArrayList<>();
        for (LocalDateTime time: times){
            if (sessionBefore(eventManager, deadline, time)){
                Long timeAfter = timeGetter.freeSlots(time, eventManager.getEnd(deadline), eventManager, deadline).get(time);
                if (timeAfter >= 4L){
                    ordered.add(time.plusHours(2));
                    toRemove.add(time);
                }
            }
            if (sessionAfter(eventManager, deadline, time, length)){
                if(timeGetter.freeSlots(time.minusHours(4), time, eventManager, deadline).size() == 1 && timeGetter.freeSlots(time.minusHours(4), time, eventManager, deadline).containsKey(time.minusHours(4))){
                    ordered.add(time.minusHours(1));
                    toRemove.add(time);
                }
            }
        }
        times.removeAll(toRemove);
        this.smallSpacing(deadline, eventManager, length, idealDates, times, ordered, timeGetter);
    }
    private  void largeSpacing(UUID deadline, EventManager eventManager, Long length, List<LocalDate> idealDates,
                               List<LocalDateTime> times, List<LocalDateTime> ordered, TimeGetter timeGetter){
        timeGetter.freeSlots(LocalDateTime.now(), eventManager.getEnd(deadline), eventManager, deadline);
        List<LocalDateTime> toRemove = new ArrayList<>();
        for (LocalDateTime time: times){
            if (sessionBefore(eventManager, deadline, time)){
                Long timeAfter = timeGetter.freeSlots(time, eventManager.getEnd(deadline), eventManager, deadline).get(time);
                if (timeAfter >= 4L){
                    ordered.add(time.plusHours(2));
                    toRemove.add(time);
                }
            }
            if (sessionAfter(eventManager, deadline, time, length)){
                if(timeGetter.freeSlots(time.minusHours(4), time, eventManager, deadline).size() == 1 && timeGetter.freeSlots(time.minusHours(4), time, eventManager, deadline).containsKey(time.minusHours(4))){
                    ordered.add(time.minusHours(1));
                    toRemove.add(time);
                }
            }
        }
        times.removeAll(toRemove);
        this.mediumSpacing(deadline, eventManager, length, idealDates, times, ordered, timeGetter);
    }

    private void smallSpacing(UUID deadline, EventManager eventManager, Long length, List<LocalDate> idealDates,
                              List<LocalDateTime> times, List<LocalDateTime> ordered, TimeGetter timeGetter){
        timeGetter.freeSlots(LocalDateTime.now(), eventManager.getEnd(deadline), eventManager, deadline);
        List<LocalDateTime> toRemove = new ArrayList<>();
        for (LocalDateTime time: times){
            if (sessionBefore(eventManager, deadline, time)){
                Long timeAfter = timeGetter.freeSlots(time, eventManager.getEnd(deadline), eventManager, deadline).get(time);
                if (timeAfter >= 4L){
                    ordered.add(time.plusHours(2));
                    toRemove.add(time);
                }
            }
            if (sessionAfter(eventManager, deadline, time, length)){
                if(timeGetter.freeSlots(time.minusHours(4), time, eventManager, deadline).size() == 1 && timeGetter.freeSlots(time.minusHours(4), time, eventManager, deadline).containsKey(time.minusHours(4))){
                    ordered.add(time.minusHours(1));
                    toRemove.add(time);
                }
            }
        }
        times.removeAll(toRemove);
        ordered.addAll(times);
    }

    private boolean sessionBefore(EventManager eventManager, UUID deadline, LocalDateTime start){
        WorkSessionManager workSessionManager = new WorkSessionManager();
        for (Event session: workSessionManager.getWorkSessions(eventManager, deadline)){
            if (eventManager.getEnd(session).equals(start)){
                return true;
            }
        }
        return false;
    }
    private boolean sessionAfter(EventManager eventManager, UUID deadline, LocalDateTime start, Long length){
        WorkSessionManager workSessionManager = new WorkSessionManager();
        for (Event session: workSessionManager.getWorkSessions(eventManager, deadline)){
            if (eventManager.getStart(session).equals(start.plusHours(length))){
                return true;
            }
        }
        return false;
    }
}
