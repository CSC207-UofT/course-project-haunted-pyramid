package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Course {

    private final String[] courseRelatedEvents = new String[]{"Assigment", "Reading", "Test", "Project"};
    private String name;
    private HashMap<String, ArrayList<Event>> taskList;

    public Course(String name) {
        this.name = name;
        ArrayList<Event> eventList = new ArrayList<>();
        for(String eventName : courseRelatedEvents){
            this.taskList = new HashMap<>();
            this.taskList.put(eventName, eventList);
        }
        this.taskList.put("Others", eventList);
    }

    public boolean addEvent(Event event){
        Event[] thisEvent;
        if (this.taskList.containsKey(event.getName())){
            this.taskList.get(event.getName()).add(event);
        }
        else{
            this.taskList.get("Others").add(event);
        }
        return true;
    }

    public boolean removeTask(Event event){
        if (this.taskList.containsKey(event.getName())){
            this.taskList.get(event.getName()).remove(event);
        }
        else{
            this.taskList.get("Others").remove(event);
        }
        return true;
    }


    public String getName() {
        return name;
    }
    public HashMap<String, ArrayList<Event>> getTaskList() {
        return taskList;
    }
    public ArrayList<Event> getTask(String taskName) {
        return taskList.get(taskName);
    }
}
