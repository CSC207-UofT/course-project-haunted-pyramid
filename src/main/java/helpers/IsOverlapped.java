package helpers;

import java.util.List;

public class IsOverlapped {
    boolean check;

    /**
     * a helper class for checkConflict() method
     * returns true if any of the elements in one list belong in between the elements of the other list
     *
     * == Representation Invariant ==
     * ex1.get(0) <= ex1.get(1)
     * ex2.get(0) <= ex2.get(1)
     * @param ex1 a list that consists of doubles
     * @param ex2 a list that consists of doubles
     */

    public IsOverlapped(List<Double> ex1, List<Double> ex2){
        if ((ex1.get(0) <= ex2.get(0)) && (ex2.get(0) <= ex1.get(1))){
            this.check = true;
        }
        else this.check = (ex1.get(0) <= ex2.get(1)) && (ex2.get(1) <= ex1.get(1));
    }

    public boolean getResult(){
        return this.check;
    }
}
