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
        this.events = new ArrayList<>();
        this.adminUsers = new ArrayList<>();
        this.adminUsers.add(adminUser);
        this.regularUsers = new ArrayList<>();
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
        event.removeCategory(this.id);
    }
    public void addEvent(Event event){
        this.events.add(event);
        event.addToCategory(this.id);
    }

    public void removeRegularUser(User user){
            this.regularUsers.remove(user);
        }


    public void removeAdmin(User user, User regularToBeAdminUser){
        if(this.adminUsers.size() == 1 && this.regularUsers.contains(regularToBeAdminUser)){
            this.adminUsers.remove(user);
            this.regularUsers.remove(regularToBeAdminUser);
            this.adminUsers.add(regularToBeAdminUser);
        }
        else if(this.adminUsers.size() == 1 && this.regularUsers.size() > 0){
            Random random = new Random();
            int regularUserIndex = random.nextInt(this.regularUsers.size());
            this.adminUsers.remove(user);
            this.adminUsers.add(this.regularUsers.get(regularUserIndex));
            this.regularUsers.remove(regularUserIndex);
        }
        else if(this.adminUsers.size() > 1){
            this.adminUsers.remove(user);
        }
    }

    public void removeUser(User user, User regularToBeAdminUser){
        if(this.adminUsers.contains(user)){
            this.removeAdmin(user, regularToBeAdminUser);
        }
        else{
            this.removeRegularUser(user);
        }
    }








}
