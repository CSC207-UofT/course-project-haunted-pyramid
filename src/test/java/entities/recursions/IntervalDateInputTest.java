package entities.recursions;

import entities.Event;
import interfaces.DateGetter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;

import interfaces.DateGetter.*;


class IntervalDateInputTest {

    LocalDateTime l =  LocalDateTime.of(2021, 12, 15, 10, 0);
    Event e1 = new Event(1, "e1", l);
    Event e2 = new Event(2, "e2", 2021, 11, 29, 10, 11, 0, 0);
    Event e3 = new Event(3, "e3", 2021, 11, 29, 10, 11, 0, 0);

//    @BeforeEach
//    void setUp() {
//        LocalDateTime l =  LocalDateTime.of(2021, 12, 15, 10, 0);
//        Event e1 = new Event(1, "e1", l);
//        Event e2 = new Event(2, "e2", 2021, 11, 29, 10, 11, 0, 0);
//        Event e3 = new Event(3, "e3", 2021, 11, 29, 10, 11, 0, 0);
//    }

    @Test
    void periodMultiplicationByScalarTest() {
        Period period = Period.between(LocalDate.from(e1.getEndTime()), LocalDate.from(e2.getEndTime()));
        // Period p = periodMultiplicationByScalar(period, 3);

    }

    @Test
    void listOfDatesInCycles() {
    }
}