package server.server.ui;

import server.Constants;
import server.server.domain.ServerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class ServerWindow extends JFrame implements ServerView {
    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private static final JTextArea log = new JTextArea();
    private ServerController serverController;

    public static void main(String[] args) {
        new ServerWindow();
    }

    public ServerWindow(){
        setting();
        createPanel();

        setVisible(true);
    }

    private void setting(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(Constants.WIDTH, Constants.HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setLocationRelativeTo(null);
    }

    private void createPanel(){
        createLog();
        setBtnSettings();
        panelBottom.add(btnStart, BorderLayout.EAST);
        panelBottom.add(btnStop, BorderLayout.WEST);
        add(panelBottom, BorderLayout.SOUTH);
    }

    private void setBtnSettings(){
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverController.stop();
            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverController.start();
            }
        });
    }

    private void createLog(){
        log.setLineWrap(true);
        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);
    }

    @Override
    public void showMessage(String message) {
        log.append(message);
    }

    @Override
    public void setServerController(ServerController serverController) {
        this.serverController = serverController;
    }

    public ServerController getConnection(){
        return serverController;
    }
}
