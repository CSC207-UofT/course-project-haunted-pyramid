package controllers;
import entities.Assignment;
import entities.Event;
import entities.Lecture;
import entities.Test;
import usecases.CalendarManager;
import usecases.EventManager;
import java.util.*;

import presenters.CalendarPresenter; // JUST FOR THE DEMONSTRATION
import usecases.SessionManager;

public class EventController {
    private final RecursiveController recursiveController;
    private final SessionController sessionController;
    private final EventManager eventManager;
    private final CalendarManager calendarManager;
    private final SessionManager sessionManager;
    private final Scanner scanner = new Scanner(System.in);
    private final CalendarPresenter calendarPresenter;
    private static Integer nextID = 0;

    public EventController(EventManager eventManager, CalendarManager calendarManager, SessionManager sessionManager){
        this.eventManager = eventManager;
        this.calendarManager = calendarManager;
        this.sessionManager = sessionManager;
        this.calendarPresenter = new CalendarPresenter(this.calendarManager);
        this.recursiveController = new RecursiveController();
        this.sessionController = new SessionController(sessionManager);

    }
    public void schedule(){
        String type = IOController.getEventType();
        Integer ID = nextID;
        nextID += 1;
        Set<Event> changes;
        String title = IOController.getTitle();
        String course = IOController.getCourse();
        //TODO get unique ID from previous serializable, this is temporary for testing :D
        if (type.equalsIgnoreCase("test")){
            changes = this.addTest(ID, title, course);
        }
        if (type.equalsIgnoreCase("assignment")){
            changes = this.addAssignment(ID, title, course);
        }
        if (type.equalsIgnoreCase("lecture")){
            changes = this.addLecture(ID, title, course);
        }
        else{
            System.out.println("please check spelling and try again!");
        }
        this.calendarManager.addToCalendar(eventManager.getEvent(ID));
        // THIS JUST FOR THE TESTING. WILL BE SEPARATED IN THE FUTURE
//        System.out.println(this.calendarPresenter.showMonthCalendar(Integer.parseInt(dateParts[0]),
//                Integer.parseInt(dateParts[1])));
    }

    public Set<Event> addTest(Integer ID, String title, String course){
        List<Integer> date = IOController.getDate("date of the test");
        List<Integer> time1 = IOController.getTime("start time of the test");
        List<Integer> time2 = IOController.getTime("End time of the test");
        this.eventManager.newTest(ID, title, date.get(0), date.get(1), date.get(2), time1.get(0), time2.get(0),
                time1.get(1), time2.get(1));
        return this.sessionController.schedule(this.eventManager.getEvent(ID));
    }
    public Set<Event> addAssignment(Integer ID, String title, String course){
        List<Integer> date = IOController.getDate("due date");
        List<Integer> time1 = IOController.getTime("time of due date");
        this.eventManager.newAssignment(ID, title, course, date.get(0), date.get(1), date.get(2), time1.get(0),
                time1.get(1));
        return this.sessionController.schedule(this.eventManager.getEvent(ID));
    }
    public Set<Event> addLecture(Integer ID, String title, String course){
        List<Integer> date = IOController.getDate("date of first lecture");
        List<Integer> time1 = IOController.getTime("start time of first lecture");
        List<Integer> time2 = IOController.getTime("End time of first lecture");
        this.eventManager.newLecture(ID, title, course, date.get(0), date.get(1), date.get(2), time1.get(0), time2.get(0),
                time1.get(1), time2.get(1));
        Set<Event> changes = this.recursiveController.schedule(this.eventManager.getEvent(ID), this.eventManager);
        changes.addAll((this.sessionController.schedule(this.eventManager.getEvent(ID))));
        return changes;
    }

}
