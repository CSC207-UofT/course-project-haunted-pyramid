package entities;

import interfaces.DateGetter;

import javax.lang.model.element.NestingKind;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RecursiveEvent{

    // + eventsInOneCycle ;for example if a lecture is tuesdays and thurdays of each week, the cycle will consist
    // of [(tuesday, date+time of lecture), (thursday, date+time of lecture), (tuesday, date+time of lecture after the
    // thursday one)]. If an event repeats 3 times each 2 week, the list would have 3 elements with the days and
    // times of the three occurrences of the event.
    // methodToGetDate: indicates which method the user want to handle repetitions. For now, the user can either input
    // the number of times a cycle repeats, or the two dates in between the cycle repeats.

    private ArrayList<Event> eventsInOneCycle;
    private DateGetter methodToGetDate;

    public RecursiveEvent(int id, String name, String type, String categoryName,
                          ArrayList<LocalDateTime> listOfEndDates, ArrayList<String> listOfOtherInformation){
        this.eventsInOneCycle = new ArrayList<>();
        int cycleLength = listOfEndDates.size();
        for(int i = 0; i < cycleLength; i++) {
            Event event = new Event(id, name, type, listOfEndDates.get(i), true, categoryName,
                    listOfOtherInformation.get(i));
            this.eventsInOneCycle.add(event);
        }
    }

    public int getCycleLength(){
        return eventsInOneCycle.size() -1;
    }

    public void setEventsInOneCycle(ArrayList<Event> eventsInOneCycle) {
        this.eventsInOneCycle = eventsInOneCycle;
    }

    public ArrayList<LocalDateTime> listOfDatesInCycles(){
        return methodToGetDate.listOfDatesInCycles(this.eventsInOneCycle);
    }
}
