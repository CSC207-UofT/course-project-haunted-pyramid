package main;

import controllers.MainController;

public class ProgramRun {
    /**
     * Main run function that launches the calendar program.
     * @param args
     */
    public static void main(String[] args) {
        MainController controller = new MainController();
        controller.displayScreen();
    }
}

