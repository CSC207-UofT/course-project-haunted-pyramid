package GUISwing;

import controllers.UserController;
import interfaces.MeltParentWindow;

import javax.swing.*;
import java.awt.*;

/**
 * @author Taite Cullen
 */
public class MainFrameWithMenu extends MainFrame{
    private final UserController uc;
    MeltParentWindow parent;

    /**
     * constructs new MainFrame with added menubar with SettingsMenu
     * @see SettingsMenu
     * @param userController active UserController with current User
     * @param parent Parent window that instantiated this window
     */
    public MainFrameWithMenu(UserController userController, MeltParentWindow parent){
        this.uc = userController;
        this.parent = parent;
        JMenuBar menu = setUpMenu();
        setUpMenus(menu);
        this.setVisible(true);
    }

    /**
     * set new menu bar as frame menu bar
     * @return a new menu bar and
     */
    private JMenuBar setUpMenu(){
        JMenuBar menuBar = new JMenuBar();
        this.add(menuBar);
        menuBar.setVisible(true);
        menuBar.setPreferredSize(new Dimension(this.getWidth(), 20));
        this.setJMenuBar(menuBar);
        return menuBar;
    }

    /**
     * add SettingsMenu to menuBar
     * @param menuBar menuBar of frame to contain SettingsMenu
     */
    private void setUpMenus(JMenuBar menuBar){
        menuBar.add(new SettingsMenu(this.uc, parent));
    }
}
