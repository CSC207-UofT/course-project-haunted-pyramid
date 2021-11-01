package usecases;

import entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class UserManager {

    private final HashMap<UUID, User> userInfo;

    public UserManager(ArrayList<User> users) {
        this.userInfo = new HashMap<>();
        for(User user : users) {
            this.userInfo.put(user.getId(), user);
        }
    }

    public void addNewUser(UUID id, String name, String username, String password) {
        User user = new User(id, name, username, password);
        this.userInfo.put(id, user);
    }

    public boolean checkUsernameAndPassword(String username, String password) {
        for (User user : this.getAllUsers()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(this.userInfo.values());
    }

    public HashMap<UUID, User> getUserInfo() {
        return this.userInfo;
    }
}
