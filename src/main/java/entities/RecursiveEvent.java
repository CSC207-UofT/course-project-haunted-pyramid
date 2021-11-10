package entities;

import interfaces.DateGetter;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RecursiveEvent{

    // + eventsInOneCycle ;for example if a lecture is tuesdays and thurdays of each week, the cycle will consist
    // of [(tuesday, date+time of lecture), (thursday, date+time of lecture), (tuesday, date+time of lecture after the
    // thursday one)]. If an event repeats 3 times each 2 week, the list would have 3 elements with the days and
    // times of the three occurrences of the event.
    // methodToGetDate: indicates which method the user want to handle repetitions. For now, the user can either input
    // the number of times a cycle repeats, or the two dates in between the cycle repeats.

    private Integer id;
    private ArrayList<Event> eventsInOneCycle;
    private DateGetter methodToGetDate;

    public RecursiveEvent(Integer id, ArrayList<Event> events, DateGetter methodToGetDate){
        this.id = id;
        this.eventsInOneCycle = new ArrayList<>();
        for(Event event : events){
            this.eventsInOneCycle.add(event);
            event.setRecursiveId(id);
        }
        this.methodToGetDate = methodToGetDate;
    }

    /**
     * Getter methods.
     */

    public Integer getId() {return id;}
    public ArrayList<Event> getEventsInOneCycle() {return eventsInOneCycle;}
    public DateGetter getMethodToGetDate() {return methodToGetDate;}
    public int getCycleLength(){
        return eventsInOneCycle.size() -1;
    }

    /**
     * Setter methods.
     */
    public void setEventsInOneCycle(ArrayList<Event> eventsInOneCycle) {this.eventsInOneCycle = eventsInOneCycle;}
    public void setMethodToGetDate(DateGetter methodToGetDate) {this.methodToGetDate = methodToGetDate;}

    /**
     *
     * Uses the classes that implement the date getter interface to return the dates of all the events in the
     * period of repetition.
     * @return
     */

    public ArrayList<Event> listOfDatesInCycles(){
        return methodToGetDate.listOfDatesInCycles(this.eventsInOneCycle);
    }

    /**
     *
     * Given a specific event in a cycle, this method returns all the dates in the period of repetition of the specific
     * event (don't include original event in this.eventsInOneCycle).
     */

    public ArrayList<Event> createEventInCycles(Event event){
        ArrayList<Event> result = new ArrayList<>();
        int indexOfEvent = this.eventsInOneCycle.indexOf(event);
        ArrayList<Event> listOfDatesInCycles = this.listOfDatesInCycles();
        int cyclesLength = listOfDatesInCycles.size();
        int i = 1;
        while(indexOfEvent + this.getCycleLength()*i < cyclesLength){
            result.add(listOfDatesInCycles.get(indexOfEvent + this.getCycleLength()*i));
            i += 1;
        }
        return result;
    }

    /**
     *
     * The listOfDatesInCyclesForSpecificEvent returns all the dates in a period of repetition for a specific event.
     * This method creates events with those dates. Looping through the events in this.eventsInOneCycle will
     * create all the events in the period of repetition. (Temporary: ids parameter until I ask group how they plan to
     * get ids)
     * Assumptions: the start and end time of an event are in the same year, month and day (to remedy this, need to
     * change the listOfDatesInCycles method to return an arrayList of 2 element array (the 2 elements are the
     * start time and end time)).
     */

//    public ArrayList<Event> createEventInCycles(Event event){
//        String name = event.getName();
//        ArrayList<Event> result = new ArrayList<>();
//        ArrayList<LocalDateTime> dateList = this.listOfDatesInCyclesForSpecificEvent(event);
//        int i = 1;
//        for(LocalDateTime date : dateList){
//            LocalDateTime eventStartDate = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(),
//                    event.getStartTime().getHour(), event.getStartTime().getMinute());
//            Event event1 = new Event(ConstantID.get(), name + "-" + i, eventStartDate, date);
//            i ++;
//            result.add(event1);
//        }
//        return result;
//    }
//

}
