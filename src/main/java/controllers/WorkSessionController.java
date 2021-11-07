package controllers;

import entities.Event;
import usecases.EventManager;
import usecases.WorkSessionScheduler;

import java.util.ArrayList;

public class WorkSessionController {

    private final WorkSessionScheduler workSessionScheduler = new WorkSessionScheduler(new ArrayList<Event>(), true, true, true);

    public void edit(Event event, EventManager eventManager){
        boolean done = false;
        while (!done){
            System.out.println(this.workSessionScheduler.sessionsString(event));
            System.out.println("to mark a work session as complete/incomplete, type 'c/ic: session #'");
            System.out.println("\nsession length: " + event.getSessionLength());
            System.out.println("\nhours needed: " + event.getHoursNeeded());
            System.out.println("to edit a field, type the field name followed by the new value [i.e. session length: 2]");
            String next = IOController.getAnswer("please enter your next request, or \ndone");
            if (next.equalsIgnoreCase("done")) {
                done = true;
            } else {
                String[] action = next.split(": ");
                if (action[0].equalsIgnoreCase("session length")) {
                    this.workSessionScheduler.setSessionLength(event, (long) Integer.parseInt(action[1]), eventManager);
                }else if (action[0].equalsIgnoreCase("hours needed")){
                    this.workSessionScheduler.setHoursNeeded(event, (long) Integer.parseInt(action[1]), eventManager);
                    System.out.println("changed hours needed");
                }
                else if (action[0].equalsIgnoreCase("c")){
                    this.workSessionScheduler.markComplete(event, action[1], eventManager);
                    System.out.println("marked c");
                }
                else if (action[0].equals("ic")){
                    this.workSessionScheduler.markInComplete(event, action[1], eventManager);
                    System.out.println("marked ic");
                }
            }
        }
    }

}
