package usecases;

import entities.User;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private final HashMap<UUID, User> userInfo;

    public UserManager(ArrayList<User> users) {
        this.userInfo = new HashMap<>();
        for(User user : users) {
            this.userInfo.put(user.getId(), user);
        }
    }

    public UserManager(){
        this.userInfo = new HashMap<>();
    }

    public void toggleProcrastinate(UUID user){
        this.userInfo.get(user).setProcrastinate(!userInfo.get(user).getProcrastinate());
    }

    public boolean getProcrastinate(UUID user){return this.userInfo.get(user).getProcrastinate();}

    public void setName(UUID user, String name){
        this.userInfo.get(user).setName(name);
    }

    public String getName(UUID user){
        return this.userInfo.get(user).getName();
    }

    public Map<LocalTime, LocalTime> getFreeTime(UUID user){
        return this.userInfo.get(user).getFreeTime();
    }

    public void addFreeTime(UUID user, LocalTime start, LocalTime end){this.userInfo.get(user).setFreeTime(start, end);}
    public void removeFreeTime(UUID user, LocalTime start){
        if (this.getFreeTime(user).containsKey(start)){
            this.userInfo.get(user).removeFreeTime(start);
        }
    }

    public void addNewUser(UUID id, String name, String username, String password) {
        User user = new User(id, name, username, password);
        this.userInfo.put(id, user);
    }

    public User checkUsernameAndPassword(String username, String password) {
        for (User user : this.getAllUsers()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(this.userInfo.values());
    }

    public HashMap<UUID, User> getUserInfo() {
        return this.userInfo;
    }
}
