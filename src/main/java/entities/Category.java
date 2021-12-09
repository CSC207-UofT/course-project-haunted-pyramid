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

    /**
     * Constructor for a Category.
     *
     * @param id the uuid of this category
     * @param name the name of this category
     * @param adminUser the user who created this category.
     */
    public Category(UUID id, String name, User adminUser){
        this.id = id;
        this.name = name;
        this.events = new ArrayList<>();
        this.adminUsers = new ArrayList<>();
        this.adminUsers.add(adminUser);
        this.regularUsers = new ArrayList<>();
    }

    /**
     * Getter methods.
     */
    public UUID getId() {return id;}
    public String getName() {return name;}
    public List<Event> getEvents() {return events;}
    public List<User> getAdminUsers() {return adminUsers;}
    public List<User> getRegularUsers() {return regularUsers;}

    /**
     * Setter methods.
     */
    public void setName(String name) {this.name = name;}
    public void setEvents(List<Event> events) {this.events = events;}
    public void setId(UUID id) {this.id = id;}
    public void addRegularUser(User user) {this.regularUsers.add(user);}

    /**
     * This method removes a regular user from the list
     *
     * @param user the regular user to remove
     */
    private void removeRegularUser(User user){
            this.regularUsers.remove(user);
        }

    /**
     * This method removes an admin user from the list, if there is one admin, it chooses a random regular user and
     * render it an admin. If there are no regular users, this category is just deleted.
     *
     * @param user admin user to delete
     * @param regularToBeAdminUser the regular user which can be chosen to become the admin in the event the user to
     * remove is the only admin.
     */
    private void removeAdmin(User user, User regularToBeAdminUser){
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

    /**
     * Removes the user based on whether it is an admin or not.
     *
     * @param user admin user to delete
     * @param regularToBeAdminUser the regular user which can be chosen to become the admin in the event the user to
     * remove is the only admin.
     */
    public void removeUser(User user, User regularToBeAdminUser){
        if(this.adminUsers.contains(user)){
            this.removeAdmin(user, regularToBeAdminUser);
        }
        else{
            this.removeRegularUser(user);
        }
    }
}
