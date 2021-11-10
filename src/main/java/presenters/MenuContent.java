package presenters;

import helpers.Constants;

import java.util.ArrayList;
import java.util.List;

public class MenuContent {
    public void addBasicMenuContent(StringBuilder result){
        List<String> menuList = new ArrayList<>(){{
            add("1. Profile Setting");
            add("2. View Events by Course");
            add("3. Add a new Event");
            add("4. Remove an Event");
            add("5. Modify an existing Event");
        }};
        for (String item : menuList){
            result.append("\n").append("|").append(" ");
            String filler = " ".repeat(Constants.MENU_DIVIDER - item.length() - 1);
            result.append(item).append(filler).append("|");
        }
    }

    public void addEventTypeMenuContent(StringBuilder result){
        List<String> menuList = new ArrayList<>(){{
            add("1. Test");
            add("2. Assignment");
            add("3. Lecture");
            add("4. Lab");
            add("5. Tutorial");
            add("6. Personal");
        }};
        for (String item : menuList){
            result.append("\n").append("|").append(" ");
            String filler = " ".repeat(Constants.MENU_DIVIDER - item.length() - 1);
            result.append(item).append(filler).append("|");
        }
    }
}
