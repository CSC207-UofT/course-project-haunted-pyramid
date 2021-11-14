package presenters.MenuStrategies;

import entities.Event;
import usecases.events.EventManager;

import java.util.ArrayList;
import java.util.List;

public class EventEditMenuContent implements MenuContent{
    private Event event;
    public EventEditMenuContent(Event event){
        this.event = event;
    }
    @Override
    public List<String> getContent() {
        ArrayList<String> content = new ArrayList<>();
        EventManager em = new EventManager();
        content.addAll(List.of(em.displayEvent(this.event).split("\n")));
        content.addAll(new ArrayList<>(){{
            add("1. Add/Change Start Date");
            add("2. Add/Change Start Time");
            add("3. Change End Date");
            add("4. Change End Time");
            add("5. Add/Change Description");
            add("6. Change Name of the Event");
            add("7. Recurse the Event (Create Repetition)");
            add("8. Prepare by Creating Work Sessions");
            add("9. Remove the Event");
            add("10. Save the Changes");
        }});
        return content;
    }
}
