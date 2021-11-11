package presenters.MenuStrategies;

import usecases.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileMenuContent implements MenuContent{
    private final UUID user;
    private final UserManager userManager;

    public ProfileMenuContent(UUID user, UserManager userManager){
        this.user = user;
        this.userManager = userManager;
    }

    @Override
    public List<String> getContent() {
        ArrayList<String> currentSettings = new ArrayList<>();
        currentSettings.add(this.userManager.getUserInfo().get(user).getName());
        currentSettings.add("free time: " + this.userManager.getUserInfo().get(user).getFreeTime().toString());
        currentSettings.add("procrastinate: " + this.userManager.getUserInfo().get(user).getProcrastinate());
        currentSettings.addAll(new ArrayList<>(){{
            add("change name");
            add("add free time");
            add("remove free time");
            add("toggle procrastinate");
            add("done");
        }});
        return currentSettings;
    }
}
