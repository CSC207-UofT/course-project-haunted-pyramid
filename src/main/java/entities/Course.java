package entities;

import java.util.ArrayList;
import java.util.HashMap;


public class Course {

    private final String name;
    private final HashMap<String, ArrayList<Event>> taskList;

    public Course(String name) {
        this.name = name;
        this.taskList = new HashMap<>();
        String[] courseRelatedEvents = new String[]{"Assigment", "Reading", "Test", "Lecture", "Tutorial"};
        for(String eventName : courseRelatedEvents){
            ArrayList<Event> eventList = new ArrayList<>();
            this.taskList.put(eventName, eventList);
        }
        ArrayList<Event> eventList = new ArrayList<>();
        this.taskList.put("Others", eventList);
    }

    public boolean addEvent(Event event){
        if (this.taskList.containsKey(event.getName())){
            this.taskList.get(event.getName()).add(event);
        }
        else{
            ArrayList<Event> eventList = this.taskList.get("Others");
            eventList.add(event);
        }
        return true;
    }

    public boolean removeEvent(Event event){
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
