package helpers;

import java.time.LocalDateTime;

public class Pattern {
    public LocalDateTime getDate(LocalDateTime start, LocalDateTime end, Integer count){
        LocalDateTime next = start.plusDays(count*7L);
        if (!next.isAfter(end)){
            return start.plusDays(count* 7L);
        }
        return null;
    }
}
