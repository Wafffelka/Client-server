package Client_server.main;

import Client_server.client.Client;
import Client_server.server.Server;

public class Main {
    public static void main(String[] args) {
        Server serverWindow = new Server();
        new Client(serverWindow);
        new Client(serverWindow);
    }
}

