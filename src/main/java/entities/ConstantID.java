package entities;

public class ConstantID {
    public static Integer ID = 0;
    public static Integer get(){
        ID ++;
        return ID;
    }
}
