package entities;

import java.io.Serializable;
import java.time.LocalDate;
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
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
    private String homeAddress;
    private ArrayList<Event> events;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) &&
                Objects.equals(username, user.username) && Objects.equals(password, user.password) &&
                Objects.equals(birthDate, user.birthDate) && Objects.equals(email, user.email) &&
                Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(homeAddress, user.homeAddress) &&
                Objects.equals(events, user.events) && Objects.equals(userPreferences, user.userPreferences) &&
                Objects.equals(categories, user.categories) && Objects.equals(suggestions, user.suggestions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, username, password, birthDate, email, phoneNumber, homeAddress, events,
                userPreferences, categories, suggestions);
    }

    public UUID getId() {
        return this.id;
    }

    public Map<UUID, List<Event>> getSuggestions() {return suggestions;}

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void createCategory(String name){
        Category category = new Category(UUID.randomUUID(), name, this);
        this.categories.put(category.getId(), category);
        suggestions.put(category.getId(), new ArrayList<>());
    }

    public void deleteCategory(UUID uuid, User replacement){
        this.categories.get(uuid).removeUser(this, replacement);
        this.categories.remove(uuid);
    }

    public Map<Event, Boolean> respondToSuggestionsInCategory(Category category, List<Boolean> decisions){
        List<Event> categorySuggestions = this.suggestions.get(category.getId());
        Map<Event, Boolean> result = new HashMap<>();
        int i = 0;
        for(Boolean decision : decisions){
            result.put(categorySuggestions.get(i), decision);
            i++;
        }
        return result;
    }

    public void addOrNotSuggestionsToCategory(Category category, List<Boolean> decisions){
        Map<Event, Boolean> suggestionMap = respondToSuggestionsInCategory(category, decisions);
        for(Event event : suggestionMap.keySet()){
            if(suggestionMap.get(event)){
                this.categories.get(category.getId()).addEvent(event);
            }
        }
    }




    public ArrayList<Event> getEvents() {
        return this.events;
    }



    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Need this later to allow change of username of user
    public void setUsername(String username) {
        this.username = username;
    }
    // Need this later to allow change of password of user
    public void setPassword(String password) {
        this.password = password;
    }

    public void addToSuggestions(Event event, Category category){
        this.getSuggestions().get(category.getId()).add(event);
    }

    public Map<UUID, Category> getCategories() {
        return categories;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public UserPreferences getUserPreferences(){
        return this.userPreferences;
    }
}
