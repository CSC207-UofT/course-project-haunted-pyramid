package helpers;

import interfaces.EventInfoGetter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Provide necessary information for GUI windows to display
 * @author Seo Won Yi
 */

public class GUIInfoProvider {

    /**
     * Provide start information of an event
     * @param eventID ID of an event to consider
     * @param eventInfoGetter Interface that helps to obtain necessary information of an event
     * @return the string of event's start information
     */
    public String getEventStartInfo(UUID eventID, EventInfoGetter eventInfoGetter) {
        StringBuilder result = new StringBuilder();
        LocalDateTime startInfo = eventInfoGetter.getStart(eventID);
        if (startInfo == null) {
            return "No Start Time is Set Currently";
        }
        else {
            result.append(startInfo.toLocalDate());
            result.append(" ").append(startInfo.toLocalTime());
        }
        return result.toString();
    }

    /**
     * Provide end information of an event
     * @param eventID ID of an event to consider
     * @param eventInfoGetter Interface that helps to obtain necessary information of an event
     * @return the string of event's end information
     */
    public String getEventEndInfo(UUID eventID, EventInfoGetter eventInfoGetter) {
        StringBuilder result = new StringBuilder();
        LocalDateTime endInfo = eventInfoGetter.getEnd(eventID);
        result.append(endInfo.toLocalDate());
        result.append(" ").append(endInfo.toLocalTime());
        return result.toString();
    }
}
