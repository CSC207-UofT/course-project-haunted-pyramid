package presenters.MenuStrategies;

import helpers.Constants;

import java.util.List;

public interface MenuContent {

    List<String> getContent();

    default void addMenuContent(StringBuilder result){
        List<String> menuList = getContent();
        int longestContent = getLongestContent(menuList);
        String divider = "-".repeat(Constants.MENU_DIVIDER + longestContent);
        result.append(" ").append(divider).append(" ");
        for (String item : menuList){
            result.append("\n").append("|").append(" ");
            String filler = " ".repeat(Constants.MENU_DIVIDER  + longestContent- item.length() - 1);
            result.append(item).append(filler).append("|");
        }
        result.append("\n").append(" ").append(divider);
    }

    private int getLongestContent(List<String> menuList) {
        int longestContent = 0;
        for (String item : menuList){
            if (item.length() > longestContent){
                longestContent = item.length();
            }
        }
        if (longestContent > Constants.MENU_DIVIDER - 5){
            longestContent -= 30;
        }
        else {longestContent = 0;}
        return longestContent;
    }

    default int numberOfOptions(){
        return getContent().size();
    }
}

