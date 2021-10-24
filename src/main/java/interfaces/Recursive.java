package interfaces;

import entities.Event;

import java.util.*;
public interface Recursive <T extends Event>{
    Optional<T> getNext();
    Optional<T> getPrevious();
    void setNext(T next);
    void setPrevious(T previous);
    boolean isLast();
    boolean isFirst();
}
