package presenters.MenuStrategies;

import interfaces.MenuContent;
import usecases.events.EventManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Malik Lahlou
 */

public class RecursionEditMenuContent implements MenuContent {

    @Override
    public List<String> getContent() {
        return new ArrayList<>(new ArrayList<>() {{
            add("");
            add("1. Create a new recursion");
            add("2. Modify a recursion");
            add("3. Delete a recursion");
        }});
    }
}
