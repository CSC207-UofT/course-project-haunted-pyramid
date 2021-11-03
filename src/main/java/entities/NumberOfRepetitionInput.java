package entities;

import interfaces.DateGetter;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class NumberOfRepetitionInput implements DateGetter {

    private int numberOfRepetitions;

    private NumberOfRepetitionInput(int numberOfRepetitions){
        this.numberOfRepetitions = numberOfRepetitions;
    }


    @Override
    public ArrayList<LocalDateTime> listOfDatesInCycles(ArrayList<Event> events) {
        // Todo implement this method.
        return null;
    }
}
