package presenters.MenuStrategies;

import java.util.ArrayList;
import java.util.List;

public class BasicMenuContent implements MenuContent {

    @Override
    public List<String> getContent() {
        return new ArrayList<>(){{
            add("1. Profile Setting");
            add("2. View Calendar by Type and Date");
            add("3. Add a new Event");
            add("4. Modify an existing Event through Calendar");
            add("5. Log Out");
            add("6. Exit");
        }};
    }

}
