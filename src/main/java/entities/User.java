package entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * @author Malik Lahlou
 * @author Taite Cullen
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
    private boolean procrastinate;
    private Map<LocalTime, LocalTime> freeTime;

    public User(UUID id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.events = new ArrayList<>();
        this.procrastinate = false;
        this.freeTime = new HashMap<>();
        this.freeTime.put(LocalTime.of(9, 0), LocalTime.of(21, 0));
    }

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

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getHomeAddress() {
        return this.homeAddress;
    }

    public ArrayList<Event> getEvents() {
        return this.events;
    }

    public boolean getProcrastinate() {
        return this.procrastinate;
    }

    public Map<LocalTime, LocalTime> getFreeTime() {
        return this.freeTime;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public void setProcrastinate(boolean procrastinate) {
        this.procrastinate = procrastinate;
    }

    public void setFreeTime(LocalTime start, LocalTime end) {
        this.freeTime.put(start, end);
    }

    public void removeFreeTime(LocalTime start) {
        this.freeTime.remove(start);
    }


}
