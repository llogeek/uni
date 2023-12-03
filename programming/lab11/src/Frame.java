import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.List;

public class Frame extends JFrame implements ActionListener {
    private JMenuBar menuBar;
    private JMenu file;
    private JMenu data;
    private JMenu view;
    private JMenu look_and_feel;
    private JMenuItem font;
    private JMenuItem append;
    private JMenuItem print;
    private JMenuItem find;
    private JMenuItem delete;
    private JMenuItem open;
    private JMenuItem quit;
    private JList<AviaRace> list;
    private JLabel status;
    private JMenu studmenu;
    Races race;
    Action student;
    private String filename;
    private String idxname;
    private String filenameBak;
    private String idxnameBak;
    public static JMenuItem createMenuFont(final JFrame frame) {

        JMenuItem fontmenu = new JMenuItem("Font");
        final FontChooser chooser = new FontChooser(frame);
        fontmenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooser.setVisible(true);
                Font f = chooser.getSelectedFont();
                Enumeration keys = UIManager.getDefaults().keys();
                while (keys.hasMoreElements()) {
                    Object key = keys.nextElement();
                    Object value = UIManager.get(key);
                    if (value instanceof FontUIResource) {
                        FontUIResource orig = (FontUIResource) value;
                        UIManager.put(key, new FontUIResource(f));
                        SwingUtilities.updateComponentTreeUI(frame);
                    }
                }
            }
        });

        return fontmenu;
    }
    public static JMenu createPlafMenu(final JFrame frame) {

        JMenu plafmenu = new JMenu("Look and Feel");

        ButtonGroup radiogroup = new ButtonGroup();

        UIManager.LookAndFeelInfo[] plafs =
                UIManager.getInstalledLookAndFeels();

        for(int i = 0; i < plafs.length; i++) {
            String plafName = plafs[i].getName();
            final String plafClassName = plafs[i].getClassName();

            JMenuItem item = plafmenu.add(new JRadioButtonMenuItem(plafName));
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {

                        UIManager.setLookAndFeel(plafClassName);
                        SwingUtilities.updateComponentTreeUI(frame);
                    }
                    catch(Exception ex) { System.err.println(ex); }
                }

            });
            radiogroup.add(item);
        }
        return plafmenu;
    }
    public Frame(Races race) throws HeadlessException {
        super("Lolita's avialines");  // Call superclass constructor and set window title
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Container contentPane = this.getContentPane();
        setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        JMenuBar menuBar = new JMenuBar();  // Create a menubar
        this.race = race;
        file = new JMenu("File");
        open = new JMenuItem("Open");
        quit = new JMenuItem("Quit");
        studmenu = new JMenu("Student");

        file.add(open);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        file.addSeparator();
        file.add(quit);
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
        data = new JMenu("Data");
        append = new JMenuItem("Append");
        print = new JMenuItem("Print");
        delete = new JMenuItem("Delete");
        find = new JMenuItem("Find");
        view = new JMenu("View");
        data.setEnabled(false);
        data.add(append);
        data.add(print);
        data.add(find);
        data.add(delete);
        font = createMenuFont(this);
        look_and_feel = createPlafMenu(this);
      //  chooser = new FontChooser(this);
        menuBar.add(file);
        menuBar.add(data);
        menuBar.add(view);
        menuBar.add(studmenu);
        setJMenuBar(menuBar);
        view.add(font);
        view.addSeparator();
        view.add(look_and_feel);
        view.add(font);
        view.addSeparator();
        view.add(look_and_feel);
        font.addActionListener(this);
        look_and_feel.addActionListener(this);
        open.addActionListener(this);
        quit.addActionListener(this);
        append.addActionListener(this);
        print.addActionListener(this);
        delete.addActionListener(this);
        find.addActionListener(this);
        list = new JList<>();
        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane);
        pack();
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        class StudAction extends AbstractAction {
            public StudAction() {
                super("Student");
            }

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Frame.this,
                        "Garmatnaya Lolita 10B group 2 course\n FPMI, BSU \n ",
                        "Student information",
                        JOptionPane.PLAIN_MESSAGE);
            }
        }
        student = new StudAction();
        studmenu.add(student);
    }

    @Override
    public void actionPerformed(ActionEvent event) throws IllegalArgumentException {
        if (event.getSource() == open) {
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Data file", "dat", "txt"));
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String file = fileChooser.getDescription(fileChooser.getSelectedFile()).replaceFirst("[.][^.]+$", "");
                data.setEnabled(true);
                filename = file + ".dat";
                idxname = file + ".idx";
                filenameBak = file + ".~dat";
                idxnameBak = file + ".~idx";
            }
        }
        else if (event.getSource() == quit) {
            int response =
                    JOptionPane.showConfirmDialog(Frame.this, "Really Quit?");
            if (response == JOptionPane.YES_OPTION) System.exit(0);
        }else if (event.getSource() == delete) {
            JPanel inputPanel = new JPanel();
            JComboBox<String> index = new JComboBox<>(new String[] {"departure", "destination", "number"});
            index.setEnabled(true);
            index.setSelectedIndex(0);
            inputPanel.add(new JLabel("Index"));
            inputPanel.add(index);
            JTextField key = new JTextField("", 4);
            inputPanel.add(new JLabel("Key"));
            inputPanel.add(key);
            String[] hints = {"", "", ""};
            index.addItemListener(e -> key.setText(hints[index.getSelectedIndex()]));
            if (JOptionPane.showConfirmDialog(this, inputPanel,
                    "Enter position", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    race.delete(filename, idxname, filenameBak,idxnameBak, index.getSelectedIndex(), key.getText());
                } catch (ClassNotFoundException | IOException | KeyNotUniqueException | IllegalArgumentException e) {
                    showError(e);
                }
            }
        } else if (event.getSource() == append) {
            JTextField departure = new JTextField("", 10);
            JTextField destination = new JTextField("", 10);
            JTextField number = new JTextField("", 20);
            JTextField type = new JTextField("", 7);
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(15, 2));
            inputPanel.add(new JLabel("Отправление"));
            inputPanel.add(departure);
            inputPanel.add(new JLabel("Прибытие"));
            inputPanel.add(destination);
            inputPanel.add(new JLabel("Номер"));
            inputPanel.add(number);
            inputPanel.add(new JLabel("Тип"));
            inputPanel.add(type);

            String[] hours = new String[24];
            String[] minutes = new String[60];
            DecimalFormat f =new DecimalFormat( "00.###" );
            for (int i = 0; i < hours.length; i++) {
                if (i < 10) {
                    hours[i] = f.format(i);
                }
                else hours[i] = String.valueOf(i);
            }
            for (int i = 0; i < minutes.length; i++) {
                if (i < 10) {
                    minutes[i] = f.format(i);
                }
                else minutes[i] = String.valueOf(i);
            }
            JComboBox timeh = new JComboBox(hours);
            JComboBox timem = new JComboBox(minutes);
            inputPanel.add(new JLabel("Час отправления"));
            inputPanel.add(timeh);
            inputPanel.add(new JLabel("Минуты"));
            inputPanel.add(timem);
            timem.setEnabled(true);
            timeh.addItemListener(ev -> getName());
            timem.addItemListener(ev -> getName());
            String[] week = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
            inputPanel.add(new JLabel("Дни курсирования"));
            JCheckBox monday = new JCheckBox(week[0], true);
            monday.setMnemonic(KeyEvent.VK_M);
            inputPanel.add(monday);
            inputPanel.add(new JLabel(""));
            JCheckBox tuesday = new JCheckBox(week[1], false);
            tuesday.setMnemonic(KeyEvent.VK_T);
            inputPanel.add(tuesday);
            inputPanel.add(new JLabel(""));
            JCheckBox wednesday = new JCheckBox(week[2], false);
            wednesday.setMnemonic(KeyEvent.VK_W);
            inputPanel.add(wednesday);
            inputPanel.add(new JLabel(""));
            JCheckBox thursday = new JCheckBox(week[3], false);
            thursday.setMnemonic(KeyEvent.VK_H);
            inputPanel.add(thursday);
            inputPanel.add(new JLabel(""));
            JCheckBox friday = new JCheckBox(week[4], false);
            friday.setMnemonic(KeyEvent.VK_F);
            inputPanel.add(friday);
            inputPanel.add(new JLabel(""));
            JCheckBox saturday = new JCheckBox(week[5], false);
            saturday.setMnemonic(KeyEvent.VK_S);
            inputPanel.add(saturday);
            inputPanel.add(new JLabel(""));
            JCheckBox sunday = new JCheckBox(week[6], false);
            sunday.setMnemonic(KeyEvent.VK_U);
            inputPanel.add(sunday);

            // inputPanel.add(days);
            JCheckBox zipped = new JCheckBox("Zipped", false);
            inputPanel.add(zipped);
            if (JOptionPane.showConfirmDialog(this, inputPanel,
                    "Append employee", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                String dep = departure.getText();
                String des = destination.getText();
                String num = number.getText();
                String tp = type.getText();
                String tm = null;
                     tm = (String) timeh.getItemAt(timeh.getSelectedIndex()) + ":" + (String) timem.getItemAt(timem.getSelectedIndex());
                String table = "";
                if (monday.isSelected()) table += "1";
                else table += "-";
                if (tuesday.isSelected()) table += "2";
                else table += "-";
                if (wednesday.isSelected()) table += "3";
                else table += "-";
                if (thursday.isSelected()) table += "4";
                else table += "-";
                if (friday.isSelected()) table += "5";
                else table += "-";
                if (saturday.isSelected()) table += "6";
                else table += "-";
                if (sunday.isSelected()) table += "7";
                else table += "-";
                   // String table = days.getText();
                try {
                    Races.appendFile(filename, idxname, zipped.isSelected(), dep, des,num, tp, tm, table);
                    table ="";
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (KeyNotUniqueException e) {
                    e.printStackTrace();
                }

            }
        } else if (event.getSource() == print) {
            JRadioButton notSorted = new JRadioButton("Not sorted");
            JRadioButton sorted = new JRadioButton("Sorted");
            JRadioButton revSorted = new JRadioButton("Reverse sorted");
            ButtonGroup group = new ButtonGroup();
            group.add(notSorted);
            group.add(sorted);
            group.add(revSorted);
            notSorted.setSelected(true);

            JPanel inputPanel = new JPanel();
            inputPanel.add(notSorted);
            inputPanel.add(sorted);
            inputPanel.add(revSorted);

            JComboBox<String> index = new JComboBox<>(new String[] {"departure", "destination", "number"});
            index.setEnabled(false);
            index.setSelectedIndex(0);
            notSorted.addActionListener(e -> index.setEnabled(false));
            sorted.addActionListener(e -> index.setEnabled(true));
            revSorted.addActionListener(e -> index.setEnabled(true));
            inputPanel.add(new JLabel("Index"));
            inputPanel.add(index);
            if (JOptionPane.showConfirmDialog(this, inputPanel,
                    "Print employees", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    if (notSorted.isSelected()) {
                        Races.printFile(filename);
                    } else if (sorted.isSelected()){
                        Races.printFile(filename, idxname,index.getSelectedIndex(),false);
                    }
                    else if (revSorted.isSelected()){
                        Races.printFile(filename, idxname,index.getSelectedIndex(),true);
                    }
                } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
                    showError(e);
                }
            }
        } else if (event.getSource() == find) {
            JRadioButton byKey = new JRadioButton("By key");
            JRadioButton larger = new JRadioButton("Right");
            JRadioButton less = new JRadioButton("Left");
            JPanel inputPanel = new JPanel();
            inputPanel.add(byKey);
            inputPanel.add(larger);
            inputPanel.add(less);
            ButtonGroup group = new ButtonGroup();
            group.add(byKey);
            group.add(larger);
            group.add(less);
            byKey.setSelected(true);
            JComboBox<String> index = new JComboBox<>(new String[] {"departure", "destination", "number"});
            index.setEnabled(true);
            index.setSelectedIndex(0);
            inputPanel.add(new JLabel("Parameter"));
            inputPanel.add(index);
            JTextField key = new JTextField("", 15);
            inputPanel.add(new JLabel("Key"));
            inputPanel.add(key);
            String[] temp = {"", "", ""};
            index.addItemListener(e -> key.setText(temp[index.getSelectedIndex()]));
            if (JOptionPane.showConfirmDialog(this, inputPanel,
                    "Find employees", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    if (byKey.isSelected()) {
                        Races.findByKey(filename, idxname, index.getSelectedIndex(), key.getText());
                    } else if (larger.isSelected()) {
                        Races.findByKey(filename, idxname, index.getSelectedIndex(), key.getText(), new KeyCompReverse());
                    } else {
                        Races.findByKey(filename, idxname, index.getSelectedIndex(), key.getText(), new KeyComp());
                    }
                } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
                    showError(e);
                }
            }
        }
    }

    void showData(List<AviaRace> data) {
        this.list.setListData(data.toArray(new AviaRace[0]));
    }

    public void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    public void showError(String e) {
        JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
    }
    void update(String string) {
        status.setText(string);
    }

}
