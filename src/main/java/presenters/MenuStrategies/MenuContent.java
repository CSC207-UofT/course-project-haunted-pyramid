package presenters.MenuStrategies;

import helpers.Constants;

import java.util.ArrayList;
import java.util.List;

public interface MenuContent {
    default void addMenuContent(StringBuilder result){
        List<String> menuList = getContent();
        for (String item : menuList){
            result.append("\n").append("|").append(" ");
            String filler = " ".repeat(Constants.MENU_DIVIDER - item.length() - 1);
            result.append(item).append(filler).append("|");
        }
    }

    List<String> getContent();

    default int numberOfOptions(){
        return getContent().size();
    }
}

