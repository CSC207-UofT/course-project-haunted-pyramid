package entities;

import interfaces.DateGetter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;

public class IntervalDateInput implements DateGetter {

    private LocalDateTime[] periodOfRepetition;

    public IntervalDateInput(LocalDateTime beginningOfCycles, LocalDateTime endOfCycles){
        this.periodOfRepetition = new LocalDateTime[2];
        this.periodOfRepetition[0] = beginningOfCycles;
        this.periodOfRepetition[1] = endOfCycles;
    }

    private Event getEventAfterStartDate(Event event, Period period){
        LocalDateTime startTime = event.getStartTime();
        Period startEndTimeDifference = Period.between(LocalDate.from(event.getStartTime()), LocalDate.from(event.getEndTime()));
        while(startTime.isBefore(this.periodOfRepetition[0])){
            startTime = startTime.plus(period);
        }
        return new Event(ConstantID.get(), event.getName(), startTime, event.getEndTime().plus(startEndTimeDifference));
    }

    private ArrayList<Event> getEventListAfterBeginningOfCycles(ArrayList<Event> events, Period period){
        ArrayList<Event> result = new ArrayList<>();
        result.add(getEventAfterStartDate(events.get(0), period));
        for(Event event : events){
            Event newEvent = getEventAfterStartDate(event, period);
            if(newEvent.getStartTime().isBefore(result.get(0).getStartTime())){
                result.add(0, newEvent);
            }
            else{
                result.add(newEvent);
            }
        }
        return result;
    }


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
        LocalDateTime currentDate = newEvents.get(0).getStartTime();
        int i = 2;
        int eventIndex = 0;
        while(currentDate.plus(period).isBefore(this.periodOfRepetition[1])){
            Event thisEvent = newEvents.get(eventIndex);
            LocalDateTime newStartTime = thisEvent.getStartTime().plus(period);
            LocalDateTime newEndTime = thisEvent.getEndTime().plus(period);
            Event newThisEvent = new Event(ConstantID.get(), thisEvent.getName() + "-" + i, newStartTime, newEndTime);
            result.add(newThisEvent);
            newEvents.remove(eventIndex);
            newEvents.add(eventIndex, newThisEvent);
            eventIndex++;
            if(eventIndex == cycleLength){
                eventIndex = 0;
                i ++;
            }
            currentDate = newEvents.get(eventIndex).getStartTime();
        }
        events.add(firstEvent2);
        return result;
    }
}
