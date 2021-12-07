package GUISwing;

import controllers.EventController;
import gateways.ICalendar;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

/**
 * Save iCalendar file with the user specified name and directory
 * @author Seo Won Yi
 */

public class SaveICalendar {
    /**
     * save iCalendar File that contains all the event information from eventController to the specified directory and
     * name
     * @param eventController EventController object that has all the event information
     */
    public void save(EventController eventController) {
        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("iCal Format", "ics");
        jFileChooser.setDialogTitle("Save iCalendar");
        jFileChooser.setFileFilter(filter);
        int response = jFileChooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile().getAbsoluteFile();
            ICalendar iCalendar = new ICalendar(eventController.getEventManager());
            try {
                iCalendar.create(file.toString());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Please type a valid file name", "File Name Error", JOptionPane.WARNING_MESSAGE);
                save(eventController);
            }
        }
    }
}
