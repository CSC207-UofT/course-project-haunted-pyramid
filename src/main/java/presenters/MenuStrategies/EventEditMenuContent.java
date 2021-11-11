package presenters.MenuStrategies;

import java.util.ArrayList;
import java.util.List;

public class EventEditMenuContent implements MenuContent{
    @Override
    public List<String> getContent() {
        return new ArrayList<>(){{
            add("enter field followed by new value, i.e. " +
                    "\nstart: 2002-12-05T2-30 or \nname: Elizabeth" );
            add("prep");
            add("recurse");
            add("save");
            add("delete");
        }};
    }
}
