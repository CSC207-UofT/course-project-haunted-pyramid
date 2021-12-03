package GUISwing;

import controllers.EventController;
import gateways.ICalendar;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class SaveICalendar {
    public void save(EventController eventController) {
        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("iCal Format", "ics");
        jFileChooser.setDialogTitle("Save iCalendar");
        jFileChooser.setDialogType(2);
        jFileChooser.setFileFilter(filter);
        int response = jFileChooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = new File(jFileChooser.getSelectedFile().getAbsolutePath());
            ICalendar iCalendar = new ICalendar(eventController.getEventManager());
            iCalendar.create(file.toString());
        }
    }
}
