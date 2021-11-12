package presenters;

import presenters.MenuStrategies.MenuContent;

public class DisplayMenu {
    private MenuContent menuContent;


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
