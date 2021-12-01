package presenters.MenuStrategies;

import usecases.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileMenuContent implements MenuContent {
    private final UUID user;
    private final UserManager userManager;

    public ProfileMenuContent(UUID user, UserManager userManager) {
        this.user = user;
        this.userManager = userManager;
    }

    @Override
    public List<String> getContent() {
        ArrayList<String> currentSettings = new ArrayList<>();
        currentSettings.add(this.userManager.getUserInfo().get(user).getName());
        currentSettings.add("Free Time: " + this.userManager.getPreferences(user).getFreeTime().toString());
        currentSettings.add("Procrastinate: " + this.userManager.getPreferences(user).getProcrastinate());
        currentSettings.add("Spacing between work sessions: " + this.userManager.getPreferences(user).getSpacingSameDay());
        currentSettings.add("Cram: " +
                this.userManager.getPreferences(user).getCram());
        currentSettings.addAll(actualOptions());
        return currentSettings;
    }

    private List<String> actualOptions() {
        return new ArrayList<>(){{
            add("1. Change Name");
            add("2. Add Free Time");
            add("3. Remove Free Time");
            add("4. Toggle 'Procrastinate'");
            add("5. Toggle Work session spacing");
            add("6. toggle 'Cram'");
            add("7. Return to the Main Menu");
        }};
    }

    @Override
    public int numberOfOptions(){
        return actualOptions().size();
    }
}
