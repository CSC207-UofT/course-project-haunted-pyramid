package gateways;

//IO
import java.io.*;
//util
import java.util.*;
//logging
import java.util.logging.*;

/**
 * This class will allow (de)serialization of files.
 */
public class IOSerializable {
    //logging
    private static final Logger logger = Logger.getLogger(IOSerializable.class.getPackage().getName());

    //filepaths for each data being serialized
    public static final String EVENTS_FILEPATH = "events.ser";
    public static final String TASKS_FILEPATH = "tasks.ser";
    public static final String USERS_FILEPATH = "users.ser";
    public static final String CALENDARS_FILEPATH = "calendars.ser";

    /**
     * Checks if the user has save files for all supported data types. Returns true if and only if all data types are saved.
     * @return A boolean whether the user has save files.
     */
    public boolean hasSaveData() {
        List<String> paths = Arrays.asList(EVENTS_FILEPATH, TASKS_FILEPATH, USERS_FILEPATH, CALENDARS_FILEPATH);
        for (String path : paths) {
            if (!new File(path).exists()) return false;
        }
        return true;
    }
}
