package GUISwing;

import controllers.MainController;
import controllers.UserController;
import interfaces.MeltParentWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainFrameWithMenu extends MainFrame{
    private UserController uc;
    MeltParentWindow parent;
    public MainFrameWithMenu(UserController userController, MeltParentWindow parent){
        this.uc = userController;
        this.parent = parent;
        JMenuBar menu = setUpMenu();
        setUpMenus(menu);
        this.setVisible(true);
    }

    private JMenuBar setUpMenu(){
        JMenuBar menuBar = new JMenuBar();
        this.add(menuBar);
        menuBar.setVisible(true);
        menuBar.setPreferredSize(new Dimension(this.getWidth(), 20));
        this.setJMenuBar(menuBar);
        return menuBar;
    }
    private void setUpMenus(JMenuBar menuBar){
        menuBar.add(new SettingsMenu(this.uc, parent));
    }
}
