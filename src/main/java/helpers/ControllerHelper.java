package helpers;

import presenters.MenuStrategies.DisplayMenu;
import presenters.MenuStrategies.MenuContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Helper class that checks if the numerical inputs are valid for the controller classes
 * @author Seo Won Yi
 * @see controllers.CalendarController
 * @see controllers.MainController
 */
public class ControllerHelper {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Checks if the input is valid by comparing with the list of available options
     * returns Return string if the user wants to return to the main menu
     * @param displayMenu menu that will show up every time the check fails
     * @param input the input to check
     * @param numberOfOptions valid options
     * @param menuContentType type of the menu that will be displayed upon failure
     * @return input if it passes the check or return (if the input was return)
     */
    public String invalidCheck(DisplayMenu displayMenu, String input, int numberOfOptions,
                                MenuContent menuContentType) {
        if (input.equalsIgnoreCase("return")) {
            return "Return";
        }
        while (!validOption(listOfOptions(numberOfOptions)).contains(input)) {
            System.out.println("Please select the valid number from the menu");
            System.out.println(displayMenu.displayMenu(menuContentType));
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("return")) {
                return "Return";
            }
        }
        return input;
    }

    public String invalidCheckNoMenu(String input, int numberOfOptions, String question) {
        if (input.equalsIgnoreCase("return")) {
            return "Return";
        }
        while (!validOption(listOfOptions(numberOfOptions)).contains(input)) {
            System.out.println(question);
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("return")) {
                return "Return";
            }
        }
        return input;
    }

    /**
     * return the list of all the integers that starts from 1 and goes up to number (inclusive)
     * @param number the highest number that will be included in the list
     * @return the list of integers
     */
    private List<Integer> listOfOptions(int number){
        List<Integer> intList = new ArrayList<>();
        for (int i = 1; i <= number; i++){
            intList.add(i);
        }
        return intList;
    }

    /**
     * return the list of integer strings that are considered to be valid options
     * @param options list of integers that are to be converted into integer strings
     * @return list of the integer strings that will be used as valid options
     */
    public List<String> validOption(List<Integer> options){
        List<String> temp = new ArrayList<>();
        for (Integer number : options){
            temp.add(String.valueOf(number));
        }
        return temp;
    }

    /**
     * confirm if the string is all numerical
     *
     * @param str the string to check
     * @return true if the string is numerical otherwise false
     */
    public boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
