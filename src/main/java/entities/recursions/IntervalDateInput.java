package entities.recursions;

import entities.ConstantID;
import entities.Event;
import interfaces.DateGetter;
import usecases.events.EventManager;

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

    private LocalDateTime startTimeGetter(Event event){
        if(event.getStartTime() == null){
            return event.getEndTime();
        }
        else{
            return event.getStartTime();
        }
    }

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

    private ArrayList<Event> getEventListAfterBeginningOfCycles(ArrayList<Event> events, Period period){
        ArrayList<Event> result = new ArrayList<>();
        for(Event event : events){
            Event newEvent = getEventAfterStartDate(event, period);
            result.add(newEvent);
        }
        EventManager e = new EventManager();
        ArrayList<Event> newResult = (ArrayList<Event>) e.timeOrder(result);
        return newResult;
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
