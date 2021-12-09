package entities.recursions;

import entities.Event;
import interfaces.DateGetter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Malik Lahlou
 */

public class NumberOfRepetitionInput implements DateGetter, Serializable {

    private final int numberOfRepetitions;

    /**
     * constructor of NumberOfRepetitionInput.
     * @param numberOfRepetitions the number of times I want to repeat a cycle.
     */

    public NumberOfRepetitionInput(int numberOfRepetitions){
        this.numberOfRepetitions = numberOfRepetitions;
    }

    /**
     * returns repetitions of event in events by adding the period between the first and last event in the list to
     * each event in the list a numberOfRepetitions of times
     * @param events the list of events I want to repeat.
     * @return list of repetitions of event in events
     */

    @Override
    public List<Event> listOfEventsInTheCycles(List<Event> events) {
        int cycleLength = events.size() - 1;
        Event firstEvent1 = events.get(0);
        Event firstEvent2 = events.get(cycleLength);
        LocalDateTime eventDate1 = firstEvent1.getEndTime();
        LocalDateTime eventDate2 = firstEvent2.getEndTime();
        Period period = Period.between(LocalDate.from(eventDate1), LocalDate.from(eventDate2));
        List<Event> result = new ArrayList<>();
        int repetitionIndex = 0;
        int i = 2;
        while(repetitionIndex < this.numberOfRepetitions){
            int eventIndex = 0;
            while(eventIndex < cycleLength){
                Event thisEvent = events.get(eventIndex);
                Period multiple = periodMultiplicationByScalar(period, repetitionIndex);
                LocalDateTime newEndTime = thisEvent.getEndTime().plus(multiple);
                Event newThisEvent = new Event(UUID.randomUUID(), thisEvent.getName() + "-" + i, newEndTime);
                if(thisEvent.getStartTime() != null){
                    LocalDateTime newStartTime = thisEvent.getStartTime().plus(multiple);
                    newThisEvent.setStartTime(newStartTime);
                }
                result.add(newThisEvent);
                eventIndex++;
            }
            repetitionIndex++;
            i++;
        }
        return result;
    }
}
