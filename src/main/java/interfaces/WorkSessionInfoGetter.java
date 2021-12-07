package interfaces;

import java.util.UUID;

public interface WorkSessionInfoGetter {
    Long getTotalHoursNeeded(UUID eventID);
    Long getEventSessionLength(UUID eventID);
}
