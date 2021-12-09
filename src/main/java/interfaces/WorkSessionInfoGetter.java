package interfaces;

import java.util.List;
import java.util.UUID;

public interface WorkSessionInfoGetter {

    Long getTotalHoursNeeded(UUID eventID);

    Long getEventSessionLength(UUID eventID);

    Long getStartWorking(UUID eventID);

    List<UUID> getFutureWorkSessions(UUID ID);

    List<UUID> getPastWorkSessions(UUID ID);
}
