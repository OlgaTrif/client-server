package server.client.domain;

import server.client.ui.ClientView;
import server.server.domain.ServerController;

import java.net.DatagramSocket;
import java.net.InetAddress;

import static server.Constants.COLON;
import static server.Constants.NEW_STR;

public class ClientController {
    private boolean connected;
    private String name;
    private ClientView clientView;
    private ServerController serverController;

    private static final String LOGIN_SUCCESS = "Login successfully";
    private static final String LOGIN_ERROR = "Login successfully";
    private static final String SERVER_DISCONNECT = "Disconnected from server";
    private static final String NO_SERVER_CONNECTION = "No server connection";

    public ClientController(ClientView clientView, ServerController serverController) {
        this.clientView = clientView;
        this.serverController = serverController;
        clientView.setClientController(this);
    }

    public boolean connectToServer(String name) {
        this.name = name;
        if (serverController.connectUser(this)){
            showOnWindow(LOGIN_SUCCESS);
            connected = true;
            String log = serverController.getHistory();
            if (log != null){
                showOnWindow(log);
            }
            return true;
        } else {
            showOnWindow(LOGIN_ERROR);
            return false;
        }
    }

    public void answerFromServer(String text) {
        showOnWindow(text);
    }

    public void disconnectedFromServer() {
        if (connected) {
            connected = false;
            clientView.disconnectedFromServer();
            showOnWindow(SERVER_DISCONNECT);
        }
    }

    public void disconnectServer() {
        serverController.disconnectUser(this);
    }

    public void message(String text) {
        if (connected) {
            if (!text.isEmpty()) {
                serverController.message(name + COLON + text);
            }
        } else {
            showOnWindow(NO_SERVER_CONNECTION);
        }
    }

    public String getName() {
        return name;
    }

    private void showOnWindow(String text) {
        clientView.showMessage(text + NEW_STR);
    }
}