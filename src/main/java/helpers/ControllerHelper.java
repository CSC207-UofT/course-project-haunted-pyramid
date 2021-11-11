package helpers;

import presenters.DisplayMenu;
import presenters.MenuStrategies.MenuContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ControllerHelper {
    private final Scanner scanner = new Scanner(System.in);

    public String invalidCheck(DisplayMenu displayMenu, String input, int numberOfOptions,
                                MenuContent menuContentType) {
        while (!validOption(listOfOptions(numberOfOptions)).contains(input)){
            System.out.println("Please select the valid number from the menu");
            System.out.println(displayMenu.displayMenu(menuContentType));
            input = scanner.nextLine();
        }
        return input;
    }


    private List<Integer> listOfOptions(int number){
        List<Integer> intList = new ArrayList<>();
        for (int i = 1; i <= number; i++){
            intList.add(i);
        }
        return intList;
    }

    public List<String> validOption(List<Integer> options){
        List<String> temp = new ArrayList<>();
        for (Integer number : options){
            temp.add(String.valueOf(number));
        }
        return temp;
    }
}
