package server.client;

import server.server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static server.Constants.*;

public class ClientGUI extends JFrame{
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private static final String SERVER_ERROR = "Server wasn't started. Start server first";
    private static final String LOGIN_SUCCESS = "login successfully";
    private static final String SERVER_DISCONNECT = "Disconnected from server";

    private static String login = "login";
    private static String ipAdress = "ipadress";
    private static String port = "port";
    private static String password = "pass";
    private static String message = "message";

    private final JPanel panelTop = new JPanel();
    private final JTextField tfIPAdress = new JTextField(ipAdress);
    private final JTextField tfPort = new JTextField(port);
    private final JTextField tfLogin = new JTextField(login);
    private final JPasswordField tfPassword = new JPasswordField(password);
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private final JPanel panelCenter = new JPanel(new BorderLayout());
    private final JTextArea tfChat = new JTextArea();

    private ServerWindow server;
    private boolean connected;

    public ClientGUI(ServerWindow serverWindow) {
        server = serverWindow;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat Client");

        createPanel();

        setVisible(true);
    }

    private void setIPInfo(){
        try {
            final DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ipAdress = socket.getLocalAddress().toString();
            port = String.valueOf(socket.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tfIPAdress.setText(ipAdress);
        tfPort.setText(port);
    }

    private void createPanel(){
        setIPInfo();
        panelTop.add(tfIPAdress);
        panelTop.add(tfPort);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        setBtnSettings();
        panelTop.add(btnLogin);
        add(panelTop, BorderLayout.NORTH);

        tfChat.setEditable(false);
        JScrollPane scrollChat = new JScrollPane(tfChat);
        panelCenter.add(scrollChat);
        add(panelCenter, BorderLayout.CENTER);

        panelBottom.add(tfMessage, BorderLayout.CENTER);
        btnSend.setEnabled(false);
        panelBottom.add(btnSend, BorderLayout.EAST);
        add(panelBottom, BorderLayout.SOUTH);
    }

    private void sendLogToServer(String mes){
        String result = login + COLON + SPASE + mes + NEW_STR;
        server.message(result);
        tfChat.setText(server.getLogInfo());
    }

    private void setBtnSettings(){
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    public void answer(String text) {
        server.setTextLog(text);
    }

    private void sendMessage(){
        message = tfMessage.getText();
        sendLogToServer(message);
        tfMessage.setText(EMPTY_STR);
    }

    private void connectToServer() {
        if (server.connectUser(this)){
            login = tfLogin.getText();
            sendLogToServer(LOGIN_SUCCESS);
            btnSend.setEnabled(true);
            btnLogin.setEnabled(false);
            panelTop.setVisible(false);
            connected = true;
            String log = server.getLog();
            if (EMPTY_STR.equals(log)) {
                tfChat.setText(log);
            }
        } else {
            sendLogToServer(SERVER_ERROR);
        }
    }

    public void disconnectFromServer() {
        if (connected) {
            panelTop.setVisible(true);
            connected = false;
            server.disconnectUser(this);
            sendLogToServer(SERVER_DISCONNECT);
        }
    }
}
