package entities;

import java.util.*;

/**
 * @author Malik Lahlou
 */


public class Category {

    private UUID id;
    private String name;
    private List<Event> events;
    private List<User> adminUsers;
    private List<User> regularUsers;

    public Category(UUID id, String name, User adminUser){
        this.id = id;
        this.name = name;
        this.adminUsers = new ArrayList<>();
        this.adminUsers.add(adminUser);
        regularUsers = new ArrayList<>();
    }

    public UUID getId() {return id;}
    public String getName() {return name;}
    public List<Event> getEvents() {return events;}
    public List<User> getAdminUsers() {return adminUsers;}

    public void setName(String name) {this.name = name;}
    public void setEvents(List<Event> events) {this.events = events;}
    public void setId(UUID id) {this.id = id;}
    public void setRegularUsers(List<User> users) {this.regularUsers = users;}

    public void removeEvent(Event event){
        this.events.remove(event);
        event.removeCategory(this.name);
    }
    public void addEvent(Event event){this.events.add(event);
    event.addToCategory(name);}

    public void removeUser(User user){
        if(this.adminUsers.contains(user)){
            this.adminUsers.remove(user);
        }
        else{
            this.regularUsers.remove(user);
        }
    }


}
