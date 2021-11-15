package helpers;

public class ConstantID {
    public static Integer ID = 0;
    public static void set(Integer start){ID = start;}
    public static Integer get(){
        ID ++;
        return ID;
    }
}
