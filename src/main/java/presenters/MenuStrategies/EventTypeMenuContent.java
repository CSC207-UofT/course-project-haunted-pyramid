package presenters.MenuStrategies;


import java.util.ArrayList;
import java.util.List;

public class EventTypeMenuContent implements MenuContent {

    @Override
    public List<String> getContent(){
        return new ArrayList<>(){{
            add("1. Test");
            add("2. Assignment");
            add("3. Lecture");
            add("4. Lab");
            add("5. Tutorial");
            add("6. Personal");
        }};
    }
}
