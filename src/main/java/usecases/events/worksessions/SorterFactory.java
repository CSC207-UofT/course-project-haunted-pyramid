package usecases.events.worksessions;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Map;

public class SorterFactory {
    public DaySorter getDaySorter(boolean procrastinate, Map<LocalTime, LocalTime> freeTime){
        if (procrastinate){
            return new DaySorterLastMinute();
        }else{
            return new DaySorterEmptiest();
        }
    }

    public TimeSorter getTimeSorter(boolean procrastinate, Map<LocalTime, LocalTime> freeTime){
        if (procrastinate){
            return new TimeSorterLastMinute();
        } else {
            return new TimeSorterShortest();
        }
    }
}
