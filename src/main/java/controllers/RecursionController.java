package controllers;

import entities.Event;
import usecases.EventManager;

public class RecursionController {

    public void edit(Event event, EventManager eventManager){
        boolean done = false;
        while (!done){
            //TODO: print should show
            // recursion: current details
            // end: YYYY-MM-DD
            System.out.println();
            System.out.println("e.g. weekly: mon-4:30-6:30, thu-2:30-8:30, fri-2:30-8:30");
            System.out.println("monthly: 4-17:50-19:50, 8-12:30-17:30");
            System.out.println("daily: 4:30-5:30, 19:30-21:30");
            System.out.println("yearly: OCT-1-1:00-2:00, AUG-10-12:00-14:00");
            String toEdit = IOController.getAnswer("enter the field you would like to edit followed by its new " +
                    "value, or enter 'done'");
            String[] toEditList = toEdit.split(": ");
            if (toEditList[0].equalsIgnoreCase("recursion")){
                String forall = IOController.getAnswer("would you like this to apply to all or future events " +
                        "\n (please enter 'all' or 'future'?");
                if (forall.equalsIgnoreCase("all")){
                    //TODO method to delete all instances of event except original and replace with new recursions
                }else if (forall.equalsIgnoreCase("future")){
                    //TODO method to make this event the original, delete all future instances, and replace with new recursion
                }
            } else if(toEditList[0].equalsIgnoreCase("end")){
                //TODO method to change the end of the recursion and schedule more / remove events accordingly
            } else if (toEditList[0].equalsIgnoreCase("done")){
                done = true;
            }
        }


    }
}
