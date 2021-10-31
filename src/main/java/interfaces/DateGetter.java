package interfaces;

import entities.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface DateGetter {
    ArrayList<LocalDateTime> listOfDatesInCycles(ArrayList<Event> events);
}
