package GUISwing;

import helpers.Constants;

import javax.swing.*;

/**
 * Set up the pop-up window to be used for the app
 * @author Seo Won Yi
 */

public class PopUpWindowFrame extends JFrame {
    /**
     * Set up the pop-up window to be used
     */
    public PopUpWindowFrame() {
        this.setTitle("Haunted Pyramid Calendar App");
        this.setResizable(false);
        ImageIcon hauntedPyramid = new ImageIcon("res/Haunted_Pyramid_Icon.png");
        this.setIconImage(hauntedPyramid.getImage());
        this.getContentPane().setBackground(Constants.WINDOW_COLOR);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(Constants.POPUP_WIDTH, Constants.POPUP_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setVisible(false);
    }
}
