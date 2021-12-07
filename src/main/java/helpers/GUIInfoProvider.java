package helpers;

import controllers.EventController;
import interfaces.EventInfoGetter;

import java.time.LocalDateTime;
import java.util.UUID;

public class GUIInfoProvider {
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

    public String getEventEndInfo(UUID eventID, EventInfoGetter eventInfoGetter) {
        StringBuilder result = new StringBuilder();
        LocalDateTime endInfo = eventInfoGetter.getEnd(eventID);
        result.append(endInfo.toLocalDate());
        result.append(" ").append(endInfo.toLocalTime());
        return result.toString();
    }
}
