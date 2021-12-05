package GUISwing;

import controllers.MainController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainFrameWithMenu extends MainFrame{
    MainController mainController;
    ActionListener parent;
    public MainFrameWithMenu(MainController mainController, ActionListener parent){
        this.mainController = mainController;
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
        menuBar.add(new SettingsMenu(mainController.getUserController(), parent));
    }

}
