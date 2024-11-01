import server.client.domain.ClientController;
import server.client.ui.ClientGUI;
import server.server.domain.ServerController;
import server.server.repository.FileStorage;
import server.server.ui.ServerWindow;

public class Main {
    public static void main(String[] args) {
        ServerController serverController = new ServerController(new ServerWindow(), new FileStorage());

        new ClientController(new ClientGUI(), serverController);
        new ClientController(new ClientGUI(), serverController);
    }
}