package main;

import com.dropbox.core.DbxException;
import controllers.MainController;

public class ProgramRun {
    /**
     * Main run function that launches the calendar program.
     */
    public static void main(String[] args) {
        MainController controller = new MainController();

        controller.displayScreen();
    }
}

