package csd.auth.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ilias on 6/1/2017.
 */
public class TwitterClientFrame extends JFrame {

    private static final int WIDTH_BUTTON = 50;
    private JTextArea console;
    private JLabel statusInfo;


    public TwitterClientFrame() {
        initUI();
    }

    private void initUI() {


        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        ImageIcon exit = new ImageIcon(this.getClass().getResource("/icons/door.png"));
        JButton bexit = new JButton(exit);
        bexit.setBorder(new EmptyBorder(0, 0, 0, 0));
        toolbar.add(bexit);

        add(toolbar, BorderLayout.NORTH);

        JToolBar vertical = new JToolBar(JToolBar.VERTICAL);
        vertical.setFloatable(false);
        vertical.setMargin(new Insets(15, 5, 5, 5));

        ImageIcon mongoIcon = new ImageIcon(this.getClass().getResource("/icons/mongo.png"));
        ImageIcon neo4jIcon = new ImageIcon(this.getClass().getResource("/icons/neo4j.png"));
        ImageIcon gephiIcon = new ImageIcon(this.getClass().getResource("/icons/gephi.png"));

        JButton mongoBtn = new JButton(mongoIcon);
        mongoBtn.setBorder(new EmptyBorder(WIDTH_BUTTON, WIDTH_BUTTON, WIDTH_BUTTON, WIDTH_BUTTON));
        mongoBtn.addActionListener(new MongoActionListener(this));

        JButton neo4jBtn = new JButton(neo4jIcon);
        neo4jBtn.setBorder(new EmptyBorder(WIDTH_BUTTON, WIDTH_BUTTON, WIDTH_BUTTON, WIDTH_BUTTON));
        neo4jBtn.addActionListener(new Neo4jActionListener(this));

        JButton gephiBtn = new JButton(gephiIcon);
        gephiBtn.setBorder(new EmptyBorder(WIDTH_BUTTON, WIDTH_BUTTON, WIDTH_BUTTON, WIDTH_BUTTON));
        gephiBtn.addActionListener(new GephiActionListener(this));

        vertical.add(mongoBtn);
        vertical.add(neo4jBtn);
        vertical.add(gephiBtn);

        add(vertical, BorderLayout.WEST);

        initConsole();

        JToolBar statusbar = new JToolBar();
        statusbar.setFloatable(false);
        statusbar.setMargin(new Insets(3, 10, 5, 35));

        JLabel statusLabel = new JLabel(" Statusbar:   ");
        statusInfo = new JLabel();
        statusbar.add(statusLabel);
        statusbar.add(statusInfo);

        add(statusbar, BorderLayout.SOUTH);

        setSize(800, 500);
        setTitle("Twitter-Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initConsole() {
        console = new JTextArea();
        console.setEnabled(false);
        console.setFont(new Font("monospaced", Font.BOLD, 15));
        console.setBackground(Color.DARK_GRAY);
        console.setDisabledTextColor(Color.GREEN);

        JScrollPane scroll = new JScrollPane(console);

        setConsoleText(getTimeStamp() + " <-----  INIT MESSAGE  ----->");


        add(scroll, BorderLayout.CENTER);
    }

    private String getTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return " " + dateFormat.format(date) + " ";
    }

    public void setStatusInfo(String text,Color color) {
        statusInfo.setForeground(color);
        statusInfo.setText(text);
    }

    public void setConsoleText(String text) {
        console.append(getTimeStamp() + text + '\n');
        console.setCaretPosition(console.getDocument().getLength()); //if you want to follow the bottom with scroll
    }

}
