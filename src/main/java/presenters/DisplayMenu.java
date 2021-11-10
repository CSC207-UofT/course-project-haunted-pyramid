package presenters;

import helpers.Constants;

import java.util.ArrayList;
import java.util.List;

public class DisplayMenu {
    private MenuContent menuContent = new MenuContent();


    public String displayMenu(String type){
        StringBuilder result = new StringBuilder();
        String divider = "-".repeat(Constants.MENU_DIVIDER);
        result.append(" ").append(divider).append(" ");
        if (type.equalsIgnoreCase("Basic"))
        {
            this.menuContent.addBasicMenuContent(result);}
        else if (type.equalsIgnoreCase("Event Type")){
            this.menuContent.addEventTypeMenuContent(result);
        }
        result.append("\n").append(" ").append(divider);
        return result.toString();
    }


    public static void main(String[] args) {
        DisplayMenu dm = new DisplayMenu();
        System.out.println(dm.displayMenu("basic"));
        System.out.println(dm.displayMenu("event type"));
    }
}
