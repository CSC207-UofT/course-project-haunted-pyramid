package presenters.MenuStrategies;

import entities.Event;
import interfaces.MenuContent;
import usecases.events.EventManager;

import java.util.ArrayList;
import java.util.List;

public class EventEditMenuContent implements MenuContent {
    private Event event;
    public EventEditMenuContent(Event event){
        this.event = event;
    }
    @Override
    public List<String> getContent() {
        ArrayList<String> content = new ArrayList<>();
        EventManager em = new EventManager(new ArrayList<>());
        content.addAll(List.of(em.displayEvent(this.event).split("\n")));
        content.addAll(new ArrayList<>(){{
            add("");
            add("1. Add/Change Start Date");
            add("2. Add/Change Start Time");
            add("3. Change End Date");
            add("4. Change End Time");
            add("5. Add/Change Description");
            add("6. Change Name of the Event");
            add("7. Prepare by Creating Work Sessions");
            add("8. Add this event to an existing recursion");
            add("9. Remove the Event");
            add("10. Save the Changes");
        }});
        return content;
    }
}
