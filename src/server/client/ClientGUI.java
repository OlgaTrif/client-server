package server.client;

import server.server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientGUI extends JFrame{
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private static final String SPASE = " ";
    private static final String COLON = ":";
    private static final String EMPTY_STR = "";
    private static final String SERVER_ERROR = "Server wasn't started. Start server first";
    private static final String LOGIN_SUCCESS = "login successfully";

    private static String login = "login";
    private static String ipAdress = "ipadress";
    private static String port = "port";
    private static String password = "pass";
    private static String message = "message";

    private final JTextArea log = new JTextArea();

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

    public ClientGUI(ServerWindow serverWindow) {
        server = serverWindow;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat Client");

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

        //log.setEditable(false);
        //JScrollPane scrollLog = new JScrollPane(log);
        //add(scrollLog);

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

    private void showMessage(String mes){
        tfChat.setText(tfChat.getText() + "\n" + mes);
    }

    private void sendLogToServer(String mes){
        server.setTextLog(login + COLON + SPASE + mes + "\n");
    }

    private void setBtnSettings(){
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (server.getServerStatus()) {
                    showMessage(LOGIN_SUCCESS);
                    login = tfLogin.getText();
                    sendLogToServer(LOGIN_SUCCESS);
                    btnSend.setEnabled(true);
                } else {
                    showMessage(SERVER_ERROR);
                }
            }
        });

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message = tfMessage.getText();
                showMessage(message);
                sendLogToServer(message);
                tfMessage.setText(EMPTY_STR);
            }
        });
    }
}
