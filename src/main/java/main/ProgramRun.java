package main;

import controllers.MainController;

/**
 * The class that boots the program.
 * It should contact MainController and MainController only.
 *
 * @author Sebin Im
 */

public class ProgramRun {
    /**
     * Main run function that launches the calendar program.
     */
    public static void main(String[] args) {
        MainController controller = new MainController();
        controller.displayInitScreen();
        controller.displayScreen();
    }
}

