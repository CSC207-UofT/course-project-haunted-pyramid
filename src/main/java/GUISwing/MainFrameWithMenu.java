package GUISwing;

import GUISwing.newSubMenus.EventMenu;
import controllers.MainController;

import javax.swing.*;
import java.awt.*;

public class MainFrameWithMenu extends MainFrame{
    MainController mainController;
    public MainFrameWithMenu(MainController mainController){
        this.mainController = mainController;
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
        menuBar.add(new SettingsMenu(mainController.getUserController()));
        menuBar.add(new EventMenu(mainController.getEventController()));
    }

}
