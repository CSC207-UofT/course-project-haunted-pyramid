package presenters.MenuStrategies;

import java.util.ArrayList;
import java.util.List;

public class BasicMenuContent implements MenuContent {

    @Override
    public List<String> getContent() {
        return new ArrayList<>(){{
            add("1. Profile Setting");
            add("2. View Events by Course");
            add("3. Add a new Event");
            add("4. Remove an Event");
            add("5. Modify an existing Event");
        }};
    }

}
