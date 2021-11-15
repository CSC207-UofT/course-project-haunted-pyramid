package entities.recursions;

import entities.ConstantID;
import entities.Event;
import interfaces.DateGetter;
import usecases.events.EventManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;

/**
 * @author Malik Lahlou
 */

public class IntervalDateInput implements DateGetter {

    private LocalDateTime[] periodOfRepetition;

    /**
     * constructor of IntervalDateInput.
     * @param beginningOfCycles The date this repetition should begin.
     * @param endOfCycles The date this repetition ends.
     */

    public IntervalDateInput(LocalDateTime beginningOfCycles, LocalDateTime endOfCycles){
        this.periodOfRepetition = new LocalDateTime[2];
        this.periodOfRepetition[0] = beginningOfCycles;
        this.periodOfRepetition[1] = endOfCycles;
    }

    /**
     * helper method
     * @param event The date this repetition should begin.
     * @return the end date if there is no start date, and the start date otherwise.
     */

    private LocalDateTime startTimeGetter(Event event){
        if(event.getStartTime() == null){
            return event.getEndTime();
        }
        else{
            return event.getStartTime();
        }
    }

    /**
     * helper method.
     * @param event the event I want to copy.
     * @param period the time period that should be added to event start/end time until it's after periodOfRepetition[0].
     * @return creates an event after periodOfRepetition[0] by adding period to the event start or end date (if there
     * is no start date).
     */

    private Event getEventAfterStartDate(Event event, Period period){
        LocalDateTime startTime = this.startTimeGetter(event);
        Period startEndTimeDifference = Period.between(LocalDate.from(startTime), LocalDate.from(event.getEndTime()));
        LocalDateTime hoursDifference = event.getEndTime().minusHours(startTime.getHour());
        while(startTime.isBefore(this.periodOfRepetition[0])){
            startTime = startTime.plus(period);
        }
        if (startEndTimeDifference == Period.ZERO & hoursDifference.getHour() == 0){
            return new Event(ConstantID.get(), event.getName(), startTime);
        }
        else{
            LocalDateTime endTime = startTime.plusHours(hoursDifference.getHour());
            endTime = endTime.plus(startEndTimeDifference);
            return new Event(ConstantID.get(), event.getName(), startTime, endTime);
        }

    }

    /**
     * helper method.
     * @param events the list of events getEventAfterStartDate should be applied to.
     * @param period the time period that should be added to event start/end time until it's after periodOfRepetition[0].
     * @return apply getEventAfterStartDate to events in the list, then order them in chronological order.
     */

    private ArrayList<Event> getEventListAfterBeginningOfCycles(ArrayList<Event> events, Period period) {
        ArrayList<Event> result = new ArrayList<>();
        for (Event event : events) {
            Event newEvent = getEventAfterStartDate(event, period);
            result.add(newEvent);
        }
        EventManager e = new EventManager();
        return (ArrayList<Event>) e.timeOrder(result);
    }

    /**
     * returns repetitions of event in events by adding the period between the first and last event in the list to
     * each event in the list until one become after periodOfRepetition[1].
     * @param events the list of events I want to repeat.
     * @return list of repetitions of event in events
     */

    @Override
    public ArrayList<Event> listOfDatesInCycles(ArrayList<Event> events) {
        int cycleLength = events.size() - 1;
        Event firstEvent1 = events.get(0);
        Event firstEvent2 = events.get(cycleLength);
        events.remove(cycleLength);
        LocalDateTime eventDate1 = firstEvent1.getEndTime();
        LocalDateTime eventDate2 = firstEvent2.getEndTime();
        Period period = Period.between(LocalDate.from(eventDate1), LocalDate.from(eventDate2));
        ArrayList<Event> newEvents = getEventListAfterBeginningOfCycles(events, period);
        ArrayList<Event> result = new ArrayList<>();
        LocalDateTime currentDate = this.startTimeGetter(newEvents.get(0));
        if (this.periodOfRepetition[0].isAfter(eventDate1)){
            result.addAll(newEvents);
        }
        int i = 2;
        int eventIndex = 0;
        while(currentDate.plus(period).isBefore(this.periodOfRepetition[1])){
            String name = newEvents.get(eventIndex).getName();
            Event thisEvent = newEvents.get(eventIndex);
            LocalDateTime newEndTime = thisEvent.getEndTime().plus(period);
            Event newThisEvent = new Event(ConstantID.get(), name + "-" + i, newEndTime);
            if(thisEvent.getStartTime() != null){
                LocalDateTime newStartTime = thisEvent.getStartTime().plus(period);
                newThisEvent.setStartTime(newStartTime);
            }
            result.add(newThisEvent);
            newEvents.remove(eventIndex);
            newEvents.add(eventIndex, newThisEvent); // To keep adding only one period instead of multiplying by scalar.
            eventIndex++;
            if(eventIndex == cycleLength){
                eventIndex = 0;
                i ++;
            }
            currentDate = this.startTimeGetter(newEvents.get(eventIndex));
        }
        events.add(firstEvent2);
        return result;
    }
}
