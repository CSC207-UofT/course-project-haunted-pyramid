package entities;

import interfaces.DateGetter;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class IntervalDateInput implements DateGetter {

    private LocalDateTime[] periodOfRepetition;

    private IntervalDateInput(LocalDateTime beginningOfCycles, LocalDateTime endOfCycles){
        this.periodOfRepetition = new LocalDateTime[2];
        this.periodOfRepetition[0] = beginningOfCycles;
        this.periodOfRepetition[1] = endOfCycles;
    }

    @Override
    public ArrayList<LocalDateTime> listOfDatesInCycles(ArrayList<Event> events) {
        // Todo implement this method.
        return null;
    }
}
