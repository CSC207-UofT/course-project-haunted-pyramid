package GUISwing;

import helpers.Constants;

import javax.swing.*;

/**
 * Main Frame to be used for the app
 * @author Seo Won Yi
 */

public class MainFrame extends JFrame {
    /**
     * Set up the main frame to be used for all the common windows
     */
    public MainFrame() {
        this.setTitle("Haunted Pyramid Calendar App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setVisible(false);
        this.getContentPane().setBackground(Constants.WINDOW_COLOR);
        this.setLayout(null);
        this.setResizable(false);
        ImageIcon hauntedPyramid = new ImageIcon("res/Haunted_Pyramid_Icon.png");
        this.setIconImage(hauntedPyramid.getImage());
    }
}
