//package entities;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class Category {
//
//    private int id;
//    private String name;
//    private ArrayList<String> typesInCategory;
//    private HashMap<String, ArrayList<Event>> eventMap;
//
//    public Category(int id, String name, Event event){
//        this.id = id;
//        this.name = name;
//        this.typesInCategory = new ArrayList<>(){{add(event.getType());}};
//        this.eventMap = new HashMap<>();
//        ArrayList<Event> eventList = new ArrayList<>();
//        eventList.add(event);
//        this.eventMap.put(event.getType(), eventList);
//    }
//
//    public int getId() {return id;}
//    public String getName() {return name;}
//    public ArrayList<String> getTypesInCategory() {return typesInCategory;}
//    public HashMap<String, ArrayList<Event>> getEventMap() {return eventMap;}
//
//    public void addTypeToCategory(String type){this.typesInCategory.add(type);}
//    public void addEvent(Event event){
//        if(this.typesInCategory.contains(event.getType())){
//            this.eventMap.get(event.getType()).add(event);
//        }
//        else{
//            ArrayList<Event> eventList = new ArrayList<>();
//            eventList.add(event);
//            this.eventMap.put(event.getType(), eventList);
//        }
//    }
//
//    public void removeEvent(Event event){
//        this.eventMap.get(event.getType()).remove(event);
//    }
//
//    public Event getEvent(String name){
//        // TODO: return the event in the eventMap with this unique name.
//        //  Search for keys in eventMap then for events in the value for that key.
//        return null;
//    }
//
//    public Event getEvent(String type, String name){
//        for(Event event : this.eventMap.get(type)){
//            if(name.equals(event.getName())){
//                return event;
//            }
//        }
//        return null;
//    }
//}
