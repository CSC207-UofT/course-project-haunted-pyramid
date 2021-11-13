package entities.recursions;

import entities.ConstantID;
import entities.Event;
import interfaces.DateGetter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;

public class NumberOfRepetitionInput implements DateGetter {

    private int numberOfRepetitions;

    public NumberOfRepetitionInput(int numberOfRepetitions){
        this.numberOfRepetitions = numberOfRepetitions;
    }


    @Override
    public ArrayList<Event> listOfDatesInCycles(ArrayList<Event> events) {
        int cycleLength = events.size() - 1;
        Event firstEvent1 = events.get(0);
        Event firstEvent2 = events.get(cycleLength);
        LocalDateTime eventDate1 = firstEvent1.getEndTime();
        LocalDateTime eventDate2 = firstEvent2.getEndTime();
        Period period = Period.between(LocalDate.from(eventDate1), LocalDate.from(eventDate2));
        ArrayList<Event> result = new ArrayList<>();
        int repetitionIndex = 0;
        int i = 2;
        while(repetitionIndex < this.numberOfRepetitions){
            int eventIndex = 0;
            while(eventIndex < cycleLength){
                Event thisEvent = events.get(eventIndex);
                Period multiple = periodMultiplicationByScalar(period, repetitionIndex);
                LocalDateTime newEndTime = thisEvent.getEndTime().plus(multiple);
                Event newThisEvent = new Event(ConstantID.get(), thisEvent.getName() + "-" + i, newEndTime);
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
