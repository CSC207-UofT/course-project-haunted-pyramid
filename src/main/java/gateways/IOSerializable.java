package gateways;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.FullAccount;
import java.util.logging.*;
import java.util.*;

import entities.Event;
import entities.User;

/**
 * This class will allow (de)serialization of files.
 */
public class IOSerializable {
    // Logging
    private static final Logger logger = Logger.getLogger(IOSerializable.class.getPackage().getName());

    // Filepaths for each data being serialized
    private static final String EVENTS_FILEPATH = "events.ser";
    private static final String USERS_FILEPATH = "users.ser";

    // A security token needed to access the Dropbox application
    private static final String ACCESS_TOKEN = "EfBUX9G7zxkAAAAAAAAAAaXr-kGtiOL1cwBhwIe7BcI0hvt-uH5LBsEh4FXJ31Ry";

    // A public Dropbox link where the serialized files are stored
    private static final String eventsURL = "https://www.dropbox.com/s/eb7pk7h533kcoqs/events.ser?dl=1";
    private static final String usersURL = "https://www.dropbox.com/s/qjwhf6s6zuw9w14/users.ser?dl=1";

    public IOSerializable() {
        readFromDropbox();
    }

    public void readFromDropbox() {
        // Download from public Dropbox repository these two files, and save them in the directory temporarily.
        try {
            URL eventsDownload = new URL(eventsURL);
            URL usersDownload = new URL(usersURL);
            ReadableByteChannel eventsReadableByteChannel = Channels.newChannel(eventsDownload.openStream());
            ReadableByteChannel usersReadableByteChannel = Channels.newChannel(usersDownload.openStream());
            FileOutputStream eventsFileOutputStream = new FileOutputStream(EVENTS_FILEPATH);
            FileOutputStream usersFileOutputStream = new FileOutputStream(USERS_FILEPATH);
            eventsFileOutputStream.getChannel().transferFrom(eventsReadableByteChannel, 0, 1 << 24);
            usersFileOutputStream.getChannel().transferFrom(usersReadableByteChannel, 0, 1 << 24);
            eventsFileOutputStream.close();
            usersFileOutputStream.close();
            eventsReadableByteChannel.close();
            usersReadableByteChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToDropbox() {
        // Create Dropbox Client
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/Sebin").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        // Check current account info is "Sebin Im"
        FullAccount account;
        try {
            account = client.users().getCurrentAccount();
            assert account.getName().getDisplayName().equalsIgnoreCase("Sebin Im");
        } catch (DbxException eDBX) {
            logger.log(Level.SEVERE, "Dropbox raised an exception.", eDBX);
        }

        // Upload serializable files to Dropbox
        try {
            InputStream eventsInputStream = new FileInputStream(EVENTS_FILEPATH);
            InputStream usersInputStream = new FileInputStream(USERS_FILEPATH);
            FileMetadata eventsMetadata = client.files().uploadBuilder("/" + EVENTS_FILEPATH).
                    uploadAndFinish(eventsInputStream);
            FileMetadata usersMetadata = client.files().uploadBuilder("/" + USERS_FILEPATH).
                    uploadAndFinish(usersInputStream);
        } catch (FileNotFoundException eFNF) {
            logger.log(Level.SEVERE, "Cannot find file.", eFNF);
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform serialization.", eIO);
        } catch (DbxException eDBX) {
            logger.log(Level.SEVERE, "Dropbox raised an exception.", eDBX);
        }
    }

    /**
     * Checks if the user has save files for all supported data types.
     * Returns true if and only if all data types are saved.
     * @return A boolean whether the user has save files.
     */
    public boolean hasSavedData() {
        List<String> paths = List.of(EVENTS_FILEPATH, USERS_FILEPATH);
        for (String path : paths) {
            if (!new File(path).exists()) return false;
        }
        return true;
    }

    public ArrayList<Event> eventsReadFromSerializable() {
        try {
            InputStream file = new FileInputStream(EVENTS_FILEPATH);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            //Please refer to specifications for explanation
            ArrayList<Event> recoveredEvents = (ArrayList<Event>) input.readObject();
            input.close();
            return recoveredEvents;
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform deserialization. Returning new blank Arraylist.", eIO);
            return new ArrayList<>();
        } catch (ClassNotFoundException eCNF) {
            logger.log(Level.SEVERE, "Cannot find class. Returning new blank Arraylist.", eCNF);
            return new ArrayList<>();
        }
    }

    public void eventsWriteToSerializable(List<Event> events) {
        try {
            OutputStream file = new FileOutputStream(EVENTS_FILEPATH);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(events);
            output.close();
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform serialization", eIO);
        }
    }

    public ArrayList<User> usersReadFromSerializable() {
        try {
            InputStream file = new FileInputStream(USERS_FILEPATH);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            //Please refer to specifications for explanation
            ArrayList<User> recoveredUsers = (ArrayList<User>) input.readObject();
            input.close();
            return recoveredUsers;
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform deserialization. Returning new blank Arraylist.", eIO);
            return new ArrayList<>();
        } catch (ClassNotFoundException eCNF) {
            logger.log(Level.SEVERE, "Cannot find class. Returning new blank Arraylist.", eCNF);
            return new ArrayList<>();
        }
    }

    public void usersWriteToSerializable(List<User> users) {
        try {
            OutputStream file = new FileOutputStream(USERS_FILEPATH);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(users);
            output.close();
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform serialization", eIO);
        }
    }
}
