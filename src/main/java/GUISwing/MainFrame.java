package GUISwing;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        this.setTitle("Haunted Pyramid Calendar App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1444, 1000);
        this.setVisible(false);
        this.getContentPane().setBackground(new Color(233, 161, 161));
        this.setLayout(null);
        this.setResizable(false);
        ImageIcon hauntedPyramid = new ImageIcon("res/Haunted_Pyramid_Icon.png");
        this.setIconImage(hauntedPyramid.getImage());
    }

    private void setupMenu(){
        JMenuBar menuBar = new JMenuBar();
        this.add(menuBar);
    }
}
