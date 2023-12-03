package taskB;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class MainFrame extends JFrame {
    private byte users = 0;
    private Object sync = new Object();
    private static final int OX = 1070, OY = 400;
    private JPanel mainPanel;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                    try {
                        MainFrame frame = new MainFrame();
                        frame.setVisible(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });
    }
    public MainFrame() throws IOException {
        super("lab 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, OX, OY);
        mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel threadOptions = new JPanel();
        threadOptions.setBorder(new EmptyBorder(1, 1, 1, 1));
        threadOptions.setLayout(new BoxLayout(threadOptions, BoxLayout.Y_AXIS));
        MyPanel p1 = new MyPanel(1,new Color(157, 157, 206));
        threadOptions.add(p1);
        mainPanel.add(threadOptions);
        setContentPane(mainPanel);
        pack();
        setResizable(false);
    }
}