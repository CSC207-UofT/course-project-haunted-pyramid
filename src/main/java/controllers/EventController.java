package controllers;
import usecases.CalendarManager;
import usecases.EventManager;
import java.util.Scanner;
import java.util.UUID;

public class EventController {

    private EventManager eventManager;
    private CalendarManager calendarManager;
    private Scanner scanner = new Scanner(System.in);

    public EventController(EventManager eventManager, CalendarManager calendarManager){
        this.eventManager = eventManager;
        this.calendarManager = calendarManager;

    }

    public void schedule(){
        System.out.println("Enter 'test' to add test, 'assignment' to add assignment, 'lecture' to add lecture " +
                "[please enter test]"); //TODO after phase 0, more than just tests
        String type = scanner.nextLine();
        if (type.equalsIgnoreCase("test")){
            System.out.println("enter course name");
            String course = scanner.nextLine();
            System.out.println("enter title for event");
            String title = scanner.nextLine();
            System.out.println("enter date of event (start) (YYYY-MM-DD-HH-MM)");
            String date = scanner.nextLine();
            System.out.println("enter end time of event (HH-MM");
            String endTime = scanner.nextLine();
            String[] dateParts = date.split("-");
            String[] timeParts = endTime.split("-");
            this.eventManager.addEvent(type, title, Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]),
                    Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[3]),  Integer.parseInt(dateParts[4]),
                    Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
            this.calendarManager.addToCalendar(eventManager.getEvent(title));
        }
        else{
            System.out.println("please check spelling and try again!");
        }

    }
}
