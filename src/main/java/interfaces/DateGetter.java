package interfaces;

import entities.Event;

import java.time.Period;
import java.util.List;

public interface DateGetter {

    /**
     * Given a recursion pattern, this method returns all the events in the recursion.
     *
     * @param events the list of events in the first cycle of the recursion.
     * @return the list of events in the whole recursion
     */
    List<Event> listOfEventsInTheCycles(List<Event> events);

    /**
     *
     * @param period the period which the scalar multiplies
     * @param scalar the number of times to repeat the period
     * @return Returns the period corresponding to adding "period" a number "scalar" of times to itself.
     */
    default Period periodMultiplicationByScalar(Period period, int scalar){
        int i = 0;
        Period period1 = period;
        while(i < scalar){
            period1 = period1.plus(period);
            i++;
        }
        return period1;
    }


}


