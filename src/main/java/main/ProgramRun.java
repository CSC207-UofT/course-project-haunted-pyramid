package main;

import controllers.MainController;

public class ProgramRun {
    /**
     * Main run function that launches the calendar program.
     */
    public static void main(String[] args) {
        MainController controller = new MainController();
        controller.displayInitScreen();
        System.out.println(controller.displayCalendar());
        controller.generateEvent();
        System.out.println(controller.displayCalendar());
        controller.saveAndExitProgram();
    }
}

