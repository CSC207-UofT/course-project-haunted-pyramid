package helpers;

import entities.Event;
import usecases.events.EventManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Converts UUID into integer ID for better viewing experience
 * @author Seo Won Yi
 */

public class EventIDConverter {
    private final Map<Integer, UUID> eventID;

    /**
     * Create a map that links integers to the UUID of events
     * @param eventManager EventManager object to get event information from
     */
    public EventIDConverter(EventManager eventManager) {
        Map<Integer, UUID> tempMap = new HashMap<>();
        List<Event> eventList = eventManager.getAllEventsFlatSplit();
        for (int i = 1; i <= eventList.size(); i++) {
            tempMap.put(i, eventManager.getDefaultEventInfoGetter().getID(eventList.get(i - 1)));
        }
        this.eventID = tempMap;
    }

    /**
     * Using the mapping, get UUID that corresponds to the given integer option
     * @param option integer choice
     * @return null if option is not a key, otherwise return a corresponding value (UUID)
     */
    public UUID getUUIDFromInt(int option) {
        return this.eventID.getOrDefault(option, null);
    }

    /**
     * Using the mapping, get integer that corresponds to the given UUID option
     * @param option UUID choice
     * @return 0 if option is not a key, otherwise return a corresponding value (Integer)
     */
    public int getIntFromUUID(UUID option) {
        for (int key : this.eventID.keySet()) {
            if (this.eventID.get(key).equals(option)) {
                return key;
            }
        }
        return 0;
    }

    public Map<Integer, UUID> getEventIDMap() {
        return this.eventID;
    }
}
