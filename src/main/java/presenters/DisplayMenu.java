package presenters;

import presenters.MenuStrategies.MenuContent;

/**
 * Display menu with the contents added by Strategy design pattern
 * @author Seo Won Yi
 * @see MenuContent
 */
public class DisplayMenu {
    private MenuContent menuContent;

    /**
     * display the chosen menu
     * @param typeMenuContent type of the menu to show
     * @return chosen menu image
     */
    public String displayMenu(MenuContent typeMenuContent) {
        setMenuContent(typeMenuContent);
        StringBuilder result = new StringBuilder();
        this.menuContent.addMenuContent(result);
        return result.toString();
    }

    public void setMenuContent(MenuContent menuContent) {
        this.menuContent = menuContent;
    }

}
