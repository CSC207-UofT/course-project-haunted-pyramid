package entities;

import java.io.Serializable;
import java.util.*;

/**
 * @author Malik Lahlou
 * @author Taite Cullen
 * @author Sebin Im
 */

public class User implements Serializable {
    private UUID id;
    private String name;
    private String username;
    private String password;
    private List<Event> events;
    private UserPreferences userPreferences;
    private Map<UUID, Category> categories;
    private Map<UUID, List<Event>> suggestions;


    public User(UUID id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.events = new ArrayList<>();
        this.userPreferences = new UserPreferences();
        categories = new HashMap<>();
        suggestions = new HashMap<>();
    }

    /**
     * Two Users are considered equal if all attributes have the same value.
     *
     * @param o the object event is being compared to
     * @return whether it is equal to this object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) &&
                Objects.equals(username, user.username) && Objects.equals(password, user.password) &&
                Objects.equals(events, user.events) && Objects.equals(userPreferences, user.userPreferences) &&
                Objects.equals(categories, user.categories) && Objects.equals(suggestions, user.suggestions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, username, password, events, userPreferences, categories, suggestions);}

    /**
     * Getter methods.
     */
    public UUID getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }
    public UserPreferences getUserPreferences(){
        return this.userPreferences;
    }
    public List<Event> getEvents() {
        return this.events;
    }

    /**
     * Setter methods.
     */
    public void setId(UUID id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
