package server.server.ui;

import server.client.ui.ClientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static server.Constants.EMPTY_STR;
import static server.Constants.NEW_STR;

public class ServerWindow extends JFrame{
    private static final int POS_X = 500;
    private static final int POS_Y = 550;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    public static final String LOG_PATH = "src/server/log.txt";
    public static final String SERVER_STARTED = "Server was started";
    public static final String SERVER_STOPPED = "Server was stopped";

    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private static final JTextArea log = new JTextArea();
    private boolean isServerWorking;
    private List<ClientGUI> clientGUIList;

    public static void main(String[] args) {
        new ServerWindow();
    }

    public ServerWindow(){
        clientGUIList = new ArrayList<>();
        isServerWorking = false;

        setBtnSettings();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("chatServer");

        createPanel();

        setVisible(true);
    }

    private void createPanel(){
        createLog();
        panelBottom.add(btnStart, BorderLayout.EAST);
        panelBottom.add(btnStop, BorderLayout.WEST);
        add(panelBottom, BorderLayout.SOUTH);
    }

    private void setBtnSettings(){
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = false;
                setTextLog(SERVER_STOPPED + NEW_STR);
                saveInLog(SERVER_STOPPED + NEW_STR);
            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = true;
                setTextLog(SERVER_STARTED + NEW_STR);
                saveInLog(SERVER_STOPPED + NEW_STR);
            }
        });
    }

    private void createLog(){
        log.setLineWrap(true);
        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);
    }

    public void setTextLog(String text){
        log.setText(log.getText() + text + NEW_STR);
    }

    public String getLogInfo(){
        return log.getText();
    }

    public boolean connectUser(ClientGUI clientGUI){
        if (!isServerWorking){
            return false;
        }
        clientGUIList.add(clientGUI);
        return true;
    }

    public void disconnectUser(ClientGUI clientGUI){
        clientGUIList.remove(clientGUI);
        if (clientGUI != null){
            clientGUI.disconnectFromServer();
        }
    }

    private void answerAll(String text){
        for (ClientGUI clientGUI: clientGUIList){
            clientGUI.answer(text);
        }
    }

    private String readLog(){
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(LOG_PATH);){
            int c;
            while ((c = reader.read()) != -1){
                stringBuilder.append((char) c);
            }
            stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
            return stringBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void saveInLog(String text){
        try (FileWriter writer = new FileWriter(LOG_PATH, true)){
            writer.write(text);
            writer.write(NEW_STR);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void message(String text){
        if (!isServerWorking){
            return;
        }
        text += EMPTY_STR;
        answerAll(text);
        saveInLog(text);
    }

    public String getLog() {
        return readLog();
    }
}
