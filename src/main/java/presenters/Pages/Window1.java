package presenters.Pages;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import java.awt.*;

public class Window1 {

    JFrame frame = new JFrame();
    JLabel label = new JLabel("Testing.");
    JPasswordField password = new JPasswordField();

    public Window1() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.add(label);
        frame.add(password);
        frame.setLayout(null);
        frame.setVisible(true);

        label.setBounds(0, 0, 100, 50);
        label.setFont(new Font(null, Font.PLAIN, 25));
        label.setVisible(true);
        password.setBounds(0, 50, 200, 50);
        password.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        password.setVisible(true);
    }

}
