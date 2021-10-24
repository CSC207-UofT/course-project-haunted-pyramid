package usecases;
import entities.Event;
import interfaces.Recursive;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecurringManager {
    public static <T extends Event> Set<Event> schedule(T event, String repetition, List<Integer> end, EventManager em) {
        //the list of events that have been added/changed to the EventManager by RecurringManager
        Set<Event> changes = new HashSet<Event>(); //For CalendarManager
        changes.add(event);

        LocalDateTime endDateTime = LocalDateTime.of(end.get(0), end.get(1), end.get(2), end.get(3), end.get(4));
        /**
         * recursively creates each subsequent event based on type of repetition and end date (same type and title as
         * original), adds it to the EventManager, and returns list of changed/created Events [all with different ID]
         */
//        if (repetition.equalsIgnoreCase("weekly")){
//            LocalDate nextDate = ((Event)event).getDay().plusDays(7);
//            if (!(nextDate.isAfter(endDateTime.toLocalDate()))){
//                LocalTime start = ((Event)event).getStartTime().toLocalTime();
//                LocalTime endTime = ((Event)event).getStartTime().toLocalTime();
//
//                ((Recursive<T>)event).setNext();
//                em.addEvent(event);
//            }
//            if (((Recursive)event).getNext().isPresent()){
//                changes.addAll(schedule(((Recursive<T>)event).getNext().get(), repetition, end, em));
//            }
//        }
        //TODO forms of repetition
        return changes;
    }
}
