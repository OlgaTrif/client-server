package server.server.domain;

import server.client.domain.ClientController;
import server.server.repository.Repository;
import server.server.ui.ServerView;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static server.Constants.NEW_STR;

public class ServerController {
    private boolean work;
    private ServerView serverView;
    private List<ClientController> clientControllerList;
    private Repository<String> repository;

    public static final String SERVER_STARTED = "Server was started";
    public static final String SERVER_START_ERROR = "Server is already working";
    public static final String SERVER_STOPPED = "Server was stopped";
    public static final String SERVER_STOP_ERROR = "Server is already stopped";
    public static final String USER_LOGOUT = "%s logout";
    public static final String USER_LOGIN= "%s login";

    public ServerController(ServerView serverView, Repository<String> repository) {
        this.serverView = serverView;
        this.repository = repository;
        clientControllerList = new ArrayList<>();
        serverView.setServerController(this);
    }

    public void start(){
        if (work){
            showOnWindow(SERVER_START_ERROR);
        } else {
            work = true;
            showOnWindow(SERVER_STARTED);
        }
    }

    public void stop(){
        if (!work){
            showOnWindow(SERVER_STOP_ERROR);
        } else {
            work = false;
            while (!clientControllerList.isEmpty()){
                disconnectUser(clientControllerList.get(clientControllerList.size() - 1));
            }
            showOnWindow(SERVER_STOPPED);
        }
    }

    public void disconnectUser(ClientController clientController){
        clientControllerList.remove(clientController);
        if (clientController != null){
            clientController.disconnectedFromServer();
            showOnWindow(String.format(USER_LOGOUT, clientController.getName()));
        }
    }

    public boolean connectUser(ClientController clientController){
        if (!work){
            return false;
        }
        clientControllerList.add(clientController);
        showOnWindow(String.format(USER_LOGIN, clientController.getName()));
        return true;
    }

    public void message(String text){
        if (!work){
            return;
        }
        showOnWindow(text);
        answerAll(text);
        saveInHistory(text);
    }

    public String getHistory() {
        return repository.load();
    }

    private void answerAll(String text){
        for (ClientController clientController : clientControllerList){
            clientController.answerFromServer(text);
        }
    }

    private void showOnWindow(String text){
        serverView.showMessage(text + NEW_STR);
    }

    private void saveInHistory(String text){
        repository.save(text);
    }
}
