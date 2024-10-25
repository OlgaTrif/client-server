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

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private static final JTextArea log = new JTextArea();
    private boolean isServerWorking;

    public static void main(String[] args) {
        new ServerWindow();
    }

    public ServerWindow(){
        isServerWorking = false;
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = false;
                setTextLog("Server was stopped \n");
            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = true;
                setTextLog("Server was started \n");
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("chatServer");

        log.setLineWrap(true);
        log.setEditable(false);
        log.setSize(WIDTH - 100, HEIGHT - 100);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);
        //panelTop.add(log, BorderLayout.CENTER);
        //add(panelTop, BorderLayout.NORTH);

        panelBottom.add(btnStart, BorderLayout.EAST);
        panelBottom.add(btnStop, BorderLayout.WEST);
        add(panelBottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void setTextLog(String text){
        log.setText(log.getText() + text + "\n");
    }

    public boolean getServerStatus(){
        return isServerWorking;
    }
}
