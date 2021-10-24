package entities;

import interfaces.Recursive;

import java.time.LocalDateTime;
import java.util.Optional;

public class Lecture extends Event implements Recursive<Lecture> { ;
    Optional<Lecture> next;
    Optional<Lecture> previous;

    public Lecture(int ID, String name, LocalDateTime startTime, LocalDateTime endTime) {
        super(ID, name, startTime, endTime);
    }
    public Lecture(int ID, String name, int year, int month, int day, int startHour, int endHour, int startMin,
                int endMin){
        super(ID, name, year, month, day, startHour, endHour, startMin, endMin);
    }


    @Override
    public Optional<Lecture> getNext() {
        return this.next;
    }

    @Override
    public Optional<Lecture> getPrevious() {
        return this.previous;
    }

    @Override
    public void setNext(Lecture next) {
        this.next = Optional.of(next);
    }

    @Override
    public void setPrevious(Lecture previous) {

    }

    @Override
    public boolean isLast() {
        return false;
    }

    @Override
    public boolean isFirst() {
        return false;
    }
}
