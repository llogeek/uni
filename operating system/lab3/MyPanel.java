package taskB;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class MyPanel extends JPanel {
    private static final int CX = 800, CY = 650;
    private JButton directoryButton;
    private String directory;
    private JTextArea text;
    private ArrayList<String> wrongTests;
    private ArrayList<String> check;
    static  ArrayDeque<Test> tests;
    static ArrayDeque<Test> solutions;
    private boolean checkTests = false;
    private boolean Solve = false;
    private boolean checkSolution = false;
    private byte users = 0;
    private Object sync = new Object();
    private Timer timer;
    JButton start;
    ThreadCheckTests t = null;
    ThreadSolve s = null;
    ThreadCheckSolution c = null;
    private int number;
    public MyPanel(int number, Color back) throws IOException {
        this.number = number;
        setSize(CX, CY);
        setBorder(new EmptyBorder(1, 1,1,1));
        setBackground(back);
        setLayout(null);
        createLists();
        createButtons();
        createLabels();
        timer = new Timer(5, new ActionListener() {
            public void actionPerformed(ActionEvent e) { if ( t != null ) {
                printResult();
                if (!t.isAlive() && !s.isAlive() && !c.isAlive()){
                    t.myInterrupt();
                    t = null;
                    s.myInterrupt();
                    s = null;
                    c.myInterrupt();
                    c = null;
                    timer.stop();
                    setFieldsEnabled(true);
                    start.setEnabled(true);
                    button = Buttons.Stop;
                }
            }
            }
        });
    }
    public synchronized boolean getFlagStateTests(){
        return checkTests;
    }

    public synchronized void setFlagStateTests(boolean f){
        checkTests = f;
    }
    public synchronized boolean getFlagStateSolutions(){
        return Solve;
    }
    public synchronized void setFlagStateSolutions(boolean f){
        Solve = f;
    }
    public synchronized boolean getFlagCheckSolutions(){
        return checkSolution;
    }
    public synchronized void setFlagCheckSolutions(boolean f){
        checkSolution = f;
    }

    public synchronized ArrayDeque<Test> getTests(){ return tests;}
    public synchronized ArrayDeque<Test> getSolutions(){ return solutions;}
    public Dimension getPreferredSize () {
        return new Dimension(CX, CY);
    }
    public synchronized MyPanel getSearchPanel() {
            return (MyPanel) this;
    }
    public synchronized void setText(String str){ text.append(str);}
    public synchronized void addResultCheck (String str) {
        synchronized(sync) {
            check.add(str);
        }
    }
    public synchronized void addWrongTest (String str) {
        synchronized(sync) {
            wrongTests.add(str);
        }
    }
    public synchronized void addTest (Test test) {
        tests.add(test);
    }
    public synchronized void addSolution (Test sol) {
            solutions.add(sol);
    }
    public synchronized void ClearList() {
        if ( users == 0 )
            text.setText("");
    }
    private synchronized void newArrayList () {
        if ( users == 0 )
            wrongTests = new ArrayList<>();
            check = new ArrayList<>();
            tests = new ArrayDeque<>();
            solutions = new ArrayDeque();
    }
    public void addUser () {
        newArrayList();
        synchronized (sync) {
            users++;
        }
    } public void removeUser () { synchronized (sync) { users--; } }
    public synchronized void printResult () {
        text.setText("");
        for (int i = 0; i < check.size(); i++)
            text.append(check.get(i));
        text.append("\n Неправильные тесты: \n");
        if (wrongTests.size() == 0) text.append("Нет неправильных тестов!");
        else {
            for (int i = 0; i < wrongTests.size(); i++)
                text.append(wrongTests.get(i));
        }
    }
    private void setFieldsEnabled (boolean enabled) {
        directoryButton.setEnabled(enabled);
        if ( enabled ) removeUser();

    }
    private void createLists(){
        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBounds(50 , 100, 700, 500);
        add(scrollPane1);
        add(scrollPane1);
        text = new JTextArea();
        text.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        text.setEditable(false);
        text.setBackground(Color.white);
        scrollPane1.setViewportView(text);
    }

    private void createButtons () {
        start = new JButton("Start");
        start.setForeground(Color.white);
        start.setBackground(new Color(52, 173, 12));
        start.setBounds(350, 20, 80, 50);
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            start.setEnabled(false);
            if ( button != Buttons.Start ) {
                button = Buttons.Start;
                if ( t == null && s == null && c == null) {
                    ClearList();
                    try {
                       t = new ThreadCheckTests(number,directory, getSearchPanel());
                       s = new ThreadSolve(number, directory, getSearchPanel());
                       c = new ThreadCheckSolution(number, getSearchPanel());
                    } catch (Exception except) {
                        JOptionPane.showMessageDialog(null, except.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
                        start.setEnabled(true);
                        button = Buttons.Stop;
                        return;
                    }
                    setFieldsEnabled(false);
                    t.start();
                    s.start();
                    c.start();
                }
                timer.start();
            }
        }
        });
        add(start);
        JButton stop = new JButton("Stop");
        stop.setForeground(Color.white);
        stop.setBackground(new Color(250, 26, 7));
        stop.setBounds(450, 20, 80, 50);
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( button != Buttons.Stop&& !s.isInterrupted() ) {
                    if ( s != null && t != null && c != null) {
                    t.myInterrupt();
                    t = null;

                        s.myInterrupt();
                        s = null;
                        c.myInterrupt();
                        c = null;
                    }
                    timer.stop();
                    setFieldsEnabled(true);
                    start.setEnabled(true);
                } button = Buttons.Stop;
            }
        });
        add(stop);
        directoryButton = new JButton("директория");
        directoryButton.setBounds(160, 28, 170, 20);
        directoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if ( chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {
                directory = chooser.getSelectedFile().getAbsolutePath();
                directoryButton.setText(directory);
            }
        }
        });
        add(directoryButton);
    }
    private void createLabels () {
        JLabel label1 = new JLabel("Директория:");
        label1.setBounds(70, 30, 80, 14);
        add(label1);

    }
   private enum Buttons { Start,  Stop }
    private Buttons button = null;
}
