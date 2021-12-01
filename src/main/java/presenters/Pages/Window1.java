package presenters.Pages;

import javax.swing.*;
import java.awt.*;

public class Window1 {

    JFrame frame = new JFrame();
    JLabel label = new JLabel("Testing.");

    public Window1() {

        label.setBounds(0, 0, 100, 50);
        label.setFont(new Font(null, Font.PLAIN, 25));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setLayout(null);
        frame.setVisible(true);
    }

}
