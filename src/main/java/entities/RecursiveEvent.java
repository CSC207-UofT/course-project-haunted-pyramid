//package entities;
//
//import interfaces.DateGetter;
//
//import javax.lang.model.element.NestingKind;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//
//public class RecursiveEvent{
//
//    // + eventsInOneCycle ;for example if a lecture is tuesdays and thurdays of each week, the cycle will consist
//    // of [(tuesday, date+time of lecture), (thursday, date+time of lecture), (tuesday, date+time of lecture after the
//    // thursday one)]. If an event repeats 3 times each 2 week, the list would have 3 elements with the days and
//    // times of the three occurrences of the event.
//    // methodToGetDate: indicates which method the user want to handle repetitions. For now, the user can either input
//    // the number of times a cycle repeats, or the two dates in between the cycle repeats.
//
//    private Integer id;
//    private ArrayList<Event> eventsInOneCycle;
//    private DateGetter methodToGetDate;
//
//    public RecursiveEvent(Integer id, ArrayList<Event> events, DateGetter methodToGetDate){
//        this.id = id;
//        this.eventsInOneCycle = new ArrayList<>();
//        for(Event event : events){
//            this.eventsInOneCycle.add(event);
//            event.setRecursiveId(id);
//        }
//        this.methodToGetDate = methodToGetDate;
//    }
//
//    public Integer getId() {return id;}
//    public ArrayList<Event> getEventsInOneCycle() {return eventsInOneCycle;}
//    public DateGetter getMethodToGetDate() {return methodToGetDate;}
//    public int getCycleLength(){
//        return eventsInOneCycle.size() -1;
//    }
//
//    public void setEventsInOneCycle(ArrayList<Event> eventsInOneCycle) {
//        this.eventsInOneCycle = eventsInOneCycle;
//    }
//    public void setMethodToGetDate(DateGetter methodToGetDate) {this.methodToGetDate = methodToGetDate;}
//
//    public ArrayList<LocalDateTime> listOfDatesInCycles(){
//        return methodToGetDate.listOfDatesInCycles(this.eventsInOneCycle);
//    }
//
//    public ArrayList<LocalDateTime> listOfDatesInCyclesForSpecificEvent(Event event){
//        ArrayList<LocalDateTime> result = new ArrayList<>();
//        int indexOfEvent = this.eventsInOneCycle.indexOf(event);
//        ArrayList<LocalDateTime> listOfDatesInCycles = this.listOfDatesInCycles();
//        int cyclesLength = listOfDatesInCycles.size();
//        int i = 1;
//        while(indexOfEvent + this.getCycleLength()*i < cyclesLength){
//            result.add(listOfDatesInCycles.get(indexOfEvent + this.getCycleLength()*i));
//            i += 1;
//        }
//        return result;
//    }
//
//    public ArrayList<Event> createEventInCycles(Event event){
//        int id = event.getID(); // same id for now
//        String name = event.getName();
//        String type = event.getType();
//        String categoryName = event.getCategoryType();
//        ArrayList<Event> result = new ArrayList<>();
//        ArrayList<LocalDateTime> dateList = this.listOfDatesInCyclesForSpecificEvent(event);
//        for(LocalDateTime date : dateList){
//            Event event1 = new Event(id, name, type, date, categoryName, "");
//            if(event.getStartTime() != null){
//                LocalDateTime eventStartDate = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(),
//                        event.getStartTime().getHour(), event.getStartTime().getMinute());
//                event1.setStartTime(eventStartDate);
//            }
//            result.add(event1);
//        }
//        return result;
//    }
//
//}
