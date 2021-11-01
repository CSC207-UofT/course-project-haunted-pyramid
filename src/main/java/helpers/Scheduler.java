package helpers;

import java.util.Map;

public interface Scheduler {
    Integer[][] getSchedule(Map<String, String> rules);
}
