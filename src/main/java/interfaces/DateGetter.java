package interfaces;

import entities.Event;

import java.time.Period;
import java.util.List;

public interface DateGetter {
    List<Event> listOfDatesInCycles(List<Event> events);


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


