package interfaces;

/**
 * An interface that helps to control the window in other windows
 * @author Seo Won Yi
 * @see GUISwing.MainMenu
 * @see GUISwing.EditEventWindow
 * @see GUISwing.CalendarChoices
 */

public interface MeltParentWindow {

    /**
     * enable the frame
     */
    void enableFrame();

    /**
     * exit the frame
     */
    void exitFrame();

    /**
     * get parent object
     * @return the parent object
     */
    MeltParentWindow getParent();

    /**
     * refresh the window
     */
    void refresh();
}
