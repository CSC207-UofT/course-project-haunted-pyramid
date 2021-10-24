package controllers;

import entities.Event;
import interfaces.Recursive;
import usecases.EventManager;
import usecases.RecurringManager;

import java.util.List;
import java.util.Set;

public class RecursiveController {

    public <T extends Event> Set<Event> schedule(T input, EventManager em) {
        String repetition = IOController.getAnswer("Does it repeat daily, weekly, monthly, custom? " +
                "(please enter response)");
        List<Integer> end = IOController.getDate("Until when?");
        return RecurringManager.schedule(input, repetition, end, em);
    }
}
