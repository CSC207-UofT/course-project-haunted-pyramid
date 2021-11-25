package presenters.MenuStrategies;

import helpers.Constants;

import java.util.List;

/**
 * Interface used for strategy pattern. Update menu image on the input StringBuilder
 * @author Seo Won Yi
 * @see DisplayMenu
 */
public interface MenuContent {

    /**
     * The list of contents that will be added on the menu
     * @return the list of content strings
     */
    List<String> getContent();

    /**
     * Return the menu image with the selections of contents
     * @param result StringBuilder object that the image will append on
     */
    default void addMenuContent(StringBuilder result) {
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

    /**
     * Get the longest length from the menuList
     * If there are enough space provided, return 0. Otherwise, subtract the space from the result
     * @param menuList list of Strings
     * @return the longest length from the menulist (subtracting the default space provided)
     */
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

    /**
     * Get the number of items in the menu
     * @return the number of items in the menu
     */
    default int numberOfOptions(){
        return getContent().size();
    }
}

