package gateways;

//IO
import entities.Student; // TODO change this to StudentManager or higher

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
    public static final String STUDENTS_FILEPATH = "students.ser"; //should change to USERS_FILEPATH and "users.ser"
    public static final String CALENDARS_FILEPATH = "calendars.ser";

    /**
     * Checks if the user has save files for all supported data types.
     * Returns true if and only if all data types are saved.
     * @return A boolean whether the user has save files.
     */
    public boolean hasSavedData() {
        List<String> paths = List.of(STUDENTS_FILEPATH);
        //List<String> paths = Arrays.asList(EVENTS_FILEPATH, TASKS_FILEPATH, STUDENTS_FILEPATH, CALENDARS_FILEPATH)
        for (String path : paths) {
            if (!new File(path).exists()) return false;
        }
        return true;
    }

    public ArrayList<Student> studentsReadFromSerializable() {
        try {
            InputStream file = new FileInputStream(STUDENTS_FILEPATH);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            //Please refer to specifications for explanation
            ArrayList<Student> recoveredStudents = (ArrayList<Student>) input.readObject();
            input.close();
            return recoveredStudents;
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform deserialization. Returning new blank Arraylist.", eIO);
            return new ArrayList<>();
        } catch (ClassNotFoundException eCNF) {
            logger.log(Level.SEVERE, "Cannot find class. Returning new blank Arraylist.", eCNF);
            return new ArrayList<>();
        }
    }

    public void studentsWriteToSerializable(List<Student> students) {
        try {
            OutputStream file = new FileOutputStream(STUDENTS_FILEPATH);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(students);
            output.close();
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform serialization", eIO);
        }
    }
}
