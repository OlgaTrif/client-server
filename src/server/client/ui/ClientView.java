package server.client.ui;

import server.client.domain.ClientController;

public interface ClientView {
    void showMessage(String message);
    void disconnectedFromServer();
    void setClientController(ClientController clientController);
}
