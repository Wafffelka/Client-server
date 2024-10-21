package Client_server.client;

import Client_server.server.Server;
import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String name;
    private Server server;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Прочитать имя пользователя
            name = in.readLine();
            System.out.println(name + " подключился.");
            server.broadcastMessage(name + " вошел в чат.", this);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(name + ": " + message);
                server.broadcastMessage(name + ": " + message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            server.broadcastMessage(name + " вышел из чата.", this);
            server.removeClient(this);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}