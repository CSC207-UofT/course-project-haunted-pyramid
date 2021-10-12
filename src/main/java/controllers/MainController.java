package controllers;

import gateways.IOSerializable;
import java.util.Scanner;

public class MainController {

    private IOSerializable ioSerializable;
    private Scanner scanner = new Scanner(System.in);

    public MainController() {
        //Instantiation of the IOSerializable
        this.ioSerializable = new IOSerializable();

        if (ioSerializable.hasSaveData()) {
            System.out.println("read from existing data");
        } else {
            System.out.println("create new files to contain data");
        }
        //TODO: Implement the rest of this method.
    }

    /**
     * Display the main screen. Used at and only at launch.
     */
    public void displayScreen() {
        //TODO: Implement this method using presenter. Make sure it overrides so the controller does not print anything.
    }

}
