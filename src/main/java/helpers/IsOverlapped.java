package helpers;

import java.util.List;

/**
 * Check if there exists any overlap between two list of doubles
 * @author Seo Won Yi
 */
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
        this.check = false;
        if ((ex1.get(0) < ex2.get(0)) && (ex2.get(0) < ex1.get(1))){
            this.check = true;
        }
        else if (ex1.get(0) < ex2.get(1) && ex2.get(1) < ex1.get(1)){
            this.check = true;
        }
        else if (ex2.get(0) < ex1.get(0) && ex1.get(0) < ex2.get(1)){
            this.check = true;
        }
        else if (ex2.get(0) < ex1.get(1) && ex1.get(1) < ex2.get(1)){
            this.check = true;
        }
    }

    public boolean getResult(){
        return this.check;
    }
}
