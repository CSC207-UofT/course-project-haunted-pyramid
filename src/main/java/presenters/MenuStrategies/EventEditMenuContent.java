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
            add("1. change start date (or create start date if start date is null)");
            add("2. change start time (or create start time if start time is null");
            add("3. change end date");
            add("4. change end time");
            add("5. change description");
            add("6. change name");
            add("7. recurse");
            add("8. prep");
            add("9. delete");
            add("10. save");
        }});
        return content;
    }
}
