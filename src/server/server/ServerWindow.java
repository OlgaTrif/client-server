package server.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerWindow extends JFrame{
    private static final int POS_X = 500;
    private static final int POS_Y = 550;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    private static final JTextArea log = new JTextArea();
    private boolean isServerWorking;

    public static void main(String[] args) {
        new ServerWindow();
    }

    public ServerWindow(){
        isServerWorking = true;
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = false;
                setTextLogAndShowText("Server was stopped " + isServerWorking);
                //System.out.println("Server was stopped " + isServerWorking + "\n");
            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = true;
                setTextLogAndShowText("Server was started " + isServerWorking);
                //System.out.println("Server was started " + isServerWorking + "\n");
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("chatServer");
        setAlwaysOnTop(true);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(btnStart);
        add(btnStop);
        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        log.setSize(WIDTH - 30, HEIGHT);
        add(log);


        setVisible(true);
    }

    public void setTextLogAndShowText(String text){
        log.setText(text);
    }
}
