package server.client.ui;

import server.Constants;
import server.client.domain.ClientController;
import server.server.ui.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static server.Constants.*;

public class ClientGUI extends JFrame implements ClientView {
    private static String login_name = "login";
    private static String ipAdress = "ipadress";
    private static String port = "port";
    private static String password = "pass";

    private final JPanel panelTop = new JPanel();
    private final JTextField tfIPAdress = new JTextField(ipAdress);
    private final JTextField tfPort = new JTextField(port);
    private final JTextField tfLogin = new JTextField(login_name);
    private final JPasswordField tfPassword = new JPasswordField(password);
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private final JPanel panelCenter = new JPanel(new BorderLayout());
    private final JTextArea tfChat = new JTextArea();

    private ClientController clientController;

    public ClientGUI() {
        setting();
        createPanel();

        setVisible(true);
    }

    @Override
    public void showMessage(String message) {
        tfChat.append(message);
    }

    @Override
    public void disconnectedFromServer() {
        hidePanelTop(true);
    }

    @Override
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    private void setting() {
        setSize(Constants.WIDTH, Constants.HEIGHT);
        setResizable(false);
        setTitle("Chat client");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    private void createPanel(){
        setIPInfo();
        createTopper();
        createMiddle();
        createBottom();
    }


    private void createTopper(){
        panelTop.add(tfIPAdress);
        panelTop.add(tfPort);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        setBtnSettings();
        panelTop.add(btnLogin);
        add(panelTop, BorderLayout.NORTH);
    }

    private void createMiddle(){
        tfChat.setEditable(false);
        JScrollPane scrollChat = new JScrollPane(tfChat);
        panelCenter.add(scrollChat);
        add(panelCenter, BorderLayout.CENTER);
    }

    private void createBottom(){
        setMessageSetting();
        panelBottom.add(tfMessage, BorderLayout.CENTER);
        btnSend.setEnabled(false);
        panelBottom.add(btnSend, BorderLayout.EAST);
        add(panelBottom, BorderLayout.SOUTH);
    }

    private void setMessageSetting(){
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    sendMessage();
                }
            }
        });
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

    private void sendMessage(){
        clientController.message(tfMessage.getText());
        tfMessage.setText(EMPTY_STR);
    }

    private void connectToServer() {
        if (clientController.connectToServer(tfLogin.getText())) {
            panelTop.setVisible(false);
            login_name = tfLogin.getText();
            btnSend.setEnabled(true);
            btnLogin.setEnabled(false);
        }
    }



    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING){
            disconnectServer();
        }
    }

    public void hidePanelTop(boolean visible){
        panelTop.setVisible(visible);
    }

    public void disconnectServer(){
        clientController.disconnectServer();
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
    }
}
