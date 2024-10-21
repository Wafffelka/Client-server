package Client_server.main;

import Client_server.client.Client;
import Client_server.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.start();

        // Запуск двух клиентов
        Client client1 = new Client("Client1");
        client1.start();

        Client client2 = new Client("Client2");
        client2.start();
    }
}

