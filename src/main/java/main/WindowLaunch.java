package main;

import presenters.Pages.Window1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowLaunch implements ActionListener {

    JFrame frame = new JFrame();
    JButton button1 = new JButton("Open New Window");

    public WindowLaunch() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setLayout(null);
        frame.setVisible(true);

        button1.setBounds(frame.getWidth()/2, frame.getHeight()/2, 300, 40);
        button1.setFocusable(false);
        button1.addActionListener(this);

        frame.add(button1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == button1) {
            frame.dispose();
            Window1 window1 = new Window1();
        }

    }
}