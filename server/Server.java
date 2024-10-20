package Client_server.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 12345;
    private JTextArea messageArea;
    private PrintWriter out;

    public Server() {
        createServerWindow();
        startServer();
    }

    private void createServerWindow() {
        JFrame frame = new JFrame("Сервер");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        JTextField inputField = new JTextField();
        frame.add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String response = inputField.getText();
                messageArea.append("Ответ сервера: " + response + "\n");
                out.println(response);  // Отправляем ответ обратно клиенту
                inputField.setText("");
            }
        });

        frame.setVisible(true);
    }

    private void startServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                messageArea.append("Ожидание подключения клиента...\n");
                Socket clientSocket = serverSocket.accept();  // Ждем подключения клиента
                messageArea.append("Клиент подключен!\n");

                // Получение потока для чтения сообщений от клиента
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String message;
                while ((message = in.readLine()) != null) {
                    messageArea.append("Сообщение от клиента: " + message + "\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

