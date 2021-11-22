package usecases;

import entities.User;

import java.time.LocalTime;
import java.util.*;

/**
 * A class for editing User Entity classes and storing/accessing a list of Users in a map, with key= UUID user id
 *
 * @author Sebin Im
 * @author Taite Cullen
 * @author Malik Lahlou
 * @see User
 */
public class UserManager {

    private final HashMap<UUID, User> userInfo;

    /**
     * @param users a list of users to be entered into <code>this.userInfo</code>
     */
    public UserManager(List<User> users) {
        this.userInfo = new HashMap<>();
        for (User user : users) {
            this.userInfo.put(user.getId(), user);
        }
    }

    /**
     * changes value of procrastinate for user
     *
     * @param user the id of the user to be edited
     */
    public void toggleProcrastinate(UUID user) {
        this.userInfo.get(user).setProcrastinate(!userInfo.get(user).getProcrastinate());
    }

    /**
     * gets the value of procrastinate for the user (true/false)
     *
     * @param user the id of the user to be accessed
     * @return boolean procrastinate (for user)
     */
    public boolean getProcrastinate(UUID user) {
        return this.userInfo.get(user).getProcrastinate();
    }

    /**
     * gets the name of the user
     *
     * @param user the id of the user to be accessed
     * @return the name of the user
     */
    public String getName(UUID user) {
        return this.userInfo.get(user).getName();
    }

    /**
     * gets the Map with key=start time, value=end time of free time periods of the user
     *
     * @param user the id of the user to be accessed
     * @return the free time of the user Map<LocalTime, LocalTime>
     */
    public Map<LocalTime, LocalTime> getFreeTime(UUID user) {
        return this.userInfo.get(user).getFreeTime();
    }

    /**
     * adds a free time section to the users free time. if start is before end, end will be the start time
     *
     * @param user  the id of the user to be modified
     * @param start the start time of the free time slot
     * @param end   the end time of the free time slot
     */
    public void addFreeTime(UUID user, LocalTime start, LocalTime end) {
        if (start.isAfter(end)) {
            this.userInfo.get(user).setFreeTime(end, start);
        } else {
            this.userInfo.get(user).setFreeTime(start, end);
        }
    }

    /**
     * removes a free time period from the user's free time
     *
     * @param user  the id of the user to be modified
     * @param start the start time of the free time to be removed
     */
    public void removeFreeTime(UUID user, LocalTime start) {
        if (this.getFreeTime(user).containsKey(start)) {
            this.userInfo.get(user).removeFreeTime(start);
        }
    }

    /**
     * adds a new user to <code>this.userInfo</code> with key=id
     *
     * @param id       the id of the new user
     * @param name     the name of the new user
     * @param username the username of the new user
     * @param password the password of the new user
     */
    public void addNewUser(UUID id, String name, String username, String password) {
        User user = new User(id, name, username, password);
        this.userInfo.put(id, user);
    }

    /**
     * returns the user with input username and password if they exist in this UserManager
     *
     * @param username the username of the user to be checked
     * @param password the password of the user to be checked
     * @return the User with given username and password, or null
     */
    public User checkUsernameAndPassword(String username, String password) {
        for (User user : this.getAllUsers()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * @return list of all users in <code>this.userInfo</code>
     */
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(this.userInfo.values());
    }

    /**
     * @return the map with key=id, value=user in <code>this.userInfo</code>
     */
    public HashMap<UUID, User> getUserInfo() {
        return this.userInfo;
    }

    public void setName(UUID currentUser, String name) {
        this.getUserInfo().get(currentUser).setName(name);
    }
}
